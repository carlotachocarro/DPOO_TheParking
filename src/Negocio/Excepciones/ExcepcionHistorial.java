package Negocio.Excepciones;

public class ExcepcionHistorial extends ExcepcionNegocio {
    private static String mensaje = "Problema con la base de datos cuando se gestiona el historial.";

    public ExcepcionHistorial(){
    }

    public String getMensajeExcepcion(){return this.mensaje;}
}