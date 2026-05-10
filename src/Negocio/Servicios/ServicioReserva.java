package Negocio.Servicios;

import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.UsuarioDBDAO;
import Negocio.Entidades.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServicioReserva {
    private ReservaDBDAO reservaDBDAO;
    private UsuarioDBDAO usuarioDBDAO;
    private PlazaDBDAO plazaDBDAO;
    private ServicioPlaza servicioPlaza;
    private ServicioUsuario servicioUsuario;


    public ServicioReserva(ServicioPlaza servicioPlaza) {
        this.servicioUsuario= new ServicioUsuario();
        this.reservaDBDAO = new ReservaDBDAO();
        this.usuarioDBDAO = new UsuarioDBDAO();
        this.plazaDBDAO = new PlazaDBDAO();
        this.servicioPlaza = servicioPlaza;


    }


    public boolean realizarReservaVeiculo(String id_plaza, String matricula,String usuario){
        String idUsuario;

        if (servicioUsuario.validarCorreoElectro(usuario)){
             idUsuario = usuarioDBDAO.getUsuarioId(null,usuario);
        }else{
             idUsuario = usuarioDBDAO.getUsuarioId(usuario,null);
        }


        if (plazaDBDAO.ocuparPlaza(id_plaza,true,matricula,usuario)){
            if(reservaDBDAO.nuevaReserva( id_plaza,idUsuario,matricula)){
                // vamos poner que esta ocupada la plaza actualizar el main
                servicioPlaza.notifyObservers();
                // Vamos hacer un registro del historial de las plazas


                return true;
            }
        }
        return false;
    }

    /*
    vista.cargarPlazasDisponibles(List.of(
                                "1 - Planta 1 - Coche - Libre",
                                "B-02 - Planta 2 - Coche - Libre",
                                "B-04 - Planta 2 - Coche - Libre",
                                "C-01 - Planta 3 - Coche - Libre"));
    * */
    public List<String> buscarPlazasDeParking (String tipoVehiculo){

        ArrayList<Plaza> plazas = plazaDBDAO.getPlazasLibres(tipoVehiculo);
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



    public  List<String> ObtenerReservas(String Nombre){
        String[] mensaje;
        ArrayList<Reserva> reservas = new ArrayList<>();
        ArrayList<Plaza> plazas = new ArrayList<>();
        String idUsuario;
        if (servicioUsuario.validarCorreoElectro(Nombre)){
            idUsuario=usuarioDBDAO.getUsuarioId(null,Nombre);
        }
        else {
            idUsuario=usuarioDBDAO.getUsuarioId(Nombre,null);
        }

        reservas = reservaDBDAO.getReservaByUser(idUsuario);
        plazas =  plazaDBDAO.getPlazas();
        List<String> resultado = new ArrayList<>();
        //"B2", "Moto", "5678DEF", "02/05/2026", "Planta 2", false

        for (Reserva reserva : reservas) {
            Plaza plazaEncontrada = null;

            for (Plaza plaza : plazas) {
                String m=plaza.getCodigoPlaza();
                String rm = reserva.getIdPlaza();

                if (m.equals(rm))
                {
                    String estadoPlaza="";
                    if(plaza.getEstado_ocupado()){
                        estadoPlaza="Ocupada";
                    }
                    resultado.add(reserva.getIdPlaza()+"-"+plaza.getTipoVehiculo()+"-"+reserva.getMatricula()+"-"+reserva.getDate()+"-"+plaza.getPlanta()+"-"+ "True" );
                }

            }


        }
       return resultado;

    }

    public boolean cancelarReserva(String idPlaza,String nombre){

        String idUsuario;
        if (servicioUsuario.validarCorreoElectro(nombre)){
            idUsuario=usuarioDBDAO.getUsuarioId(null,nombre);
        }
        else {
            idUsuario=usuarioDBDAO.getUsuarioId(nombre,null);
        }

        if (reservaDBDAO.borrarReserva(idPlaza,idUsuario)){
            return true;
        }

        return false;
    }



}
