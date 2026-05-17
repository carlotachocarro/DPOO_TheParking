package Presentacion.Controladores;

import Negocio.Excepciones.ExcepcionDatosIncorrectos;
import Negocio.Excepciones.ExcepcionNegocio;
import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.LoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorInicioSesion implements ActionListener {

    private final LoginPanel vista;
    private final ServicioUsuario servicioUsuario;
    private final ControladorAplicacion app;

    public ControladorInicioSesion(LoginPanel vista, ControladorAplicacion app, ServicioUsuario servicioUsuario) {
        this.vista = vista;
        this.app = app;
        this.servicioUsuario = servicioUsuario;

        this.vista.addLoginListener(this);
        this.vista.addIrARegistroListener(e -> app.mostrarRegistro());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            iniciarSesion();
        } catch (ExcepcionNegocio ex) {
            JOptionPane.showMessageDialog(vista,
                    ex.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarSesion() throws ExcepcionNegocio {
        String usuarioCorreo = vista.getUsuarioCorreo();
        String password = vista.getPassword();

        if (usuarioCorreo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Debes rellenar usuario/correo y contraseña",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuarioCorreo.equalsIgnoreCase("admin")) {
            if (servicioUsuario.inicioSessionAdmin(password)) {
                app.abrirMenuAdmin();
            } else {
                JOptionPane.showMessageDialog(vista,
                        "Las credenciales introducidas son incorrectas.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                vista.limpiarCampos();
            }
            return;
        }

        boolean loginCorrecto;
        try {
            loginCorrecto = servicioUsuario.inicioSession(usuarioCorreo, password);
        } catch (ExcepcionDatosIncorrectos ex) {
            JOptionPane.showMessageDialog(vista,
                    "Las credenciales introducidas son incorrectas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            vista.limpiarCampos();
            vista.focoEnUsuarioCorreo();
            return;
        }

        if (!loginCorrecto) {
            JOptionPane.showMessageDialog(vista,
                    "Las credenciales introducidas son incorrectas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            vista.limpiarCampos();
            vista.focoEnUsuarioCorreo();
            return;
        }

        String idUsuario = servicioUsuario.idUsuarioParaSesion(usuarioCorreo);
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo obtener tu identificador de usuario para el parking.",
                    "Error de sesión",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        app.abrirMenuUsuario(usuarioCorreo.trim(), idUsuario);
    }
}
