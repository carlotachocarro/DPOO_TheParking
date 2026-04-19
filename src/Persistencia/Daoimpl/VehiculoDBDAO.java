package Persistencia.Daoimpl;

import Persistencia.SQL_CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehiculoDBDAO {
    public VehiculoDBDAO() {
        Singleton.getInstance().getConn();
    }

    /**
     * Crea un nuevo vehiculo en la BBDD
     * @param nombreUsuario Nombre del usuario propietario
     * @param matricula Matricula del vehiculo
     * @param tipo Tipo de vehiculo
     * @return Devuelve true si ha funcionado
     */
    public boolean agregarVehiculo(String nombreUsuario, String matricula, String tipo){
        if (checkVehiculo(matricula) != null) return false;
        UsuarioDBDAO userDao = new UsuarioDBDAO();
        String userId = userDao.getUsuarioId(nombreUsuario, "null");
        String query = "INSERT INTO vehiculo (matricula, tipo, id_usuario) VALUES (?,?,?)";

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(matricula);
        values.add(tipo);
        values.add(userId);
        types.add("String");
        types.add("String");
        types.add("int");

        int result = SQL_CRUD.CUD(query, values, types);
        return result > 0;
    }

    public boolean eliminarVehiculo(String matricula){
        //POR IMPLEMENTAR
        return false;
    }

    /**
     * Comprueba si existe un vehiculo, si existe devuelve el nombre del usuario al que pertenece
     * @param matricula Matricula del vehiculo
     * @return Nombre del Usuario.
     */
    public String checkVehiculo(String matricula){
        String query = "SELECT * FROM usuario WHERE id_usuario = (SELECT id_usuario FROM vehiculo WHERE matricula = ? LIMIT 1)";

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(matricula);
        types.add("String");

        ResultSet rs = SQL_CRUD.Select(query, values, types);
        try {
            if (rs.next()){
                return rs.getString("nombre");
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
