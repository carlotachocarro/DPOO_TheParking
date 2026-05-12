package Persistencia.persistenciaExcepciones;

public class ExcepcionGeneralDB extends ExcepcionPersistencia{
    private static String mensaje = "Hay un problema con la base de datos.";

    public ExcepcionGeneralDB(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}
