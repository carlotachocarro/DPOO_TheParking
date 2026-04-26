package Negocio.Servicios;

import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.UsuarioDAO;
import Persistencia.Daoimpl.UsuarioDBDAO;
import Negocio.Entidades.*;

import java.util.ArrayList;
import java.util.List;

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

    /*
    vista.cargarPlazasDisponibles(List.of(
                                "1 - Planta 1 - Coche - Libre",
                                "B-02 - Planta 2 - Coche - Libre",
                                "B-04 - Planta 2 - Coche - Libre",
                                "C-01 - Planta 3 - Coche - Libre"));
    * */
    public List<String> buscarPlazasDeParking (){

        ArrayList<Plaza> plazas = plazaDBDAO.getPlazas();
        List<String> resultado = new ArrayList<>();

        for( Plaza plaza : plazas){
            resultado.add(formatearPlazaParaVista(plaza));
        }


        return resultado;
    }


    private String formatearPlazaParaVista(Plaza plaza) {

        String estado;

        if (plaza.getEstado_reserva()) {
            estado = "Reservada";
        } else if (plaza.getEstado_ocupado()) {
            estado = "Ocupada";
        } else {
            estado = "Libre";
        }

        return plaza.getCodigoPlaza()
                + " - Planta " + plaza.getPlanta()
                + " - " + plaza.getTipoVehiculo()
                + " - " + estado;
    }


}
