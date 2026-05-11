package Presentacion.Controladores;

import Negocio.Servicios.ServicioPlaza;
import Presentacion.Vistas.Dialogs.PlazaFormDialog;

import javax.swing.*;
import java.awt.*;

public class ControladorPOPAP_NuevaPlaza {

    private final Window parentWindow;
    private final ServicioPlaza servicioPlaza;
    private final Runnable onPlazaCreada; // callback para refrescar el panel

    /**
     * @param parentWindow   Ventana padre del diálogo (MainAdminFrame)
     * @param servicioPlaza  Servicio de lógica de negocio
     * @param onPlazaCreada  Callback que se ejecuta si la plaza se crea con éxito
     *                       (normalmente: panelGestionPlazas.mostrarPlazas())
     */
    public ControladorPOPAP_NuevaPlaza(Window parentWindow,
                                       ServicioPlaza servicioPlaza,
                                       Runnable onPlazaCreada) {
        this.parentWindow   = parentWindow;
        this.servicioPlaza  = servicioPlaza;
        this.onPlazaCreada  = onPlazaCreada;
    }

    /**
     * Abre el diálogo de nueva plaza y gestiona todo el flujo.
     * Llamar desde MainAdminFrame al pulsar "+ Nueva Plaza".
     */
    public void abrirDialogo() {
        PlazaFormDialog dialog = PlazaFormDialog.paraNueva(parentWindow);
        dialog.setVisible(true); // bloqueante hasta que el usuario cierra

        if (!dialog.fueConfirmado()) {
            return; // usuario canceló → no hacer nada
        }

        String piso = dialog.getPiso();
        String tipo = dialog.getTipoVehiculo();

        boolean exito = servicioPlaza.adminCrearPlaza(tipo, piso);

        if (exito) {
            JOptionPane.showMessageDialog(
                    parentWindow,
                    "Plaza creada correctamente en el piso " + piso + " para " + tipo + ".",
                    "Plaza creada",
                    JOptionPane.INFORMATION_MESSAGE
            );
            onPlazaCreada.run(); // refresca el panel de plazas
        } else {
            JOptionPane.showMessageDialog(
                    parentWindow,
                    "No se ha podido crear la plaza. Comprueba la conexión con la base de datos.",
                    "Error al crear plaza",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}