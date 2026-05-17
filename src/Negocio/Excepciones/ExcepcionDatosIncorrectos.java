package Negocio.Excepciones;

public class ExcepcionDatosIncorrectos extends ExcepcionNegocio {

    private static final String MENSAJE = "Los datos introducidos son incorrectos.";

    public ExcepcionDatosIncorrectos() {
        super(MENSAJE);
    }

    public ExcepcionDatosIncorrectos(Throwable causa) {
        super(MENSAJE, causa);
    }
}
