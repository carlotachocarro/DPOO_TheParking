package Negocio.Servicios;

import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.UsuarioDAO;
import Persistencia.Daoimpl.UsuarioDBDAO;
import Negocio.Entidades.*;

import java.util.ArrayList;

public class ServicioReserva {
    private ReservaDBDAO reservaDBDAO;
    private UsuarioDBDAO usuarioDBDAO;
    private PlazaDBDAO plazaDBDAO;


    public ServicioReserva() {

        this.reservaDBDAO = new ReservaDBDAO();
        this.usuarioDBDAO = new UsuarioDBDAO();
        this.plazaDBDAO = new PlazaDBDAO();
    }


    public boolean realizarReservaVeiculo(String id_plaza, String matricula,String usuario){
        String idUsuario;

        if (esCorreoElectronico(usuario)){
             idUsuario = usuarioDBDAO.getUsuarioId(usuario,null);
        }else{
             idUsuario = usuarioDBDAO.getUsuarioId(null,usuario);
        }

        if(reservaDBDAO.nuevaReserva( id_plaza,idUsuario,matricula)){
            return true;
        }
        return false;
    }

    public boolean esCorreoElectronico(String texto) {
        return texto.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


    public ArrayList<Plaza> buscarPlazasDeParking (){

        ArrayList<Plaza> plazas = plazaDBDAO.getPlazas();

        return plazas;
    }

}
