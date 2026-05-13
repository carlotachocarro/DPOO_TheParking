package Persistencia.Daoimpl;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Usuario;
import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlazaDBDAO {
    public PlazaDBDAO() throws ExcepcionFicheroNoEncontrado {
        try{
            Singleton.getInstance().getConn();
        } catch (Exception e){
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

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
     * Elimina la fila de plaza. Las reservas asociadas deben haberse movido o borrado antes si hay FK.
     */
    public boolean eliminarPlazaFisica(String idPlaza) {
        String query = "DELETE FROM plaza_parking WHERE id_plaza = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(idPlaza);
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
     * Deja la plaza en estado reservada sin ocupación (vehículo aún no aparca), con usuario y matrícula de la reserva.
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
     * Actualiza tipo de vehículo y planta de una plaza (metadatos de la instalación).
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
     * @param id
     * @return
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
