package Negocio.Excepciones;

public class ExcepcionHistorial extends ExcepcionNegocio {

    private static final String MENSAJE = "Problema al gestionar el historial de ocupación.";

    public ExcepcionHistorial() {
        super(MENSAJE);
    }

    public ExcepcionHistorial(Throwable causa) {
        super(MENSAJE, causa);
    }
}
