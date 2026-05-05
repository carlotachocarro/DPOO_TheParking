package Presentacion.Controladores;

import javax.swing.SwingUtilities;

import Presentacion.Vistas.AuthFrame;
import Presentacion.Vistas.LoginPanel;
import Presentacion.Vistas.MainAdminFrame;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.RegistroPanel;

public class ControladorAplicacion {

    /**
     * Muestra login/registro con los controladores enlazados. Seguro desde el hilo principal o desde el EDT.
     */
    public static void reiniciarFlujoAutenticacion() {
        if (SwingUtilities.isEventDispatchThread()) {
            new ControladorAplicacion().iniciar();
        } else {
            SwingUtilities.invokeLater(() -> new ControladorAplicacion().iniciar());
        }
    }

    private AuthFrame authFrame;
    private LoginPanel loginPanel;
    private RegistroPanel registroPanel;

    public void iniciar() {
        authFrame = new AuthFrame();

        loginPanel = new LoginPanel();
        registroPanel = new RegistroPanel();

        authFrame.addPanel(loginPanel, AuthFrame.LOGIN);
        authFrame.addPanel(registroPanel, AuthFrame.REGISTRO);

        ControladorInicioSesion controladorInicioSesion = new ControladorInicioSesion(loginPanel, this);
        ControladorRegistroUsuario controladorRegistroUsuario = new ControladorRegistroUsuario(registroPanel, this);

        authFrame.mostrarLogin();
        authFrame.setVisible(true);
    }

    public void mostrarLogin() {
        authFrame.mostrarLogin();
    }

    public void mostrarRegistro() {
        authFrame.mostrarRegistro();
    }

    public void abrirMenuUsuario(String nombreUsuario) {
        authFrame.dispose();
        ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
        new MainUsuarioFrame(nombreUsuario, controller).setVisible(true);
    }

    public void abrirMenuAdmin() {
        authFrame.dispose();
        ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
        new MainAdminFrame(controller).setVisible(true);
    }
}