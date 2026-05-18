package Persistencia.persistenciaExcepciones;

/**
 * Excepción padre para las excepciones de Persistencia
 */
public abstract class ExcepcionPersistencia extends Exception{
    /**
     * Atributo con el mensaje a mostrar.
     */
    private static String mensaje;

    /**
     * Getter del mensaje de la excepción
     * @return Mensaje de la Excepción.
     */
    public String getMensajeExcepcion(){return this.mensaje;}
}
