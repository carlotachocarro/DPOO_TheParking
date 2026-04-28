package Presentacion.Controladores;


import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;

public class ControllerMenuPrincipalAdmin {
    private ServicioPlaza servicioPlaza;
    private ServicioReserva servicioReserva;


    public ControllerMenuPrincipalAdmin() {
        this.servicioPlaza = new ServicioPlaza();
        this.servicioReserva = new ServicioReserva();
    }

    public String actualizarPlazasLibres(){
       String Value =servicioPlaza.Actualizar_PlazasMenu();

        return Value;
    }


    public String actualizarEstadoParking() {
         return servicioPlaza.saberTodaslasPlazas();
    }


}
