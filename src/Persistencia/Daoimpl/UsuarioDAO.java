package Persistencia.Daoimpl;

import Negocio.Entidades.Usuario;

public interface UsuarioDAO {
    public boolean registrarUsuario(String Nombre, String eMail, String contraseña);

    public boolean eliminarUsuario(String nombre, String mail);

    public boolean checkUsuario(String nombre, String mail);

    public Usuario getUsuario(String nombre, String mail);

    public Usuario getUsuarioById(String id);

    public String getUsuarioContraseña(String nombre, String mail);


    public String getUsuarioId(String nombre, String mail);



}
