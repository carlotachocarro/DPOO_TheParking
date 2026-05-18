package Persistencia.persistenciaExcepciones;

/**
 * Excepción de persistencia del tipo General DB.
 */
public class ExcepcionGeneralDB extends ExcepcionPersistencia{
    /**
     * Atributo con el mensaje a mostrar.
     */
    private static String mensaje = "Hay un problema con la base de datos.";
    /**
     * Constructor de la Clase.
     */
    public ExcepcionGeneralDB(){
    }
    /**
     * Getter del mensaje de la excepción
     * @return Mensaje de la Excepción.
     */
    public String getMensajeExcepcion(){return this.mensaje;}
}
