package Presentacion.Controladores;

import Negocio.Excepciones.ExcepcionNegocio;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioVehiculo;
import Presentacion.Vistas.Panels.EntradaSalidaPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorEntradasSalidas implements ActionListener {

    private final EntradaSalidaPanel entradaSalidaPanel;
    private final ServicioVehiculo servicioVehiculo;
    private final ServicioPlaza servicioPlaza;
    private final String nombreUsuario;

    public ControladorEntradasSalidas(EntradaSalidaPanel entradaSalidaPanel,
                                      String nombreUsuario,
                                      ServicioPlaza servicioPlaza,
                                      ServicioVehiculo servicioVehiculo) {
        this.entradaSalidaPanel = entradaSalidaPanel;
        this.nombreUsuario = nombreUsuario;
        this.servicioPlaza = servicioPlaza;
        this.servicioVehiculo = servicioVehiculo;
        entradaSalidaPanel.addEntradaSalidaListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String matricula = entradaSalidaPanel.getMatriculaEntrada();
        String matriculaSalida = entradaSalidaPanel.getMatriculaSalida();
        String tipoVhiculo = entradaSalidaPanel.getTipoVehiculoEntrada();


        switch (e.getActionCommand()) {
            case "ENTRADA":
                if (matricula == null || matricula.isEmpty()) {
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "Por favor introduzca la matrícula");
                    break;
                }

                try {
                    if (servicioVehiculo.registroEntradaVehiculo(matricula, tipoVhiculo, nombreUsuario) == null) {
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "La plaza no existe");
                    } else {
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "Se ha registrado una entrada correctamente.");
                    }
                    servicioPlaza.notifyObservers();
                } catch (ExcepcionNegocio ex) {
                    JOptionPane.showMessageDialog(entradaSalidaPanel,
                            ex.getMensajeExcepcion(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                entradaSalidaPanel.limpiarCampos();
                break;

            case "SALIDA":

                if (matriculaSalida == null || matriculaSalida.isEmpty()) {
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "Por favor introduzca la matrícula");
                    break;
                }

                try {
                    if (!servicioVehiculo.registrarSalidaVehiculo(matriculaSalida)) {
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "La plaza no existe");
                    } else {
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "Se ha registrado una salida correctamente.");
                    }
                    servicioPlaza.notifyObservers();
                } catch (ExcepcionNegocio ex) {
                    JOptionPane.showMessageDialog(entradaSalidaPanel,
                            ex.getMensajeExcepcion(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                entradaSalidaPanel.limpiarCampos();
                break;
        }
    }
}
