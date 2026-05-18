package Negocio.Excepciones;

public class ExcepcionCrearPlaza extends ExcepcionNegocio {

    private static final String MENSAJE = "No se ha podido crear la plaza.";

    public ExcepcionCrearPlaza() {
        super(MENSAJE);
    }

    public ExcepcionCrearPlaza(Throwable causa) {
        super(MENSAJE, causa);
    }
}
