package Negocio.Entidades;

public class Usuario {
    private String nombre;
    private String correoElectronico;

    public Usuario( String nombre, String correoElectronico) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
    }

    public String getNombre() {
        return nombre;
    }


    public String getCorreoElectronico() {
        return correoElectronico;
    }




}

