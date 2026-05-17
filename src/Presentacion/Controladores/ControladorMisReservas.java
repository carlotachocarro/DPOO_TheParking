package Presentacion.Controladores;

import Negocio.Excepciones.ExcepcionReservaPlaza;
import Negocio.Servicios.ParkingObserver;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Presentacion.Vistas.Dialogs.CancelarReservaDialog;
import Presentacion.Vistas.Panels.MisReservasPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del panel "Mis reservas". Es quien observa al servicio (no la vista)
 * y quien orquesta la apertura del diálogo de cancelación.
 */
public class ControladorMisReservas implements ParkingObserver {

    private final MisReservasPanel panel;
    private final ServicioReserva servicioReserva;
    private final ServicioPlaza servicioPlaza;
    private final String nombreUsuario;

    public ControladorMisReservas(MisReservasPanel panel,
                                  String nombreUsuario,
                                  ServicioPlaza servicioPlaza,
                                  ServicioReserva servicioReserva) {
        this.panel = panel;
        this.nombreUsuario = nombreUsuario;
        this.servicioPlaza = servicioPlaza;
        this.servicioReserva = servicioReserva;

        servicioPlaza.addObserver(this);
        panel.setCancelarReservaListener(this::abrirDialogoCancelar);
    }

    /** Carga inicial al montar el panel. */
    public void cargarInicial() {
        actualizarReservas();
    }

    @Override
    public void onParkingChange(String estado, String resumen) {
        SwingUtilities.invokeLater(this::actualizarReservas);
    }

    private void actualizarReservas() {
        try {
            List<String> raw = servicioReserva.ObtenerReservas(nombreUsuario);
            List<MisReservasPanel.ReservaVista> reservas = new ArrayList<>();
            for (String r : raw) {
                String[] partes = r.split("-");
                if (partes.length < 6) continue;
                reservas.add(new MisReservasPanel.ReservaVista(
                        partes[0], partes[1], partes[2], partes[3], partes[4],
                        Boolean.parseBoolean(partes[5])));
            }
            panel.setReservas(reservas);
        } catch (ExcepcionReservaPlaza e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoCancelar(MisReservasPanel.ReservaVista reserva) {
        Window padre = SwingUtilities.getWindowAncestor(panel);
        CancelarReservaDialog dlg = new CancelarReservaDialog(
                padre,
                reserva.codigoPlaza(),
                reserva.planta(),
                reserva.matricula(),
                reserva.fechaReserva(),
                reserva.tipoVehiculo());
        new ControladorPOPAP_CancelarReserva(dlg, nombreUsuario, servicioReserva);
        dlg.setVisible(true);
    }
}
