package Negocio.Servicios;

import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.UsuarioDAO;
import Persistencia.Daoimpl.UsuarioDBDAO;
import Negocio.Entidades.*;

public class ServicioReserva {
    private ReservaDBDAO reservaDBDAO;
    private UsuarioDBDAO usuarioDBDAO;
    private Usuario usuario;


    public ServicioReserva() {

        this.reservaDBDAO = new ReservaDBDAO();
        this.usuarioDBDAO = new UsuarioDBDAO();
    }


    public void realizarReservaVeiculo(String id_plaza, String matricula){
        /*
        String idUsuario = usuarioDBDAO.getUsuarioId(usuario.getNombre(),usuario.getCorreoElectronico());
        switch (reservaDBDAO.nuevaReserva( ,idUsuario, )){
            case 1:
                break;
                case 2:
                    break;
        }*/
    }
}
