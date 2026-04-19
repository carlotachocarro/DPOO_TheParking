package Negocio.Entidades;

public class Usuario {
    private String nombre;
    private String correoElectronico;
    private String contraseña;

    public Usuario( String nombre, String correoElectronico, String contrasenia) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contraseña = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }


    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getContraseña() {
        return contraseña;
    }



}

