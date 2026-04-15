package Negocio.Entidades;

public class Usuario {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String contrasenia;

    public Usuario(int idCliente, String nombre, String apellido, String correoElectronico, String contrasenia) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getContrasenia() {
        return contrasenia;
    }


    public int getIdCliente() {
        return idCliente;
    }

}

