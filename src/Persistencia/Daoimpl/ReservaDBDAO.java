package Persistencia.Daoimpl;

import Negocio.Entidades.Reserva;
import Persistencia.SQL_CRUD;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReservaDBDAO {

    public ReservaDBDAO(){
        Singleton.getInstance().getConn();
    }

    public boolean nuevaReserva(String id_plaza, String id_usuario, String matricula){
        String query = "INSERT INTO reserva(id_plaza, id_usuario, matricula, fecha) VALUES (?, ?, ?, Now())";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(id_plaza);
        values.add(id_usuario);
        values.add(matricula);
        types.add("int");
        types.add("int");
        types.add("String");

        int res = SQL_CRUD.CUD(query, values, types);

        return res > 0;
    }

    public ArrayList<Reserva> getReservas(){
        String query = "SELECT * FROM reserva WHERE ";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        ArrayList<Reserva> reservas = new ArrayList<>();

        ResultSet res = SQL_CRUD.Select(query,values,types);

        try {
            while (true){
                if (!res.next()) break;
                Timestamp fecha = res.getTimestamp("fecha");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaFin = formato.format(fecha);
                reservas.add(new Reserva(res.getString("id_reserva"), res.getString("matricula"), res.getString("id_usuario"), res.getString("id_plaza"), fechaFin));
            }
        } catch(Exception e) {
            System.out.println(e);
            return null;
        }
        return reservas;
    }

    public ArrayList<Reserva> getReservaByUser(String id_usuario){
        String query = "SELECT * FROM reserva WHERE id_usuario = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(id_usuario);
        types.add("int");

        ArrayList<Reserva> reservas = new ArrayList<>();

        ResultSet res = SQL_CRUD.Select(query,values,types);

        try {
            while (true){
                if (!res.next()) break;
                Timestamp fecha = res.getTimestamp("fecha");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaFin = formato.format(fecha);
                reservas.add(new Reserva(res.getString("id_reserva"), res.getString("matricula"), res.getString("id_usuario"), res.getString("id_plaza"), fechaFin));
            }
        } catch(Exception e) {
            System.out.println(e);
            return null;
        }
        return reservas;
    }

    /**
     * Borra todas las reservas de un usuario
     * @param id_usuario Id del usuario
     * @return
     */
    public boolean borrarReservasUser(String id_usuario){
        String query = "DELETE FROM reserva WHERE id_usuario=?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(id_usuario);
        types.add("int");

        int res = SQL_CRUD.CUD(query, values, types);
        return res > 0;
    }

    /**
     * Borra todas las reservas de un usuario
     * @param id_usuario Id del usuario
     * @return
     */
    public boolean borrarReserva(String id_plaza, String id_usuario){
        String query = "DELETE FROM reserva WHERE id_plaza=? AND id_usuario=?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(id_plaza);
        values.add(id_usuario);
        types.add("int");
        types.add("int");

        int res = SQL_CRUD.CUD(query, values, types);
        return res > 0;
    }


}
