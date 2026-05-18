package Persistencia.Daoimpl;

import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Clase que permite interactuar con la tabla de Historial de la BBDD
 */
public class HistorialDBDAO {

    /**
     * Constructor de la clase HistorialDBDAO.
     * @throws ExcepcionFicheroNoEncontrado
     */
    public HistorialDBDAO() throws ExcepcionFicheroNoEncontrado {
        try{
            Singleton.getInstance().getConn();
        } catch (Exception e){
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    /**
     * Esta función genera un nuevo registro del historial.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean nuevoRegistro() throws ExcepcionGeneralDB {
        String query1 = "SELECT COUNT(*) FROM plaza_parking WHERE estado_actual = 1;";
        String query2 = "INSERT INTO historial (plazasOcupadas, fecha) VALUES (?, Now())";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        int plazas = 0;
        try {
            ResultSet res = SQL_CRUD.Select(query1, values, types);
            if (res.next()){
                plazas = res.getInt(1);
            }
            values.add(plazas+"");
            types.add("int");
            int res2 = SQL_CRUD.CUD(query2, values, types);
            return res2 > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Esta función devuelve los registros de historial de los últimos 60 minutos.
     * @return Devuelve un ArrayList de integer con la cantidad de plazas ocupadas.
     * @throws ExcepcionGeneralDB
     */
    public ArrayList<Integer> sacaHistorial() throws ExcepcionGeneralDB {
        String query = "SELECT * FROM historial WHERE fecha >= NOW() - INTERVAL 60 MINUTE ORDER BY fecha DESC LIMIT 60;";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        ArrayList<Integer> valores = new ArrayList<>();
        try{
            ResultSet res = SQL_CRUD.Select(query, values, types);
            while (true){
                    if (!res.next()) break;
                    valores.add(res.getInt("plazasOcupadas"));
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return valores;
    }

}
