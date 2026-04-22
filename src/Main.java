import Persistencia.Daoimpl.UsuarioDBDAO;

import javax.swing.SwingUtilities;

import Presentacion.Controladores.ControladorInicioSesion;
import Presentacion.Controladores.ControladorRegistroUsuario;
import Presentacion.Vistas.AuthFrame;
import Presentacion.Vistas.LoginPanel;
import Presentacion.Vistas.RegistroPanel;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            arrancarModoDesarrollo();
        });
    }

    private static void arrancarModoDesarrollo() {
        // Opción 1: probar login
        AuthFrame f =  new AuthFrame();
        f.setVisible(true);


        // Opción 2: probar directamente la pantalla principal de usuario
        // Usuario usuarioPrueba = new Usuario("usuario1", "usuario1@email.com");
        // new MainUsuarioFrame(usuarioPrueba).setVisible(true);
    }
}