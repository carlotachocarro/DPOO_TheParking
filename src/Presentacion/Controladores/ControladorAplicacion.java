package Presentacion.Controladores;

import javax.swing.SwingUtilities;

import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Vistas.AuthFrame;
import Presentacion.Vistas.LoginPanel;
import Presentacion.Vistas.MainAdminFrame;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.RegistroPanel;

public class ControladorAplicacion {

    /**
     * Muestra login/registro con los controladores enlazados. Seguro desde el hilo principal o desde el EDT.
     */
    public static void reiniciarFlujoAutenticacion() throws ExcepcionFicheroNoEncontrado {
        if (SwingUtilities.isEventDispatchThread()) {
            new ControladorAplicacion().iniciar();
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    new ControladorAplicacion().iniciar();
                } catch (ExcepcionFicheroNoEncontrado e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private AuthFrame authFrame;
    private LoginPanel loginPanel;
    private RegistroPanel registroPanel;

    public void iniciar() throws ExcepcionFicheroNoEncontrado {
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

    /**
     * @param nombreUsuario texto de login (nombre o correo), para reservas y vistas
     * @param idUsuario     id numérico en BD (tabla {@code usuario}), para FK en {@code plaza_parking}
     */
    public void abrirMenuUsuario(String nombreUsuario, String idUsuario) throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        authFrame.dispose();
        ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
        new MainUsuarioFrame(nombreUsuario, idUsuario, controller).setVisible(true);
    }

    public void abrirMenuAdmin() throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        authFrame.dispose();
        ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
        new MainAdminFrame(controller).setVisible(true);
    }
}