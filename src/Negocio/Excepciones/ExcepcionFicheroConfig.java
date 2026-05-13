package Negocio.Excepciones;

public class ExcepcionFicheroConfig extends ExcepcionNegocio {
    private static String mensaje = "El fichero no existe.";

    public ExcepcionFicheroConfig(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}