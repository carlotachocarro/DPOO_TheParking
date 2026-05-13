package Presentacion.Controladores;

import Negocio.Servicios.ServicioReserva;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Vistas.Panels.ReservasPlazaPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorReservasPlaza implements ActionListener {

    private final ReservasPlazaPanel vista;
    private final ServicioReserva servicioReserva;
    private final String nombreUsuario;

    public ControladorReservasPlaza(ReservasPlazaPanel vista,
                                   String nombreUsuario,
                                   ServicioReserva servicioReserva) {
        this.nombreUsuario = nombreUsuario;
        this.vista = vista;
        this.servicioReserva = servicioReserva;

        this.vista.addRegistroListener(this);
    }

    public boolean esMatriculaValida(String matricula) {
        if (matricula == null) {
            return false;
        }
        String m = matricula.trim().toUpperCase();
        return m.matches("^[0-9]{4}\\s?[A-Z]{3}$");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "CONFIRM_RESERVA":
                String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();
                if (matricula.isEmpty() || !esMatriculaValida(matricula)) {
                    JOptionPane.showMessageDialog(vista,
                            "Matrícula inválida. Formato: 4 dígitos y 3 letras (ej: 1234 ABC).",
                            "Reservar plaza",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String seleccion = vista.getListaPlazas().getSelectedValue();
                if (seleccion == null || seleccion.isBlank()) {
                    JOptionPane.showMessageDialog(vista,
                            "Selecciona una plaza en la lista.",
                            "Reservar plaza",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String idPlaza = seleccion.split(" - ")[0];
                try {
                    if (!servicioReserva.realizarReservaVeiculo(idPlaza, matricula, nombreUsuario)) {
                        JOptionPane.showMessageDialog(vista, "No se ha podido realizar la reserva de la plaza.");
                        vista.limpiarCampos();
                        return;
                    }
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                } catch (ExcepcionFicheroNoEncontrado ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(vista, "Reserva realizada con éxito");
                vista.limpiarCampos();
                break;

            case "BUSCAR":
                try {
                    vista.cargarPlazasDisponibles(servicioReserva.buscarPlazasDeParking(vista.getComboTipoVehiculo()));
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                }
                break;

            default:
                break;
        }
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
