package Persistencia.persistenciaExcepciones;

public abstract class ExcepcionPersistencia extends Exception{
    private static String mensaje;

    public String getMensajeExcepcion(){return this.mensaje;}
}
