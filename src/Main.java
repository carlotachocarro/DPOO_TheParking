import javax.swing.SwingUtilities;
import Presentacion.Controladores.ControladorAplicacion;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            arrancarAplicacion();
        });
    }

    private static void arrancarAplicacion() {
        ControladorAplicacion app = new ControladorAplicacion();
        app.iniciar();
    }
}