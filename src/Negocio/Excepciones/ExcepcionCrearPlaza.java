package Negocio.Excepciones;

public class ExcepcionCrearPlaza extends ExcepcionNegocio {
    private static String mensaje = "No se ha podido crear la plaza.";

    public ExcepcionCrearPlaza(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}