import Persistencia.Daoimpl.UsuarioDBDAO;

import javax.swing.SwingUtilities;
import Presentacion.Vistas.AuthFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            arrancarModoDesarrollo();
        });
    }

    private static void arrancarModoDesarrollo() {
        // Opción 1: probar login
        new AuthFrame().setVisible(true);

        // Opción 2: probar directamente la pantalla principal de usuario
        // Usuario usuarioPrueba = new Usuario("usuario1", "usuario1@email.com");
        // new MainUsuarioFrame(usuarioPrueba).setVisible(true);
    }
}