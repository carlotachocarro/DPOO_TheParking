package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Negocio.Entidades.AvisoCancelacionUsuario;
import Negocio.Excepciones.ExcepcionCrearPlaza;
import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.util.ArrayList;
import java.util.List;

public class ServicioPlaza {
    private PlazaDBDAO PLAZA_DAO;
    private ReservaDBDAO RESERVA_DAO;
    private List<ParkingObserver> observers = new ArrayList<>();

    public ServicioPlaza() throws ExcepcionFicheroConfig {
        try {
            this.PLAZA_DAO = new PlazaDBDAO();
            this.RESERVA_DAO = new ReservaDBDAO();
        } catch (ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionFicheroConfig(e);
        }
    }


    public String saberTodaslasPlazas() throws ExcepcionEntradaSalidaPlaza {
        try {
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
                            break;
                        }
                    }
                }

                info.append("-").append(plaza.getCodigoPlaza()).append("-").append(plaza.getPlanta())
                        .append("-").append(estadoPlaza)
                        .append("-").append(Reserva)
                        .append("-").append(matricula)
                        .append("-").append(data)
                        .append("-").append(plaza.getTipoVehiculo())
                        .append("\n");
            }

            return info.toString();
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    /** Número de plazas definidas en el aparcamiento (todas las filas en BD). */
    public int getTotalPlazasEnSistema() throws ExcepcionEntradaSalidaPlaza {
        try {
            return PLAZA_DAO.getPlazas().size();
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    public String Actualizar_PlazasMenu() throws ExcepcionEntradaSalidaPlaza {
        try {
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

            return plazaLibre + "," + PlazaReserva + "," + PlazaOcupado + "," + TotalPlazas;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }


    public void addObserver(ParkingObserver o) {
        observers.add(o);
    }

    public void removeObserver(ParkingObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() throws ExcepcionEntradaSalidaPlaza {

        String estado = saberTodaslasPlazas();
        String resumen = Actualizar_PlazasMenu();


        for (ParkingObserver o : observers) {
            o.onParkingChange(estado, resumen);
        }
    }

    /**
     * Busca plaza por código (id en BD tal como llega desde la tabla).
     */
    public Plaza obtenerPlazaPorCodigo(String codigo) throws ExcepcionEntradaSalidaPlaza {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        try {
            for (Plaza p : PLAZA_DAO.getPlazas()) {
                if (codigo.equals(p.getCodigoPlaza())) {
                    return p;
                }
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
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

    public boolean adminCrearPlaza(String tipoPlaza, String planta) throws ExcepcionCrearPlaza {
        try {
            return PLAZA_DAO.crearPlaza(tipoPlaza, planta);
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionCrearPlaza(e);
        }
    }

    /**
     * Persiste cambios de tipo de vehículo y piso para una plaza ya existente.
     */
    public boolean adminActualizarPlaza(String codigoPlaza, String tipoVehiculo, String pisoTexto) throws ExcepcionEntradaSalidaPlaza {
        if (codigoPlaza == null || codigoPlaza.isBlank() || tipoVehiculo == null || pisoTexto == null) {
            return false;
        }
        int planta;
        try {
            planta = Integer.parseInt(pisoTexto.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        try {
            if (PLAZA_DAO.actualizarDatosPlaza(codigoPlaza.trim(), tipoVehiculo, planta)) {
                notifyObservers();
                return true;
            }
            return false;
        } catch (ExcepcionEntradaSalidaPlaza e) {
            throw e;
        }
    }

    /**
     * Elimina una plaza: no permitido si hay vehículo aparcado. Con reserva, reasigna a plaza libre del mismo tipo o elimina la reserva (enunciado).
     */
    public boolean adminEliminarPlaza(String codigoPlaza) throws ExcepcionEntradaSalidaPlaza {
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
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    private Reserva encontrarReservaPorIdPlaza(String idPlaza) throws ExcepcionEntradaSalidaPlaza {
        try {
            for (Reserva r : RESERVA_DAO.getReservas()) {
                if (idPlaza.equals(r.getIdPlaza())) {
                    return r;
                }
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
        return null;
    }

    private Plaza primeraPlazaLibreMismoTipoDistinta(String idExcluir, String tipoVehiculo) throws ExcepcionEntradaSalidaPlaza {
        try {
            for (Plaza c : PLAZA_DAO.getPlazasLibres(tipoVehiculo)) {
                if (!idExcluir.equals(c.getCodigoPlaza())) {
                    return c;
                }
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
        return null;
    }

    private void insertarAvisoReasignacionPlaza(String idPlazaVieja, Plaza pVieja, Reserva r, Plaza destino) {
        AvisoCancelacionUsuario aviso = new AvisoCancelacionUsuario(
                idPlazaVieja,
                String.valueOf(pVieja.getPlanta()),
                r.getMatricula(),
                pVieja.getTipoVehiculo(),
                destino.getCodigoPlaza(),
                String.valueOf(destino.getPlanta())
        );
        // TODO: persistir el aviso en BD cuando exista AvisoDBDAO.
    }


}
