package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Negocio.Entidades.AvisoCancelacionUsuario;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.AvisoLoginDBDAO;
import java.util.ArrayList;
import java.util.List;

public class ServicioPlaza {
    private PlazaDBDAO PLAZA_DAO;
    private ReservaDBDAO RESERVA_DAO;
    private List<ParkingObserver> observers = new ArrayList<>();

    public ServicioPlaza() {
        this.PLAZA_DAO = new PlazaDBDAO();
        this.RESERVA_DAO = new ReservaDBDAO();

    }


    public String saberTodaslasPlazas() {
        ArrayList<Plaza> plazas = PLAZA_DAO.getPlazas();
        ArrayList<Reserva> reservas = RESERVA_DAO.getReservas();

        StringBuilder info = new StringBuilder();

        for (Plaza plaza : plazas) {
            String matricula = "*";
            String estadoPlaza = "Libre";
            String Reserva = "Disponible";
            String data = "";

            if (plaza.getEstado_reserva()) {
                Reserva = "Reservado";
            }
            if (plaza.getEstado_ocupado()) {
                estadoPlaza = "Ocupado";
            }

            if (plaza.getEstado_reserva()) {
                for (Reserva reserva : reservas) {
                    if (reserva.getIdPlaza().equals(plaza.getCodigoPlaza())) {
                        matricula = reserva.getMatricula();
                        data = reserva.getDate();
                        break; // importante cortar el bucle
                    }
                }
            }

            info.append("-").append(plaza.getCodigoPlaza()).append("-").append(plaza.getPlanta())
                    .append("-").append(estadoPlaza)
                    .append("-").append(Reserva)
                    .append("-").append(matricula)
                    .append("-").append(data)
                    .append("\n");
        }

        return info.toString();
    }

    /** Número de plazas definidas en el aparcamiento (todas las filas en BD). */
    public int getTotalPlazasEnSistema() {
        return PLAZA_DAO.getPlazas().size();
    }

    public String Actualizar_PlazasMenu() {
        ArrayList<Plaza> plazas = PLAZA_DAO.getPlazas();
        int plazaLibre = 0;
        int PlazaReserva = 0;
        int PlazaOcupado = 0;
        int TotalPlazas = 0;

        for (Plaza plaza : plazas) {
            TotalPlazas++;
            if (plaza.getEstado_reserva()) {
                PlazaReserva++;
            }

            if (plaza.getEstado_ocupado()) {
                PlazaOcupado++;
            } else {
                plazaLibre++;
            }

        }

        String info = plazaLibre + "," + PlazaReserva + "," + PlazaOcupado + "," + TotalPlazas;
        return info;

    }


    public void addObserver(ParkingObserver o) {
        observers.add(o);
    }

