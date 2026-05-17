package Negocio.Excepciones;

public class ExcepcionFicheroConfig extends ExcepcionNegocio {

    private static final String MENSAJE = "No se ha encontrado el fichero de configuración.";

    public ExcepcionFicheroConfig() {
        super(MENSAJE);
    }

    public ExcepcionFicheroConfig(Throwable causa) {
        super(MENSAJE, causa);
    }
}
