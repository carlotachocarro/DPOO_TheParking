package Persistencia.persistenciaExcepciones;

public class ExcepcionFicheroNoEncontrado extends ExcepcionPersistencia{
    private static String mensaje = "El fichero no existe.";

    public ExcepcionFicheroNoEncontrado(){
    }

    public String getMensajeExcepcion(){
        return this.mensaje;
    }
}
