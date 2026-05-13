package Negocio.Excepciones;

public class ExcepcionEntradaSalidaPlaza extends ExcepcionNegocio {
    private static String mensaje = "Problema con la base de datos para gestionar la Plaza.";

    public ExcepcionEntradaSalidaPlaza(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}