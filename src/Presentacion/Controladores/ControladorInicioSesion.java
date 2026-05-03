package Presentacion.Controladores;

import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.LoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorInicioSesion implements ActionListener {

    private final LoginPanel vista;
    private final ServicioUsuario servicioUsuario;
    private final ControladorAplicacion app;

    public ControladorInicioSesion(LoginPanel vista, ControladorAplicacion app) {
        this.vista = vista;
        this.app = app;
        this.servicioUsuario = new ServicioUsuario();

        this.vista.addLoginListener(this);
        this.vista.addIrARegistroListener(e -> app.mostrarRegistro());

        servicioUsuario.registrarAdmin();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        iniciarSesion();
    }

    private void iniciarSesion() {
        String usuarioCorreo = vista.getUsuarioCorreo();
        String password = vista.getPassword();

        if (usuarioCorreo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Debes rellenar usuario/correo y contraseña",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean loginCorrecto = servicioUsuario.inicioSession(usuarioCorreo, password);

        if (!loginCorrecto) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Contraseña o usuario incorrecto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            vista.limpiarCampos();
            vista.focoEnUsuarioCorreo();
            return;
        }

        if (usuarioCorreo.equalsIgnoreCase("admin")) {
            app.abrirMenuAdmin();
        } else {
            app.abrirMenuUsuario(usuarioCorreo);
        }
    }
}