package Presentacion.Controladores;

import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.LoginPanel;

import javax.swing.*;
import java.awt.*;
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

       // servicioUsuario.registrarAdmin();
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
                    vista, "Debes rellenar usuario/correo y contraseña", "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (usuarioCorreo.equalsIgnoreCase("admin")) {

            if (servicioUsuario.inicioSessionAdmin(password)) {
                app.abrirMenuAdmin();
            } else {
                JOptionPane.showMessageDialog(vista, "Contraseña o usuario incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                vista.limpiarCampos();
            }

        }
        else {
            boolean loginCorrecto = servicioUsuario.inicioSession(usuarioCorreo, password);
            if (!loginCorrecto) {
                JOptionPane.showMessageDialog(vista, "Contraseña o usuario incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
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

            ServicioPlaza plazaAux = new ServicioPlaza();
            ServicioReserva reservaAux = new ServicioReserva(plazaAux);
            Window win = SwingUtilities.getWindowAncestor(vista);
            ControladorPOPAP_Notificaciones.mostrarSiCorresponde(win, reservaAux, idUsuario);

            app.abrirMenuUsuario(usuarioCorreo.trim(), idUsuario);
        }


    }
}