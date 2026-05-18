package Persistencia.Daoimpl;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Usuario;
import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que permite interactuar con la tabla de Plaza_Parking de la BBDD
 */
public class PlazaDBDAO {
    /**
     * Constructor de la clase PlazaDBDAO
     * @throws ExcepcionFicheroNoEncontrado
     */
    public PlazaDBDAO() throws ExcepcionFicheroNoEncontrado {
        try{
            Singleton.getInstance().getConn();
        } catch (Exception e){
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    /**
     * Esta función crea una nueva plaza de parking en la BBDD.
     * @param tipoPlaza Tipo de plaza de parking.
     * @param planta Planta en la que se situa la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean crearPlaza(String tipoPlaza, String planta) throws ExcepcionGeneralDB {
        String query = "INSERT INTO plaza_parking (tipo_vehiculo, planta, estado_actual, estado_reserva, matricula, simulado, id_usuario) VALUES (?, ?, 0,0,'none',0, NULL)";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(tipoPlaza);
        values.add(planta);
        types.add("String");
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
     * Esta función elimina una plaza de parking y todos sus registros asociados.
     * @param idPlaza Identificador de la plaza a eliminar.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     */
    public boolean eliminarPlaza(String idPlaza) throws ExcepcionGeneralDB{
        String query1 = "DELETE FROM reserva WHERE id_plaza = ?";
        String query = "DELETE FROM plaza_parking WHERE id_plaza = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(idPlaza);
        types.add("int");
        try {
            int res = SQL_CRUD.CUD(query1, values, types);
            if( res > 0){
                int res2 = SQL_CRUD.CUD(query, values, types);
                return res > 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }


    /**
     * Esta función modifica el estado de una plaza de parking para indicar que está reservada.
     * @param idPlaza Identificador de la plaza.
     * @param idUsuario Identificador del usuario que ha reservado la plaza.
     * @param matricula Matricula del vehiculo que reserva la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     */
    public boolean aplicarReservaEnPlaza(String idPlaza, String idUsuario, String matricula) {
        String query = """
                UPDATE plaza_parking
                SET estado_reserva = 1, estado_actual = 0, matricula = ?, id_usuario = ?, simulado = 0
                WHERE id_plaza = ?
                """;
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(matricula);
        values.add(idUsuario);
        values.add(idPlaza);
        types.add("String");
        types.add("int");
        types.add("int");
        try {
            int res = SQL_CRUD.CUD(query, values, types);
            return res > 0;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }


    /**
     * Esta función permite modificar la planta y tipo de vehículo de una plaza.
     * @param idPlaza Identificador de la plaza.
     * @param tipoVehiculo Tipo de vehículo que soporta la plaza
     * @param planta Planta a la que cambiaremos la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     */
    public boolean actualizarDatosPlaza(String idPlaza, String tipoVehiculo, int planta) {
        String query = "UPDATE plaza_parking SET tipo_vehiculo = ?, planta = ? WHERE id_plaza = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(tipoVehiculo);
        values.add(String.valueOf(planta));
        values.add(idPlaza);
        types.add("String");
        types.add("int");
        types.add("int");
        try {
            int res = SQL_CRUD.CUD(query, values, types);
            return res > 0;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }


    /**
     * Esta función devuelve todas las plazas del parking.
     * @return Devuelve un ArrayList con todas las plazas.
     * @throws ExcepcionGeneralDB
     */
    public ArrayList<Plaza> getPlazas() throws ExcepcionGeneralDB {
        String query = "SELECT * FROM plaza_parking";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Plaza> plazas = new ArrayList<>();
        try {
            ResultSet res = SQL_CRUD.Select(query, values, types);
            while(true){
                if(!res.next()) break;
                String tipo_plaza = res.getString("tipo_vehiculo");
                int planta = res.getInt("planta");
                String id = res.getInt("id_plaza") + "";
                boolean ocu = false;
                boolean rese = false;
                boolean sim = false;
                if (res.getInt("estado_actual") == 1){
                    ocu = true;
                } else {
                    ocu = false;
                }
                if (res.getInt("estado_reserva") == 1){
                    rese = true;
                } else {
                    rese = false;
                }
                if (res.getInt("simulado") == 1){
                    sim = true;
                } else {
                    sim = false;
                }
                int userId = res.getInt("id_usuario");
                if (userId != 0){
                    UsuarioDBDAO u = new UsuarioDBDAO();
                    plazas.add(new Plaza(tipo_plaza,planta,id,ocu, rese, sim, u.getUsuarioById(userId+""), res.getString("matricula")));
                } else {
                    plazas.add(new Plaza(tipo_plaza,planta,id,ocu, rese, sim, null, "none"));
                }
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return plazas;
    }

    /**
     * Esta función devuelve todas las plazas libres de un tipo de vehículo en específico.
     * @param tipo Tipo de vehículo.
     * @return Devuelve un ArrayList con las plazas encontradas.
     * @throws ExcepcionGeneralDB
     */
    public ArrayList<Plaza> getPlazasLibres(String tipo) throws ExcepcionGeneralDB {
        String query = "SELECT * FROM plaza_parking WHERE estado_actual = 0 AND estado_reserva = 0 AND simulado = 0 AND tipo_vehiculo = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Plaza> plazas = new ArrayList<>();

        values.add(tipo);
        types.add("String");
        try {
            ResultSet res = SQL_CRUD.Select(query, values, types);
            while(true) {
                if (!res.next()) break;
                String tipo_plaza = res.getString("tipo_vehiculo");
                int planta = res.getInt("planta");
                String id = res.getInt("id_plaza") + "";
                boolean ocu = false;
                boolean rese = false;
                boolean sim = false;
                if (res.getInt("estado_actual") == 1) {
                    ocu = true;
                } else {
                    ocu = false;
                }
                if (res.getInt("estado_reserva") == 1) {
                    rese = true;
                } else {
                    rese = false;
                }
                if (res.getInt("simulado") == 1) {
                    sim = true;
                } else {
                    sim = false;
                }
                int userId = res.getInt("id_usuario");
                if (userId != 0) {
                    UsuarioDBDAO u = new UsuarioDBDAO();
                    plazas.add(new Plaza(tipo_plaza, planta, id, ocu, rese, sim, u.getUsuarioById(userId + ""), res.getString("matricula")));
                } else {
                    plazas.add(new Plaza(tipo_plaza, planta, id, ocu, rese, sim, null, "none"));
                }
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return plazas;
    }


    /**
     * Devuelve el usuario que ocupa una plaza de parking.
     * @param id Identificador de la plaza de parking.
     * @return Instancia del usuario que ocupa la plaza.
     * @throws ExcepcionGeneralDB
     */
    public Usuario getPlazaUsuario(String id) throws ExcepcionGeneralDB {
        String query = "SELECT * FROM plaza_parking WHERE id_plaza = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(id);
        types.add("int");
        try {
            ResultSet res = SQL_CRUD.Select(query, values, types);
            if (res.next()){
                int userId = res.getInt("id_usuario");
                if (userId != 0){
                    UsuarioDBDAO u = new UsuarioDBDAO();
                    return u.getUsuarioById(userId+"");
                }else {
                    return null;
                }
            }
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return null;
    }

    /**
     * Esta función ocupa una plaza de parking.
     * @param id_plaza Identificador de la plaza.
     * @param ocupacion Booleano que nos dice si se ocupa o desocupa.
     * @param matricula Matricula a poner en caso de ocupar.
     * @param nombreUsuario Nombre del usuario que ocupa.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     * @throws ExcepcionFicheroNoEncontrado
     */
    public boolean ocuparPlaza(String id_plaza ,boolean ocupacion, String matricula, String nombreUsuario) throws ExcepcionGeneralDB, ExcepcionFicheroNoEncontrado {
        String query = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        if (ocupacion){
            UsuarioDBDAO u = new UsuarioDBDAO();
            String idUser = u.getUsuarioId(nombreUsuario, "");
            query = "UPDATE plaza_parking SET estado_actual = 1,estado_reserva = 1,matricula = ?, id_usuario = ? WHERE id_plaza = ?";
            values.add(matricula);
            values.add(idUser);
            values.add(id_plaza);
            types.add("String");
            types.add("int");
            types.add("int");
        } else {
            query = "UPDATE plaza_parking SET estado_actual = 0, matricula = 'none', id_usuario = NULL WHERE id_plaza = ?";
            values.add(id_plaza);
            types.add("int");
        }
        try {
            int result = SQL_CRUD.CUD(query, values, types);
            return result > 0;
        } catch (Exception e){
            throw new ExcepcionGeneralDB();
        }
    }

    /**
     * Esta función ocupa una plaza de parking para la simulación.
     * @param id_plaza Identificador de la plaza.
     * @param ocupacion Estado de la ocupación.
     * @param matricula Matricula que ocupará la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean ocuparPlazaSimul(String id_plaza ,boolean ocupacion, String matricula) throws ExcepcionGeneralDB {
        String query = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        if (ocupacion){
            query = "UPDATE plaza_parking SET estado_actual = 1, matricula = ? , id_usuario = NULL WHERE id_plaza = ?";
            values.add(matricula);
            values.add(id_plaza);
            types.add("String");
            types.add("int");
        } else {
            query = "UPDATE plaza_parking SET estado_actual = 0, matricula = 'none', id_usuario = NULL WHERE id_plaza = ?";
            values.add(id_plaza);
            types.add("int");
        }
        try {
            int result = SQL_CRUD.CUD(query, values, types);
            return result > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }

    }

    /**
     * Esta función reserva una plaza.
     * @param id_plaza Identificador de la plaza.
     * @param reserva Boolean que define si reservamos o no.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean reservarPlaza(String id_plaza ,boolean reserva) throws ExcepcionGeneralDB{
        String query = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        if (reserva){
            query = "UPDATE plaza_parking SET estado_reserva = 1 WHERE id_plaza = ?";
        } else {
            query = "UPDATE plaza_parking SET estado_reserva = 0 WHERE id_plaza = ?";
        }
        values.add(id_plaza);
        types.add("int");
        try {
            int result = SQL_CRUD.CUD(query, values, types);
            return result > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }

    }

    /**
     * Esta función define que una plaza se usará para la simulación.
     * @param id_plaza Identificador de la plaza.
     * @param simul Boolean que define si se simula o deja de simular.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean simularPlaza(String id_plaza ,boolean simul) throws ExcepcionGeneralDB{
        String query = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        if (simul){
            query = "UPDATE plaza_parking SET simulado = 1 WHERE id_plaza = ?";
        } else {
            query = "UPDATE plaza_parking SET simulado = 0 WHERE id_plaza = ?";
        }
        values.add(id_plaza);
        types.add("int");
        try {
            int result = SQL_CRUD.CUD(query, values, types);
            return result > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();

        }

    }

    /**
     * Función que "limpia" el registro de una plaza que pasa a estar libre.
     * @param idPlaza Identificador de la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean limpiarPlaza(String idPlaza) throws ExcepcionGeneralDB{

        String query = """
        UPDATE plaza_parking
        SET
            estado_actual = 0,
            estado_reserva = 0,
            matricula = 'none',
            id_usuario = NULL,
            simulado = 0
        WHERE id_plaza = ?
        """;

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        values.add(idPlaza);
        types.add("int");
        try {
            int result = SQL_CRUD.CUD(query, values, types);
            return result > 0;
        } catch (Exception e){
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }


}
