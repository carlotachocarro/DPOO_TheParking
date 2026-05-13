package Negocio.Excepciones;

public class ExcepcionDatosIncorrectos extends ExcepcionNegocio {
    private static String mensaje = "Los datos introducidos son incorrectos.";

    public ExcepcionDatosIncorrectos(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}