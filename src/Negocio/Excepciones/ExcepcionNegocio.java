package Negocio.Excepciones;

public abstract class ExcepcionNegocio extends Exception {
    private static String mensaje;

    public String getMensajeExcepcion(){return this.mensaje;}
}