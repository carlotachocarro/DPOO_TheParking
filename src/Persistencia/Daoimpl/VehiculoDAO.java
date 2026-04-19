package Persistencia.Daoimpl;

public interface VehiculoDAO {
    public boolean agregarVehiculo(String nombreUsuario, String matricula, String tipo);

    public boolean eliminarVehiculo(String matricula);

    /**
     * Comprueba si existe un vehiculo, si existe devuelve el nombre del usuario al que pertenece
     * @param matricula Matricula del vehiculo
     * @return Nombre del Usuario.
     */
    public String checkVehiculo(String matricula);
}
