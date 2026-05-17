package Presentacion.Controladores;

import Negocio.Servicios.ServicioReserva;
import Presentacion.Vistas.Dialogs.NotificacionesDialog;

import java.awt.*;

/**
 * Muestra {@link NotificacionesDialog} tras el login si hay avisos pendientes y luego los elimina de BD.
 * Pendiente de implementación cuando exista {@code AvisoDBDAO} (la tabla está definida en
 * {@code src/CONFIG/schema_aviso_cancelacion.sql}).
 */
public final class ControladorPOPAP_Notificaciones {

    private ControladorPOPAP_Notificaciones() {
    }

    public static void mostrarSiCorresponde(Window parent, ServicioReserva servicioReserva, String idUsuario) {
        // TODO: leer avisos del usuario, mostrar NotificacionesDialog y borrarlos
    }
}
