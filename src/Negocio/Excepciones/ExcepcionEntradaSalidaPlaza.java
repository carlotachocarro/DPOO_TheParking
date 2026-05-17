package Negocio.Excepciones;

public class ExcepcionEntradaSalidaPlaza extends ExcepcionNegocio {

    private static final String MENSAJE = "Problema al gestionar la plaza.";

    public ExcepcionEntradaSalidaPlaza() {
        super(MENSAJE);
    }

    public ExcepcionEntradaSalidaPlaza(Throwable causa) {
        super(MENSAJE, causa);
    }
}
