package Presentacion.Controladores;

import Negocio.Entidades.Plaza;
import Negocio.Servicios.ServicioPlaza;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Vistas.Dialogs.PlazaFormDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Flujo POP/AP para editar una plaza existente: diálogo + persistencia vía {@link ServicioPlaza#adminActualizarPlaza}.
 */
public class ControladorPOPAP_EditarPlaza {

    private final Window parentWindow;
    private final ServicioPlaza servicioPlaza;
    private final Runnable onPlazaActualizada;

    public ControladorPOPAP_EditarPlaza(Window parentWindow,
                                        ServicioPlaza servicioPlaza,
                                        Runnable onPlazaActualizada) {
        this.parentWindow = parentWindow;
        this.servicioPlaza = servicioPlaza;
        this.onPlazaActualizada = onPlazaActualizada;
    }

    /**
     * Abre el formulario de edición para el código indicado. Refresca la vista con el callback solo si guarda bien.
     *
     * @return {@code true} si el usuario confirmó y la actualización en BD tuvo éxito
     */
    public boolean abrirDialogo(String codigoPlaza) throws ExcepcionGeneralDB {
        Plaza p = servicioPlaza.obtenerPlazaPorCodigo(codigoPlaza);
        if (p == null) {
            JOptionPane.showMessageDialog(parentWindow,
                    "No se encontró la plaza seleccionada.",
                    "Editar plaza",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        PlazaFormDialog dlg = PlazaFormDialog.paraEditar(parentWindow,
                p.getCodigoPlaza(),
                String.valueOf(p.getPlanta()),
                ServicioPlaza.tipoVehiculoParaCombo(p.getTipoVehiculo()));
        dlg.setVisible(true);

        if (!dlg.fueConfirmado()) {
            return false;
        }

        if (servicioPlaza.adminActualizarPlaza(p.getCodigoPlaza(), dlg.getTipoVehiculo(), dlg.getPiso())) {
            JOptionPane.showMessageDialog(parentWindow,
                    "Plaza actualizada correctamente.",
                    "Editar plaza",
                    JOptionPane.INFORMATION_MESSAGE);
            onPlazaActualizada.run();
            return true;
        }

        JOptionPane.showMessageDialog(parentWindow,
                "No se pudieron guardar los cambios.",
                "Editar plaza",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
