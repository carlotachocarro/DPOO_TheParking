package Presentacion.Controladores;

import Negocio.Excepciones.ExcepcionDatosIncorrectos;
import Negocio.Excepciones.ExcepcionNegocio;
import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.RegistroPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorRegistroUsuario implements ActionListener {

    private final RegistroPanel vista;
    private final ServicioUsuario servicioUsuario;
    private final ControladorAplicacion app;

    public ControladorRegistroUsuario(RegistroPanel vista, ControladorAplicacion app, ServicioUsuario servicioUsuario) {
        this.vista = vista;
        this.app = app;
        this.servicioUsuario = servicioUsuario;

        this.vista.addRegistroListener(this);
        this.vista.addIrALoginListener(e -> app.mostrarLogin());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            registrarUsuario();
        } catch (ExcepcionNegocio ex) {
            JOptionPane.showMessageDialog(vista,
                    ex.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarUsuario() throws ExcepcionNegocio {
        String nombre = vista.getNombreUsuario();
        String correo = vista.getCorreo();
        String password = vista.getPassword();
        String repetirPassword = vista.getConfirmarPassword();

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || repetirPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debes rellenar todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int resultado;
        try {
            resultado = servicioUsuario.registrarUsua(nombre, correo, password, repetirPassword);
        } catch (ExcepcionDatosIncorrectos ex) {
            JOptionPane.showMessageDialog(vista,
                    ex.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (resultado) {
            case -1:
                JOptionPane.showMessageDialog(
                        vista,
                        "El correo electrónico no es correcto",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                vista.limpiarCampos();
                vista.focoEnCorreo();
                break;

            case 0:
                JOptionPane.showMessageDialog(
                        vista,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                vista.limpiarCampos();
                vista.focoEnPassword();
                break;

            case 1:
                JOptionPane.showMessageDialog(
                        vista,
                        "La contraseña no cumple la política MIT",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                vista.limpiarCampos();
                vista.focoEnPassword();
                break;

            case 2:
                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo registrar el usuario",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                vista.limpiarCampos();
                vista.focoEnNombreUsuario();
                break;

            case 3:
                JOptionPane.showMessageDialog(
                        vista,
                        "Ya existe el usuario, cambia el nombre",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                vista.limpiarCampos();
                vista.focoEnNombreUsuario();
                break;

            case 4:
                JOptionPane.showMessageDialog(
                        vista,
                        "Usuario registrado correctamente",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
                String idUsuario = servicioUsuario.idUsuarioParaSesion(nombre);
                app.abrirMenuUsuario(nombre, idUsuario != null ? idUsuario : "");
                break;

            default:
                JOptionPane.showMessageDialog(
                        vista,
                        "Ha ocurrido un error inesperado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
        }
    }
}
