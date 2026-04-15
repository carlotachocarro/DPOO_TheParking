package Negocio.Entidades;

public class Vehiculo {

    private String idCliente;
    private String tipoVehiculo;
    private String matricula;

    public Vehiculo(String idCliente, String tipoVehiculo, String matricula) {

        this.tipoVehiculo = tipoVehiculo;
        this.matricula = matricula;

    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }
}
