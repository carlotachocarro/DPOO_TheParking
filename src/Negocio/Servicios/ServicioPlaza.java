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
            String matricula = "*";
            String estadoPlaza = "Libre";
            String Reserva = "Disponible";

            if (plaza.getEstado_reserva()){
                Reserva = "Reservado";
            }
            if (plaza.getEstado_ocupado()){
                estadoPlaza = "Ocupado";
            }

            if (plaza.getEstado_reserva()) {
                for (Reserva reserva : reservas) {
                    if (reserva.getIdPlaza().equals(plaza.getCodigoPlaza())) {
                        matricula = reserva.getMatricula();
                        break; // importante cortar el bucle
                    }
                }
            }

            info.append("-").append(plaza.getCodigoPlaza()).append("-").append(plaza.getPlanta())
                    .append("-").append(estadoPlaza)
                    .append("-").append(Reserva)
                    .append("-").append(matricula)
                    .append("\n");
        }

        return info.toString();
    }

    public String Actualizar_PlazasMenu(){
        ArrayList<Plaza> plazas = PLAZA_DAO.getPlazas();
        int plazaLibre=0;
        int PlazaReserva=0;
        int PlazaOcupado=0;
        int TotalPlazas=0;

        for (Plaza plaza : plazas) {
            TotalPlazas++;
            if (plaza.getEstado_reserva()) {
                PlazaReserva++;
            }

            if (plaza.getEstado_ocupado()) {
                PlazaOcupado++;
            }
            else {
                plazaLibre++;
            }

        }

        String info = plazaLibre+","+PlazaReserva+","+PlazaOcupado+","+TotalPlazas;
        return info;

    }

    public void saberReserva(ServicioReserva reserva) {

    }

}
