package Persistencia;



import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import Persistencia.Daoimpl.Singleton;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

/**
 * Esta clase le permite a los devs tener un set de instrucciones para hacer peticiones CRUD contra la BBDD.
 */
public class SQL_CRUD {
    /**
     * Constructor de la clase.
     */
    public SQL_CRUD() {}

    /**
     * Esta función permite hacer preguntas a la BBDD del tipo Select.
     * @param query String con la query a ejecutar.
     * @param values ArrayList con los Strings de los valores a poner en la query.
     * @param tipos ArrayList con los tipode de dato de los valores.
     * @return Devuelve un objeto ResultSet que nos permite sacar la información de la query.
     */
    public static ResultSet Select(String query, ArrayList<String> values, ArrayList<String> tipos) throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        PreparedStatement pst;
        Singleton s1 = Singleton.getInstance();
        ResultSet res;
        try {
            pst = s1.getConn().prepareStatement(query);
            for (int i = 0; i < values.size(); i++) {
                switch (tipos.get(i)) {
                    case "String":
                        pst.setString(i + 1, values.get(i));
                        break;
                    case "int":
                        pst.setInt(i + 1, Integer.parseInt(values.get(i)));
                        break;
                    case "float":
                    case "double": // <- añadido
                        pst.setDouble(i + 1, Double.parseDouble(values.get(i)));
                        break;
                    case "tinyint":
                        pst.setInt(i + 1, values.get(i).equals("true") ? 1 : 0);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo no soportado en SELECT: " + tipos.get(i));
                }
            }
            res = pst.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * Esta función permite hacer peticiones del tipo Create, Update or Delete a la BBDD.
     * @param query String con la query a ejecutar.
     * @param values ArrayList con los Strings de los valores a poner en la query.
     * @param tipos ArrayList con los tipode de dato de los valores.
     * @param isInsert Booleano que define si la petición es un insert o no.
     * @return Devuelve un objeto PreparedStatement.
     */
    private static PreparedStatement CUDpreparedStament(String query, ArrayList<String> values, ArrayList<String> tipos, boolean isInsert) throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        PreparedStatement pst;
        Singleton s1 = Singleton.getInstance();
        try {
            if (isInsert) {
                pst = s1.getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                pst = s1.getConn().prepareStatement(query);
            }

            for (int i = 0; i < values.size(); i++) {
                switch (tipos.get(i)) {
                    case "String":
                        pst.setString(i + 1, values.get(i));
                        break;
                    case "int":
                        pst.setInt(i + 1, Integer.parseInt(values.get(i)));
                        break;
                    case "float":
                    case "double": // <- añadido
                        pst.setDouble(i + 1, Double.parseDouble(values.get(i)));
                        break;
                    case "tinyint":
                        pst.setInt(i + 1, values.get(i).equals("true") ? 1 : 0);
                        break;
                    case "datetime":
                        pst.setTimestamp(i + 1, Timestamp.valueOf(LocalDateTime.now()));
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo no soportado en CUD: " + tipos.get(i));
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionGeneralDB();
        }
        return pst;
    }

    /**
     * Esta función permite hacer peticiones del tipo Create, Update or Delete a la BBDD usando la función CUDpreparedStament.
     * @param query String con la query a ejecutar.
     * @param values ArrayList con los Strings de los valores a poner en la query.
     * @param tipos ArrayList con los tipode de dato de los valores.
     * @return Devuelve un número con el estado de la petición.
     */
    public static int CUD(String query, ArrayList<String> values, ArrayList<String> tipos) throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        int res = 0;
        try {
            PreparedStatement pst = CUDpreparedStament(query, values, tipos, false);
            res = pst.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ExcepcionGeneralDB();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return res;
    }


}
