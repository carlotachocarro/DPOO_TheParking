package Presentacion.Controladores;

import Negocio.Excepciones.ExcepcionNegocio;
import Negocio.Servicios.ServicioReserva;
import Presentacion.Vistas.Dialogs.CancelarReservaDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ControladorPOPAP_CancelarReserva implements ActionListener {

    private final CancelarReservaDialog cancelarReservaDialog;
    private final ServicioReserva servicioReserva;
    private final String nombreUsuario;

    public ControladorPOPAP_CancelarReserva(CancelarReservaDialog cancelarReservaDialog,
                                            String nombreUsuario,
                                            ServicioReserva servicioReserva) {
        this.cancelarReservaDialog = cancelarReservaDialog;
        this.nombreUsuario = nombreUsuario;
        this.servicioReserva = servicioReserva;
        this.cancelarReservaDialog.addCancelarReservaListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "volver":
                cancelarReservaDialog.dispose();
                break;

            case "cancelar":
                try {
                    if (servicioReserva.cancelarReserva(cancelarReservaDialog.getIdPlaza(), nombreUsuario)) {
                        JOptionPane.showMessageDialog(cancelarReservaDialog, "Reserva cancelada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(cancelarReservaDialog, "La reserva no se ha podido cancelar.");
                    }
                    cancelarReservaDialog.dispose();
                } catch (ExcepcionNegocio ex) {
                    JOptionPane.showMessageDialog(cancelarReservaDialog,
                            ex.getMensajeExcepcion(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
        }

    }


}
