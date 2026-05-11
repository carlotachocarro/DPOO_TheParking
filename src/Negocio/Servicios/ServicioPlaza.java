package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
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

    private boolean eliminarPlaza(String id) {

        if (PLAZA_DAO.eliminarPlaza(id)) {
            return true;
        }
        return false;
    }



}
