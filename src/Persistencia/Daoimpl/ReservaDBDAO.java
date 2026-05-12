package Persistencia.Daoimpl;

import Negocio.Entidades.Reserva;
import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReservaDBDAO {

    public ReservaDBDAO() throws ExcepcionFicheroNoEncontrado {
        try{
            Singleton.getInstance().getConn();
        } catch (Exception e){
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    public boolean nuevaReserva(String id_plaza, String id_usuario, String matricula) throws ExcepcionGeneralDB {
        String query = "INSERT INTO reserva(id_plaza, id_usuario, matricula, fecha) VALUES (?, ?, ?, Now())";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(id_plaza);
        values.add(id_usuario);
        values.add(matricula);
        types.add("int");
        types.add("int");
        types.add("String");

        try {
            int res = SQL_CRUD.CUD(query, values, types);
            return res > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    public ArrayList<Reserva> getReservas() throws ExcepcionGeneralDB {
        String query = "SELECT * FROM reserva";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        ArrayList<Reserva> reservas = new ArrayList<>();


        try {
            ResultSet res = SQL_CRUD.Select(query,values,types);
            while (true){
                if (!res.next()) break;
                Timestamp fecha = res.getTimestamp("fecha");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaFin = formato.format(fecha);
                reservas.add(new Reserva(res.getString("id_reserva"), res.getString("matricula"), res.getString("id_usuario"), res.getString("id_plaza"), fechaFin));
            }
        } catch(Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return reservas;
    }

    public ArrayList<Reserva> getReservaByUser(String id_usuario) throws ExcepcionGeneralDB{
        String query = "SELECT * FROM reserva WHERE id_usuario = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(id_usuario);
        types.add("int");

        ArrayList<Reserva> reservas = new ArrayList<>();


        try {
            ResultSet res = SQL_CRUD.Select(query,values,types);
            while (true){
                if (!res.next()) break;
                Timestamp fecha = res.getTimestamp("fecha");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaFin = formato.format(fecha);
                reservas.add(new Reserva(res.getString("id_reserva"), res.getString("matricula"), res.getString("id_usuario"), res.getString("id_plaza"), fechaFin));
            }
        } catch(Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return reservas;
    }

    /**
     * Borra todas las reservas de un usuario
     * @param id_usuario Id del usuario
     * @return
     */
    public boolean borrarReservasUser(String id_usuario) throws ExcepcionGeneralDB{
        String query = "DELETE FROM reserva WHERE id_usuario=?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(id_usuario);
        types.add("int");

        try {
            int res = SQL_CRUD.CUD(query, values, types);
            return res > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Borra todas las reservas de un usuario
     * @param id_usuario Id del usuario
     * @return
     */
    public boolean borrarReserva(String id_plaza, String id_usuario) throws ExcepcionGeneralDB{
        String query = "DELETE FROM reserva WHERE id_plaza=? AND id_usuario=?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(id_plaza);
        values.add(id_usuario);
        types.add("int");
        types.add("int");
        try {
            int res = SQL_CRUD.CUD(query, values, types);
            return res > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Reasigna todas las reservas apuntando a una plaza a otra (mismo registro, nuevo {@code id_plaza}).
     */
    public boolean actualizarIdPlazaReserva(String idPlazaAnterior, String idPlazaNuevo) throws ExcepcionGeneralDB {
        String query = "UPDATE reserva SET id_plaza = ? WHERE id_plaza = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(idPlazaNuevo);
        values.add(idPlazaAnterior);
        types.add("int");
        types.add("int");
        try {
            int res = SQL_CRUD.CUD(query, values, types);
            return res > 0;
        } catch (Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    public boolean eliminarReservasDePlaza(String idPlaza) throws ExcepcionGeneralDB {
        String query = "DELETE FROM reserva WHERE id_plaza = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(idPlaza);
        types.add("int");
        try {
            SQL_CRUD.CUD(query, values, types);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

}
