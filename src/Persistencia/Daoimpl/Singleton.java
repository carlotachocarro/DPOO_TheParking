package Persistencia.Daoimpl;
import Persistencia.Config.ConfigJSONDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.*;

/**
 * Esta clase nos permite crear una única conexión con la BBDD.
 */
public final class Singleton {
    /**
     * Instancia única de la clase.
     */
    private static volatile Singleton instance;
    /**
     * Conexión con la BBDD.
     */
    private Connection conn;

    /**
     * Constructor de la clase.
     */
    private Singleton() throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB {
        ConfigJSONDAO config = new ConfigJSONDAO();
        String url = "jdbc:mysql://" + config.getDBIp() + ":" + config.getDBPuerto() + "/" + config.getDBNombre();
        String driver = "com.mysql.cj.jdbc.Driver";
        String usuario = config.getDBHost();
        String password = config.getDBContraseña();
        try{
            Class.forName(driver);
            this.conn = DriverManager.getConnection(url, usuario, password);
        }
        // ✅ Por esto:
        catch(ClassNotFoundException | SQLException e){
            System.out.println("❌ Error de conexión: " + e.getMessage());
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Función usada para sacar la instancia del Singleton.
     * @return Devuelve una instancia del Singleton.
     */
    public static Singleton getInstance() throws ExcepcionFicheroNoEncontrado, ExcepcionGeneralDB{

        Singleton result = instance;
        if (result != null) {
            return result;
        }
        synchronized(Singleton.class) {
            if (instance == null) {
                instance = new Singleton();
            }
            return instance;
        }
    }

    /**
     * Getter para la conexión a la BBDD.
     * @return Devuelve una instancia de la conexión a la BBDD.
     */
    public Connection getConn(){

        return this.conn;
    }

}