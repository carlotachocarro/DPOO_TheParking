package Negocio.Excepciones;

public class ExcepcionReservaPlaza extends ExcepcionNegocio {
    private static String mensaje = "No se ha podido reservar la plaza.";

    public ExcepcionReservaPlaza(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}