    public void removeObserver(ParkingObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() {

        String estado = saberTodaslasPlazas();
        String resumen = Actualizar_PlazasMenu();


        for (ParkingObserver o : observers) {
            o.onParkingChange(estado, resumen);
        }
    }

    /**
     * Busca plaza por código (id en BD tal como llega desde la tabla).
     */
    public Plaza obtenerPlazaPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        for (Plaza p : PLAZA_DAO.getPlazas()) {
            if (codigo.equals(p.getCodigoPlaza())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Iguala el texto guardado en BD con las opciones del combo del formulario: Moto, Coche, Coche grande.
     */
    public static String tipoVehiculoParaCombo(String desdeBd) {
        if (desdeBd == null || desdeBd.isBlank()) {
            return "Coche";
        }
        String t = desdeBd.trim();
        for (String opt : new String[]{"Moto", "Coche", "Coche grande"}) {
            if (opt.equalsIgnoreCase(t)) {
                return opt;
            }
        }
        return "Coche";
    }

    public boolean adminCrearPlaza(String tipoPlaza, String planta) {
        {
            if (PLAZA_DAO.crearPlaza(tipoPlaza, planta)) {
                return true;
            }
            return false;
        }


    }

    /**
     * Persiste cambios de tipo de vehículo y piso para una plaza ya existente.
     */
    public boolean adminActualizarPlaza(String codigoPlaza, String tipoVehiculo, String pisoTexto) {
        if (codigoPlaza == null || codigoPlaza.isBlank() || tipoVehiculo == null || pisoTexto == null) {
            return false;
        }
        try {
            int planta = Integer.parseInt(pisoTexto.trim());
            if (PLAZA_DAO.actualizarDatosPlaza(codigoPlaza.trim(), tipoVehiculo, planta)) {
                notifyObservers();
                return true;
            }
        } catch (NumberFormatException ignored) {
            return false;
        }
        return false;
    }

    /**
     * Elimina una plaza: no permitido si hay vehículo aparcado. Con reserva, reasigna a plaza libre del mismo tipo o elimina la reserva (enunciado).
     */
    public boolean adminEliminarPlaza(String codigoPlaza) {
        if (codigoPlaza == null || codigoPlaza.isBlank()) {
            return false;
        }
        String id = codigoPlaza.trim();
        try {
            Plaza p = obtenerPlazaPorCodigo(id);
            if (p == null) {
                return false;
            }
            if (p.getEstado_ocupado()) {
                return false;
            }

            if (p.getEstado_reserva()) {
                Reserva r = encontrarReservaPorIdPlaza(id);
                if (r != null) {
                    Plaza destino = primeraPlazaLibreMismoTipoDistinta(id, p.getTipoVehiculo());
                    if (destino != null) {
                        if (!RESERVA_DAO.actualizarIdPlazaReserva(id, destino.getCodigoPlaza())) {
                            return false;
                        }
                        if (!PLAZA_DAO.limpiarPlaza(id)) {
                            return false;
                        }
                        if (!PLAZA_DAO.aplicarReservaEnPlaza(destino.getCodigoPlaza(), r.getIdCliente(), r.getMatricula())) {
                            return false;
                        }
                        insertarAvisoReasignacionPlaza(id, p, r, destino);
                    } else {
                        if (!RESERVA_DAO.borrarReserva(id, r.getIdCliente())) {
                            RESERVA_DAO.eliminarReservasDePlaza(id);
                        }
                        PLAZA_DAO.limpiarPlaza(id);
                    }
                } else {
                    RESERVA_DAO.eliminarReservasDePlaza(id);
                    PLAZA_DAO.limpiarPlaza(id);
                }
            } else {
                RESERVA_DAO.eliminarReservasDePlaza(id);
            }

            if (PLAZA_DAO.eliminarPlazaFisica(id)) {
                notifyObservers();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private Reserva encontrarReservaPorIdPlaza(String idPlaza) {
        for (Reserva r : RESERVA_DAO.getReservas()) {
            if (idPlaza.equals(r.getIdPlaza())) {
                return r;
            }
        }
        return null;
    }

    private Plaza primeraPlazaLibreMismoTipoDistinta(String idExcluir, String tipoVehiculo) {
        for (Plaza c : PLAZA_DAO.getPlazasLibres(tipoVehiculo)) {
            if (!idExcluir.equals(c.getCodigoPlaza())) {
                return c;
            }
        }
        return null;
    }

    private void insertarAvisoReasignacionPlaza(String idPlazaVieja, Plaza pVieja, Reserva r, Plaza destino) {
        try {
            AvisoLoginDBDAO dao = new AvisoLoginDBDAO();
            AvisoCancelacionUsuario aviso = new AvisoCancelacionUsuario(
                    idPlazaVieja,
                    String.valueOf(pVieja.getPlanta()),
                    r.getMatricula(),
                    pVieja.getTipoVehiculo(),
                    destino.getCodigoPlaza(),
                    String.valueOf(destino.getPlanta())
            );
            dao.insertar(r.getIdCliente(), aviso);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
