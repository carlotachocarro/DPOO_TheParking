package Presentacion.Controladores;


import Negocio.Entidades.Plaza;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Presentacion.MainAdminFrame;
import Presentacion.Vistas.LoginPanel;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.Panels.EstadoParkingPanel;

import java.util.ArrayList;

public class ControllerMenuPrincipalAdmin {
    private ServicioPlaza servicioPlaza;
    private ServicioReserva servicioReserva;


    public ControllerMenuPrincipalAdmin() {
        this.servicioPlaza = new ServicioPlaza();
        this.servicioReserva = new ServicioReserva();
    }

    public String actualuzarPlazasLibres(){
       String Value =servicioPlaza.Actualizar_PlazasMenu();

        return Value;
    }


    public String actualizarEstadoParking() {
         return servicioPlaza.saberTodaslasPlazas();
    }


}
