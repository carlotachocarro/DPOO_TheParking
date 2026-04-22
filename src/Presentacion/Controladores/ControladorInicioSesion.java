package Presentacion.Controladores;

import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.LoginPanel;
import Presentacion.Vistas.MainUsuarioFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorInicioSesion implements ActionListener {


    private LoginPanel vista;
    private ServicioUsuario servicioUsuario;

    public ControladorInicioSesion(LoginPanel vista) {
        this.vista = vista;
        this.servicioUsuario = new ServicioUsuario();
        this.vista.addLoginListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String usuarioCorreo = vista.getUsuarioCorreo();
        String password = vista.getPassword();

        if (usuarioCorreo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debes rellenar usuario/correo y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean loginCorrecto = servicioUsuario.inicioSession(usuarioCorreo,password);
        if (!loginCorrecto) {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Window ventana = SwingUtilities.getWindowAncestor(vista);
        ventana.dispose();
        new MainUsuarioFrame(usuarioCorreo).setVisible(true);

    }
}
