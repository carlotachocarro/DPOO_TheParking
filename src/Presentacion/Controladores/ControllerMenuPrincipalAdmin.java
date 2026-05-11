package Presentacion.Controladores;


import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.Panels.ReservasPlazaPanel;

public class ControllerMenuPrincipalAdmin {
    private ServicioPlaza servicioPlaza;
    private ServicioReserva servicioReserva;
    private MainUsuarioFrame mainUsuarioFrame;
    private ServicioUsuario servicioUsuario;



    public ControllerMenuPrincipalAdmin() {
        this.servicioPlaza = new ServicioPlaza();
        this.servicioReserva = new ServicioReserva(servicioPlaza);
        this.servicioUsuario = new ServicioUsuario();

       // new ControladorReservasPlaza(reservasPanel,mainUsuarioFrame.getNombreUsuario(),servicioPlaza);

    }

    public boolean eliminarCuenta(String name){
        if (servicioUsuario.eliminarCuenta(name)){
            return true;
        }
        return false;

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
