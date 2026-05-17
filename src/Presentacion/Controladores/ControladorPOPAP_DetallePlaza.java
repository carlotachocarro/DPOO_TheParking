package Presentacion.Controladores;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Usuario;
import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Excepciones.ExcepcionReservaPlaza;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Presentacion.Vistas.Dialogs.DetallePlazaDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Controlador del diálogo "Detalle de plaza" del admin. Asume la responsabilidad
 * que estaba en {@code MainAdminFrame.abrirDetallePlaza()}: pedir la plaza al
 * servicio, construir el diálogo y gestionar la cancelación de la reserva.
 */
public class ControladorPOPAP_DetallePlaza {

    private final Window parent;
    private final ServicioPlaza servicioPlaza;
    private final ServicioReserva servicioReserva;
    private final Runnable onCambio;

    public ControladorPOPAP_DetallePlaza(Window parent,
                                         ServicioPlaza servicioPlaza,
                                         ServicioReserva servicioReserva,
                                         Runnable onCambio) {
        this.parent = parent;
        this.servicioPlaza = servicioPlaza;
        this.servicioReserva = servicioReserva;
        this.onCambio = onCambio;
    }

    public void abrirDialogo(String codigoPlaza) {
        try {
            Plaza p = servicioPlaza.obtenerPlazaPorCodigo(codigoPlaza);
            if (p == null) {
                JOptionPane.showMessageDialog(parent,
                        "No se encontró la plaza.",
                        "Detalle de plaza",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String estado = p.getEstado_ocupado() ? "Ocupada" : "Libre";
            String reservaTxt = p.getEstado_reserva() ? "Reservada" : "Disponible";
            String mat = p.getMatricula();
            if (mat == null || mat.isBlank() || "none".equalsIgnoreCase(mat)) {
                mat = null;
            }
            Usuario u = p.getUser();
            String nombre = u != null ? u.getNombre() : null;
            String correo = u != null ? u.getCorreoElectronico() : null;

            DetallePlazaDialog dlg = new DetallePlazaDialog(
                    parent,
                    p.getCodigoPlaza(),
                    String.valueOf(p.getPlanta()),
                    ServicioPlaza.tipoVehiculoParaCombo(p.getTipoVehiculo()),
                    estado,
                    reservaTxt,
                    mat,
                    nombre,
                    correo);

            if ("Reservada".equalsIgnoreCase(reservaTxt)) {
                dlg.setCancelarReservaListener(this::cancelarReserva);
            }
            dlg.setVisible(true);
        } catch (ExcepcionEntradaSalidaPlaza ex) {
            JOptionPane.showMessageDialog(parent,
                    ex.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReserva(String plazaId) {
        try {
            if (servicioReserva.adminCancelarReservaEnPlaza(plazaId)) {
                JOptionPane.showMessageDialog(parent, "Reserva cancelada.");
                onCambio.run();
            } else {
                JOptionPane.showMessageDialog(parent,
                        "No se pudo cancelar la reserva.",
                        "Detalle de plaza",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (ExcepcionReservaPlaza ex) {
            JOptionPane.showMessageDialog(parent,
                    ex.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
