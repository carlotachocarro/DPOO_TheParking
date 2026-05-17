package Negocio.Excepciones;

/**
 * Excepción base de la capa de negocio. Toda excepción que cruza
 * el límite Negocio → Presentación debe heredar de esta clase.
 */
public abstract class ExcepcionNegocio extends Exception {

    protected ExcepcionNegocio(String mensaje) {
        super(mensaje);
    }

    protected ExcepcionNegocio(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public String getMensajeExcepcion() {
        return getMessage();
    }
}
