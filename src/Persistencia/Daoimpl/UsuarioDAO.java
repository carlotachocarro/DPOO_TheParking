package Persistencia.Daoimpl;

public interface UsuarioDAO {
    public boolean registrarUsuario(String Nombre, String eMail, String contraseña);

    public boolean eliminarUsuario(String nombre, String mail);

    public boolean checkUsuario(String nombre, String mail);





}
