package Persistencia.Daoimpl;

import Negocio.Entidades.Usuario;
import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que permite interactuar con la tabla de Usuario de la BBDD
 */
public class UsuarioDBDAO implements UsuarioDAO{
    /**
     * Constructor de la clase UsuarioDBDAO
     * @throws ExcepcionFicheroNoEncontrado
     */
    public UsuarioDBDAO() throws ExcepcionFicheroNoEncontrado {
        try{
            Singleton.getInstance().getConn();
        } catch (Exception e){
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    /**
     * Esta función permite crear un nuevo usuario en la BBDD.
     * @param nombre Nombre del usuario.
     * @param eMail Email del usuario.
     * @param contraseña Contraseña del usuario. (Encriptada)
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean registrarUsuario(String nombre, String eMail, String contraseña) throws ExcepcionGeneralDB {
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
        try {
            int result = SQL_CRUD.CUD(query, values, types);
            return result > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Esta función elimina un usuario de la BBDD.
     * @param nombre Nombre del usuario a eliminar.
     * @param mail Mail del usuario a eliminar.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean eliminarUsuario(String nombre, String mail) throws ExcepcionGeneralDB{
        String q1 = "DELETE FROM reserva WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE nombre = ? OR mail=? LIMIT 1 )";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");
        try {
            int res1 = SQL_CRUD.CUD(q1, values, types);
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }

        String q2 = "UPDATE plaza_parking SET estado_actual = 0, estado_reserva = 0, matricula = 'none' , id_usuario = NULL WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE nombre = ? OR mail=? LIMIT 1 )";
        ArrayList<String> values2 = new ArrayList<>();
        ArrayList<String> types2 = new ArrayList<>();
        values2.add(nombre);
        values2.add(mail);
        types2.add("String");
        types2.add("String");
        try {
            int res2 = SQL_CRUD.CUD(q2, values2, types2);
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }

        String q3 = "DELETE FROM usuario WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE nombre = ? OR mail=? LIMIT 1 )";
        ArrayList<String> values3 = new ArrayList<>();
        ArrayList<String> types3 = new ArrayList<>();
        values3.add(nombre);
        values3.add(mail);
        types3.add("String");
        types3.add("String");
        try {
            int res3= SQL_CRUD.CUD(q3, values3, types3);
            return res3 > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Comprueba si un usuario existe.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean checkUsuario(String nombre, String mail) throws ExcepcionGeneralDB{
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");
        try {
            ResultSet rs = SQL_CRUD.Select(query, values, types);
            return rs.next();
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Este metodo devuelve los datos de un usuario en específico.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return Devuelve una instancia de Usuario con los datos del usuario.
     * @throws ExcepcionGeneralDB
     */
    public Usuario getUsuario(String nombre, String mail) throws ExcepcionGeneralDB{
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";
        Usuario user =null;
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");

        try{
            ResultSet rs = SQL_CRUD.Select(query, values, types);
            if (rs.next()){
                user = new Usuario(rs.getString("nombre"), rs.getString("mail"));
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return user;
    }

    /**
     * Este metodo devuelve los datos de un usuario en específico.
     * @param id Identificador del usuario.
     * @return Devuelve una instancia de Usuario con los datos del usuario.
     * @throws ExcepcionGeneralDB
     */
    public Usuario getUsuarioById(String id) throws ExcepcionGeneralDB{
        String query = "SELECT * FROM usuario WHERE id_usuario=?";
        Usuario user =null;
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(id);
        types.add("int");

        try{
            ResultSet rs = SQL_CRUD.Select(query, values, types);
            if (rs.next()){
                user = new Usuario(rs.getString("nombre"), rs.getString("mail"));
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return user;
    }

    /**
     * Devuelve la contraseña (encriptada) de un usuario en específico.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return Devuelve un String con la contraseña.
     * @throws ExcepcionGeneralDB
     */
    public String getUsuarioContraseña(String nombre, String mail) throws ExcepcionGeneralDB{
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";
        String password = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(nombre);
        values.add(mail);
        types.add("String");
        types.add("String");

        try{
            ResultSet rs = SQL_CRUD.Select(query, values, types);
            if (rs.next()){
                password = rs.getString("contraseña");
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return password;
    }

    /**
     * Esta función devuelve el identificador de un usuario de la BBDD.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return String con el identificador del usuario.
     * @throws ExcepcionGeneralDB
     */
    public String getUsuarioId(String nombre, String mail) throws ExcepcionGeneralDB{
        String query = "SELECT * FROM usuario WHERE nombre = ? OR mail = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        int userId = -1;

        String n = nombre != null ? nombre.trim() : null;
        String m = mail != null ? mail.trim() : null;
        if ((n != null && n.isEmpty())) n = null;
        if ((m != null && m.isEmpty())) m = null;
        if (n == null && m == null) {
            return "-1";
        }

        values.add(n);
        values.add(m);
        types.add("String");
        types.add("String");

        try{
            ResultSet rs = SQL_CRUD.Select(query, values, types);
            if (rs.next()){
                userId = rs.getInt("id_usuario");
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return ""+userId;
    }
}
