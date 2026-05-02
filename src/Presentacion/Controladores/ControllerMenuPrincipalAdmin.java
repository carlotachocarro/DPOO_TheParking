package Presentacion.Controladores;


import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.Panels.ReservasPlazaPanel;

public class ControllerMenuPrincipalAdmin {
    private ServicioPlaza servicioPlaza;
    private ServicioReserva servicioReserva;
    private MainUsuarioFrame mainUsuarioFrame;



    public ControllerMenuPrincipalAdmin() {
        this.servicioPlaza = new ServicioPlaza();
        this.servicioReserva = new ServicioReserva(servicioPlaza);

       // new ControladorReservasPlaza(reservasPanel,mainUsuarioFrame.getNombreUsuario(),servicioPlaza);

    }



    public ServicioPlaza getServicioPlaza() {

        return servicioPlaza;
    }

    public String actualizarPlazasLibres(){
       String Value =servicioPlaza.Actualizar_PlazasMenu();

        return Value;
    }


    public String actualizarEstadoParking() {
         return servicioPlaza.saberTodaslasPlazas();
    }


}
