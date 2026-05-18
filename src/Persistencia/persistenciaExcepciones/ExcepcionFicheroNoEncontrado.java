package Persistencia.persistenciaExcepciones;

/**
 * Excepción de persistencia del tipo Fichero no Encontrado.
 */
public class ExcepcionFicheroNoEncontrado extends ExcepcionPersistencia{
    /**
     * Atributo con el mensaje a mostrar.
     */
    private static String mensaje = "El fichero no existe.";

    /**
     * Constructor de la Clase.
     */
    public ExcepcionFicheroNoEncontrado(){
    }

    /**
     * Getter del mensaje de la excepción
     * @return Mensaje de la Excepción.
     */
    public String getMensajeExcepcion(){
        return this.mensaje;
    }
}
