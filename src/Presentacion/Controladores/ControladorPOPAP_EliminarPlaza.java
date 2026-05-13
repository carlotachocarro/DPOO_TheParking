package Presentacion.Controladores;

import Negocio.Entidades.Plaza;
import Negocio.Servicios.ServicioPlaza;

import javax.swing.*;
import java.awt.*;

/**
 * Confirmación + eliminación de plaza (POP/AP), alineado con {@link ControladorPOPAP_NuevaPlaza} y {@link ControladorPOPAP_EditarPlaza}.
 */
public class ControladorPOPAP_EliminarPlaza {

    private final Window parentWindow;
    private final ServicioPlaza servicioPlaza;
    private final Runnable onEliminada;

    public ControladorPOPAP_EliminarPlaza(Window parentWindow,
                                          ServicioPlaza servicioPlaza,
                                          Runnable onEliminada) {
        this.parentWindow = parentWindow;
        this.servicioPlaza = servicioPlaza;
        this.onEliminada = onEliminada;
    }

    /**
     * Comprueba si se puede borrar, pide confirmación y llama a {@link ServicioPlaza#adminEliminarPlaza(String)}.
     *
     * @return {@code true} si la plaza se eliminó correctamente
     */
    public boolean confirmarYEliminar(String codigoPlaza) {
        Plaza p = servicioPlaza.obtenerPlazaPorCodigo(codigoPlaza);
        if (p == null) {
            JOptionPane.showMessageDialog(parentWindow,
                    "No se encontró la plaza.",
                    "Eliminar plaza",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (p.getEstado_ocupado()) {
            JOptionPane.showMessageDialog(parentWindow,
                    "No se puede eliminar la plaza mientras haya un vehículo aparcado.",
                    "Eliminar plaza",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String mensaje = "¿Eliminar la plaza " + codigoPlaza + "? Esta acción no se puede deshacer.";
        if (p.getEstado_reserva()) {
            mensaje += "\n\nSi hay una reserva asociada, se reasignará a otra plaza del mismo tipo; "
                    + "si no hay plaza libre compatible, se eliminará la reserva.";
        }

        int opcion = JOptionPane.showConfirmDialog(parentWindow,
                mensaje,
                "Eliminar plaza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) {
            return false;
        }

        if (servicioPlaza.adminEliminarPlaza(codigoPlaza)) {
            JOptionPane.showMessageDialog(parentWindow,
                    "Plaza eliminada.",
                    "Eliminar plaza",
                    JOptionPane.INFORMATION_MESSAGE);
            onEliminada.run();
            return true;
        }

        JOptionPane.showMessageDialog(parentWindow,
                "No se pudo eliminar la plaza.",
                "Eliminar plaza",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
