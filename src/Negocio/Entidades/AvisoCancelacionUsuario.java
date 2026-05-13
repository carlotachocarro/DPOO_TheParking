package Negocio.Entidades;

/**
 * Datos para el aviso al usuario cuando el admin cancela (y opcionalmente reasigna) una reserva.
 */
public record AvisoCancelacionUsuario(
        String codigoPlazaCancelada,
        String plantaCancelada,
        String matricula,
        String tipoVehiculo,
        String codigoPlazaNueva,
        String plantaNueva
) {
    public boolean tieneReasignacion() {
        return codigoPlazaNueva != null && !codigoPlazaNueva.isBlank();
    }
}
