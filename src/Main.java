import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Controladores.ControladorAplicacion;

/**
 * Punto de entrada: solo arranca el controlador de aplicación (login / registro / menús).
 * <p>
 * Pendientes funcionales opcionales del enunciado: panel admin de gestión de reservas en bloque,
 * notificación persistente tras cancelación admin (ya hay flujo desde detalle de plaza), etc.
 */
public class Main {

    public static void main(String[] args) throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        ControladorAplicacion.reiniciarFlujoAutenticacion();
    }
}
