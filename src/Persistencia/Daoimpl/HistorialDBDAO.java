package Persistencia.Daoimpl;

import Persistencia.SQL_CRUD;

import java.sql.ResultSet;
import java.util.ArrayList;

public class HistorialDBDAO {

    public HistorialDBDAO() {
        Singleton.getInstance().getConn();
    }

    public boolean nuevoRegistro(){
        String query1 = "SELECT COUNT(*) FROM plaza_parking WHERE estado_actual = 1;";
        String query2 = "INSERT INTO historial (plazasOcupadas, fecha) VALUES (?, Now())";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ResultSet res = SQL_CRUD.Select(query1, values, types);
        int plazas = 0;
        try {
            if (res.next()){
                plazas = res.getInt(1);
            }
            values.add(plazas+"");
            types.add("int");
            int res2 = SQL_CRUD.CUD(query2, values, types);
            return res2 > 0;
        } catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public ArrayList<Integer> sacaHistorial(){
        String query = "SELECT * FROM historial WHERE fecha >= NOW() - INTERVAL 60 MINUTE ORDER BY fecha DESC LIMIT 60;";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        ArrayList<Integer> valores = new ArrayList<>();
        ResultSet res = SQL_CRUD.Select(query, values, types);
        while (true){
            try{
                if (!res.next()) break;
                valores.add(res.getInt("plazasOcupadas"));

            } catch (Exception e){
                System.out.println(e);
            }
        }
        return valores;
    }

}
