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
        String q1 = "DELETE FROM reserva WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE nombre = ? OR mail=? LIMIT 1 )";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");
        int res1 = SQL_CRUD.CUD(q1, values, types);

        String q2 = "UPDATE plaza_parking SET estado_actual = 0, estado_reserva = 0, matricula = 'none' , id_usuario = NULL WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE nombre = ? OR mail=? LIMIT 1 )";
        ArrayList<String> values2 = new ArrayList<>();
        ArrayList<String> types2 = new ArrayList<>();
        values2.add(nombre);
        values2.add(mail);
        types2.add("String");
        types2.add("String");
        int res2 = SQL_CRUD.CUD(q2, values2, types2);

        String q3 = "DELETE FROM usuario WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE nombre = ? OR mail=? LIMIT 1 )";
        ArrayList<String> values3 = new ArrayList<>();
        ArrayList<String> types3 = new ArrayList<>();
        values3.add(nombre);
        values3.add(mail);
        types3.add("String");
        types3.add("String");
        int res3 = SQL_CRUD.CUD(q3, values, types);

        return res3 > 0;
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
            System.out.println(e);
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
                user = new Usuario(rs.getString("nombre"), rs.getString("mail"));
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    public Usuario getUsuarioById(String id){
        String query = "SELECT * FROM usuario WHERE id_usuario=?";
        Usuario user =null;
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(id);
        types.add("int");

        ResultSet rs = SQL_CRUD.Select(query, values, types);
        try{
            if (rs.next()){
                user = new Usuario(rs.getString("nombre"), rs.getString("mail"));
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    public String getUsuarioContraseña(String nombre, String mail){
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";
        String password = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");

        ResultSet rs = SQL_CRUD.Select(query, values, types);
        try{
            if (rs.next()){
                password = rs.getString("contraseña");
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return password;
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
