package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import java.util.ArrayList;

public class ServicioVehiculo {

    private PlazaDBDAO plazaDBDAO;
    private ReservaDBDAO reservaDBDAO;

    public ServicioVehiculo() {
        this.plazaDBDAO = new PlazaDBDAO();
        this.reservaDBDAO = new ReservaDBDAO();
    }
    public String registroEntradaVehiculo(String matricula, String tipoVehiculo, String username) {
        ArrayList<Reserva> reservas = reservaDBDAO.getReservas();
        String IdplazaAsignada = null;
        //Bucle para ver si esta matricula tiene alguna reserva ya hecha
        for (Reserva r : reservas) {
            if (r.getMatricula().equals(matricula)) {
                IdplazaAsignada = r.getIdPlaza();
                break;
            }
        }
        //En caso de que no tenga reserva:
        if (IdplazaAsignada == null) {
            ArrayList<Plaza> plazasLibres = plazaDBDAO.getPlazasLibres(tipoVehiculo);
            if (!plazasLibres.isEmpty()) {
                IdplazaAsignada = plazasLibres.getFirst().getCodigoPlaza(); //Cogemos la primera disponible
            }
        }
        //Si se ha conseguido una plaza, se ocupa
        if (IdplazaAsignada != null) {
            if (plazaDBDAO.ocuparPlaza(IdplazaAsignada, true, matricula, username)) {
                return IdplazaAsignada;
            }
        }

        //Si no se ha conseguido una plaza, se retorna null
        return null;
    }

    public boolean registrarSalidaVehiculo(String matricula){
        ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
        for(Plaza p : plazasTotal){
            if(p.getEstado_ocupado() && p.getMatricula().equals(matricula)){ //Buscamos la plaza que tenia reservada
                //Vaciamos plaza
                return plazaDBDAO.ocuparPlaza(p.getCodigoPlaza(), false, "none", null);
            }
        }
        //No se ha podido encontrar la matricula
        return false;
        ///
    }
}
