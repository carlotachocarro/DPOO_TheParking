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
        String query = "INSERT INTO plaza_parking (tipo_vehiculo, planta, estado_actual, estado_reserva, matricula, simulado, id_usuario) VALUES (?, ?, 0,0,'none',0, NULL)";
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
                    plazas.add(new Plaza(tipo_plaza,planta,id, ocu, rese, sim, u.getUsuarioById(userId+"")));
                } else {
                    plazas.add(new Plaza(tipo_plaza,planta,id, ocu, rese, sim, null));
                }

            } catch (Exception e){
                System.out.println(e);
            }

        }
        return plazas;
    }

    public ArrayList<Plaza> getPlazasLibres(String tipo){
        String query = "SELECT * FROM plaza_parking WHERE estado_actual = 0 AND estado_reserva = 0 AND simulado = 0 AND tipo_vehiculo = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Plaza> plazas = new ArrayList<>();

        values.add(tipo);
        types.add("String");

        ResultSet res = SQL_CRUD.Select(query, values, types);
        while(true){
            try {
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
                    plazas.add(new Plaza(tipo_plaza,planta,id, ocu, rese, sim, u.getUsuarioById(userId+"")));
                } else {
                    plazas.add(new Plaza(tipo_plaza,planta,id, ocu, rese, sim, null));
                }

            } catch (Exception e){
                System.out.println(e);
            }

        }
        return plazas;
    }

    /**
     * Devuelve el usuario que ocupa una plaza de parking.
     * @param id
     * @return
     */
    public Usuario getPlazaUsuario(String id){
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
        }
        return null;
    }

    public boolean ocuparPlaza(String id_plaza ,boolean ocupacion, String matricula, String nombreUsuario){
        String query = "";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        if (ocupacion){
            UsuarioDBDAO u = new UsuarioDBDAO();
            String idUser = u.getUsuarioId(nombreUsuario, "");
            query = "UPDATE plaza_parking SET estado_actual = 1, matricula = ?, id_usuario = ? WHERE id_plaza = ?";
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
        int result = SQL_CRUD.CUD(query, values, types);
        return result > 0;
    }

    public boolean ocuparPlazaSimul(String id_plaza ,boolean ocupacion, String matricula){
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
        int result = SQL_CRUD.CUD(query, values, types);
        return result > 0;
    }

    public boolean reservarPlaza(String id_plaza ,boolean reserva){
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
        int result = SQL_CRUD.CUD(query, values, types);
        return result > 0;
    }

    public boolean simularPlaza(String id_plaza ,boolean simul){
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
        int result = SQL_CRUD.CUD(query, values, types);
        return result > 0;
    }

}
