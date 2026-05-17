package Presentacion.Controladores;

import javax.swing.*;

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
    public static void reiniciarFlujoAutenticacion() throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB{
        Runnable tarea = () -> {
            try {
                new ControladorAplicacion().iniciar();

            }  catch (ExcepcionFicheroNoEncontrado e) {
                JOptionPane.showMessageDialog(
                        null,
                        "No se ha encontrado el fichero de configuración.",
                        "Error de configuración",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (ExcepcionGeneralDB e) {
                JOptionPane.showMessageDialog(
                        null,
                        "No hay conexión con la base de datos.",
                        "Error de base de datos",
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

    public void iniciar() throws ExcepcionFicheroNoEncontrado ,ExcepcionGeneralDB{
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