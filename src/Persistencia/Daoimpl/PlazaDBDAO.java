package Persistencia.Daoimpl;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Usuario;
import Persistencia.SQL_CRUD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlazaDBDAO {
    public PlazaDBDAO() {
        Singleton.getInstance().getConn();
    }

    public boolean crearPlaza(String tipoPlaza, String planta){
        String query = "INSERT INTO plaza_parking (tipo_vehiculo, planta, estado_actual, estado_reserva, matricula, simulado) VALUES (?, ?, 0,0,'none',0)";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(tipoPlaza);
        values.add(planta);
        types.add("String");
        types.add("int");

        int res = SQL_CRUD.CUD(query, values, types);
        return res > 0;
    }

    public boolean eliminarPlaza(String id){
        //POR IMPLEMENTAR
        return false;
    }

    public ArrayList<Plaza> getPlazas(){
        String query = "SELECT * FROM plaza_parking";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Plaza> plazas = new ArrayList<>();

        ResultSet res = SQL_CRUD.Select(query, values, types);
        while(true){
            try {
                if(!res.next()) break;
                String tipo_plaza = res.getString("tipo_vehiculo");
                int planta = res.getInt("planta");
                String id = res.getInt("id_plaza") + "";
                plazas.add(new Plaza(tipo_plaza,planta,id));
            } catch (Exception e){
                System.out.println(e);
            }

        }
        return plazas;
    }

    public Usuario getPlazaUsuario(String id){
        String query = "SELECT * FROM usuario WHERE id_usuario = ( SELECT id_usuario FROM vehiculo WHERE matricula = (SELECT matricula FROM plaza_parking WHERE id_plaza = ? LIMIT 1) LIMIT 1)";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(id);
        types.add("int");
        try {
            ResultSet res = SQL_CRUD.Select(query, values, types);
            if (res.next()){
                Usuario user = new Usuario(res.getString("nombre"), res.getString("mail"), res.getString("contraseña"));
                return user;
            } else {
                return null;
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
