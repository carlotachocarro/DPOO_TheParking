import Negocio.Excepciones.ExcepcionFicheroConfig;
import Presentacion.Controladores.ControladorAplicacion;

/**
 * Punto de entrada: solo arranca el controlador de aplicación (login / registro / menús).
 */
public class Main {

    public static void main(String[] args) throws ExcepcionFicheroConfig {
        ControladorAplicacion.reiniciarFlujoAutenticacion();
    }
}
