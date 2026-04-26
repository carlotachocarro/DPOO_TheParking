package Presentacion.Controladores;

import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.RegistroPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorRegistroUsuario implements ActionListener {

    private RegistroPanel vista;
    private ServicioUsuario servicioUsuario;

    public ControladorRegistroUsuario(RegistroPanel vista) {
        this.vista = vista;
        this.servicioUsuario = new ServicioUsuario();
        this.vista.addRegistroListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String nombre = vista.getNombreUsuario();
        String correo = vista.getCorreo();
        String password = vista.getPassword();
        String repetirPassword = vista.getConfirmarPassword();

        if (nombre.isEmpty() || correo.isEmpty()// miramos que no haya ningun campo vacio
                || password.isEmpty() || repetirPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debes rellenar todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int resultado = servicioUsuario.registrarUsua(nombre,correo,password,repetirPassword);

        switch (resultado) {
            case -1:
                JOptionPane.showMessageDialog(null, "El correo electronico no esta correcto ");
                vista.limpiarCampos();
                vista.limpiarEmail();
                break;
            case 0 :
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                vista.limpiarCampos();
                vista.focoEnPassword();
                break;
            case 1 :
                JOptionPane.showMessageDialog(null, "La contraseña no cumple la política MIT");
                vista.limpiarCampos();
                break;
            case 2 :
                JOptionPane.showMessageDialog(null, "No se pudo registrar el usuario");
                vista.limpiarCampos();
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Ya esxiste el usuario canvia el nombre");
                vista.limpiarCampos();

                break;
            case 4 :
                //JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
                ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
                Window ventana = SwingUtilities.getWindowAncestor(vista);
                ventana.dispose();
                new MainUsuarioFrame(nombre,controller).setVisible(true);

                break;
        }
    }



}
