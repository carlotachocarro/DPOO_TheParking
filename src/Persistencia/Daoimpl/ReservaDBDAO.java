package Persistencia.Daoimpl;

import Negocio.Entidades.Reserva;
import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Clase que permite interactuar con la tabla de Reserva de la BBDD.
 */
public class ReservaDBDAO {

    /**
     * Constructor de la clase ReservaDBDAO.
     * @throws ExcepcionFicheroNoEncontrado
     */
    public ReservaDBDAO() throws ExcepcionFicheroNoEncontrado {
        try{
            Singleton.getInstance().getConn();
        } catch (Exception e){
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    /**
     * Este metodo genera una nueva reserva en la BBDD.
     * @param id_plaza Identificador de la plaza a reservar.
     * @param id_usuario Identificador del usuario que ha hecho la reserva.
     * @param matricula Matrícula del vehículo registrado en la reserva.
     * @return Devuelve un boolean en funcion de si se ha creado el registro con exito o no.
     * @throws ExcepcionGeneralDB
     */
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

    /**
     * Este metodo devuelve una lista con todas las reservas que hay en el sistema registrados.
     * @return Devuelve un ArrayList de reservas, con todas las reservas del sistema.
     * @throws ExcepcionGeneralDB
     */
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

    /**
     * Este metodo devuelve las reservas hechas por un usuario en específico.
     * @param id_usuario Identificador del usuario del cual estamos buscando sus reservas.
     * @return Devuelve un ArrayList de reservas con las reservas del usuario (solo las activas, el resto se eliminan).
     * @throws ExcepcionGeneralDB
     */
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
     * Borra todas las reservas de un usuario.
     * @param id_usuario Id del usuario
     * @return Devuelve un boolean en función de si se ha conseguido borrar los registros.
     * @throws ExcepcionGeneralDB
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
     * Borra una reserva en específico de un usuario.
     * @param id_plaza Identificador de la plaza de la reserva.
     * @param id_usuario Identificador del usuario de la reserva.
     * @return Devuelve un booleano en función de si la operación se ha completado con exito o no.
     * @throws ExcepcionGeneralDB
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
     * Cambia la plaza de una reserva.
     * @param idPlazaAnterior Identificador de la plaza original de la reserva.
     * @param idPlazaNuevo Identificador de la nueva plaza de la reserva.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
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

    /**
     * Elimina la reserva de una plaza de parking específica.
     * @param idPlaza Identificador de la plaza de parking.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
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
