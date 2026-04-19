package Persistencia.Daoimpl;

import Negocio.Entidades.Usuario;
import Persistencia.SQL_CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDBDAO implements UsuarioDAO{
    public UsuarioDBDAO() {
        Singleton.getInstance().getConn();
    }

    public boolean registrarUsuario(String nombre, String eMail, String contraseña){
        if (checkUsuario(nombre, eMail)) return false;

        String query = "INSERT INTO usuario (nombre, mail, contraseña) VALUES (?,?,?)";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(eMail);
        values.add(contraseña);
        types.add("String");
        types.add("String");
        types.add("String");

        int result = SQL_CRUD.CUD(query, values, types);
        return result > 0;
    }

    public boolean eliminarUsuario(String nombre, String mail){
        //POR IMPLEMENTAR
        return false;
    }

    public boolean checkUsuario(String nombre, String mail){
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");

        ResultSet rs = SQL_CRUD.Select(query, values, types);
        try {
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Usuario getUsuario(String nombre, String mail){
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";
        Usuario user =null;
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");

        ResultSet rs = SQL_CRUD.Select(query, values, types);
        try{
            if (rs.next()){
                user = new Usuario(rs.getString("nombre"), rs.getString("mail"), rs.getString("contraseña"));
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    public String getUsuarioId(String nombre, String mail){
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        int userId = -1;

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");

        ResultSet rs = SQL_CRUD.Select(query, values, types);
        try{
            if (rs.next()){
                userId = rs.getInt("id_usuario");
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return ""+userId;
    }
}
