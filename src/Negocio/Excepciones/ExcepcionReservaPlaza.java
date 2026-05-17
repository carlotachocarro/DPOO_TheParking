package Negocio.Excepciones;

public class ExcepcionReservaPlaza extends ExcepcionNegocio {

    private static final String MENSAJE = "No se ha podido completar la operación sobre la reserva.";

    public ExcepcionReservaPlaza() {
        super(MENSAJE);
    }

    public ExcepcionReservaPlaza(Throwable causa) {
        super(MENSAJE, causa);
    }
}
