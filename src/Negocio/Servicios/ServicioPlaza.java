package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Presentacion.Controladores.ControllerMenuPrincipalAdmin;

import java.util.ArrayList;

public class ServicioPlaza {
    private PlazaDBDAO PLAZA_DAO;
    private ReservaDBDAO RESERVA_DAO;

    public ServicioPlaza() {
        this.PLAZA_DAO = new PlazaDBDAO();
        this.RESERVA_DAO = new ReservaDBDAO();

    }


    public String saberTodaslasPlazas() {
        ArrayList<Plaza> plazas = PLAZA_DAO.getPlazas();
        ArrayList<Reserva> reservas = RESERVA_DAO.getReservas();

        StringBuilder info = new StringBuilder();

        for (Plaza plaza : plazas) {
            String matricula = "Libre";

            if (plaza.getEstado_reserva()) {
                for (Reserva reserva : reservas) {
                    if (reserva.getIdPlaza().equals(plaza.getCodigoPlaza())) {
                        matricula = reserva.getMatricula();
                        break; // importante cortar el bucle
                    }
                }
            }

            info.append("-").append(plaza.getCodigoPlaza()).append("-").append(plaza.getPlanta())
                    .append("-").append(plaza.getEstado_ocupado())
                    .append("-").append(matricula)
                    .append("\n");
        }

        return info.toString();
    }

    public void saberReserva(ServicioReserva reserva) {

    }

}
