package Presentacion.Controladores;

import javax.swing.*;

import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Servicios.ServicioUsuario;
import Presentacion.Vistas.AuthFrame;
import Presentacion.Vistas.LoginPanel;
import Presentacion.Vistas.MainAdminFrame;
import Presentacion.Vistas.MainUsuarioFrame;
import Presentacion.Vistas.RegistroPanel;

public class ControladorAplicacion {

    /**
     * Muestra login/registro con los controladores enlazados. Seguro desde el hilo principal o desde el EDT.
     */
    public static void reiniciarFlujoAutenticacion() throws ExcepcionFicheroConfig {
        Runnable tarea = () -> {
            try {
                new ControladorAplicacion().iniciar();
            } catch (ExcepcionFicheroConfig e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMensajeExcepcion(),
                        "Error de configuración",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            tarea.run();
        } else {
            SwingUtilities.invokeLater(tarea);
        }
    }

    private AuthFrame authFrame;
    private LoginPanel loginPanel;
    private RegistroPanel registroPanel;
    private ServicioUsuario servicioUsuario;

    public void iniciar() throws ExcepcionFicheroConfig {
        this.servicioUsuario = new ServicioUsuario();

        authFrame = new AuthFrame();
        loginPanel = new LoginPanel();
        registroPanel = new RegistroPanel();

        authFrame.addPanel(loginPanel, AuthFrame.LOGIN);
        authFrame.addPanel(registroPanel, AuthFrame.REGISTRO);

        new ControladorInicioSesion(loginPanel, this, servicioUsuario);
        new ControladorRegistroUsuario(registroPanel, this, servicioUsuario);

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
    public void abrirMenuUsuario(String nombreUsuario, String idUsuario) throws ExcepcionFicheroConfig {
        authFrame.dispose();
        ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
        new MainUsuarioFrame(nombreUsuario, idUsuario, controller).setVisible(true);
    }

    public void abrirMenuAdmin() throws ExcepcionFicheroConfig {
        authFrame.dispose();
        ControllerMenuPrincipalAdmin controller = new ControllerMenuPrincipalAdmin();
        new MainAdminFrame(controller).setVisible(true);
    }
}
