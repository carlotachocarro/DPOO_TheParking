package Persistencia.Daoimpl;

import Negocio.Entidades.Usuario;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

public interface UsuarioDAO {
    public boolean registrarUsuario(String Nombre, String eMail, String contraseña) throws ExcepcionGeneralDB;

    public boolean eliminarUsuario(String nombre, String mail) throws ExcepcionGeneralDB;

    public boolean checkUsuario(String nombre, String mail) throws ExcepcionGeneralDB;

    public Usuario getUsuario(String nombre, String mail) throws ExcepcionGeneralDB;

    public Usuario getUsuarioById(String id) throws ExcepcionGeneralDB;

    public String getUsuarioContraseña(String nombre, String mail) throws ExcepcionGeneralDB;


    public String getUsuarioId(String nombre, String mail) throws ExcepcionGeneralDB;



}
