package Persistencia.Daoimpl;

import Negocio.Entidades.AvisoCancelacionUsuario;
import Persistencia.SQL_CRUD;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Avisos mostrados una vez al iniciar sesión. Tabla esperada:
 * <pre>
 * CREATE TABLE IF NOT EXISTS aviso_cancelacion_admin (
 *   id_aviso INT NOT NULL AUTO_INCREMENT,
 *   id_usuario INT NOT NULL,
 *   cod_plaza_cancel VARCHAR(32) NOT NULL,
 *   planta_cancel INT NOT NULL,
 *   matricula VARCHAR(32) NOT NULL,
 *   tipo_vehiculo VARCHAR(64) NOT NULL,
 *   cod_plaza_nueva VARCHAR(32) DEFAULT NULL,
 *   planta_nueva INT DEFAULT NULL,
 *   PRIMARY KEY (id_aviso),
 *   KEY idx_usuario (id_usuario)
 * );
 * </pre>
 */
public class AvisoLoginDBDAO {

    public AvisoLoginDBDAO() throws ExcepcionFicheroNoEncontrado {
        try {
            Singleton.getInstance().getConn();
        } catch (Exception e) {
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    public boolean insertar(String idUsuario, AvisoCancelacionUsuario aviso) throws ExcepcionGeneralDB {
        try {
            if (aviso.tieneReasignacion()) {
                String query = """
                        INSERT INTO aviso_cancelacion_admin
                        (id_usuario, cod_plaza_cancel, planta_cancel, matricula, tipo_vehiculo, cod_plaza_nueva, planta_nueva)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;
                ArrayList<String> values = new ArrayList<>();
                ArrayList<String> types = new ArrayList<>();
                values.add(idUsuario);
                values.add(aviso.codigoPlazaCancelada());
                values.add(aviso.plantaCancelada());
                values.add(aviso.matricula());
                values.add(aviso.tipoVehiculo());
                values.add(aviso.codigoPlazaNueva());
                values.add(aviso.plantaNueva());
                types.add("int");
                types.add("String");
                types.add("int");
                types.add("String");
                types.add("String");
                types.add("String");
                types.add("int");
                return SQL_CRUD.CUD(query, values, types) > 0;
            } else {
                String query = """
                        INSERT INTO aviso_cancelacion_admin
                        (id_usuario, cod_plaza_cancel, planta_cancel, matricula, tipo_vehiculo)
                        VALUES (?, ?, ?, ?, ?)
                        """;
                ArrayList<String> values = new ArrayList<>();
                ArrayList<String> types = new ArrayList<>();
                values.add(idUsuario);
                values.add(aviso.codigoPlazaCancelada());
                values.add(aviso.plantaCancelada());
                values.add(aviso.matricula());
                values.add(aviso.tipoVehiculo());
                types.add("int");
                types.add("String");
                types.add("int");
                types.add("String");
                types.add("String");
                return SQL_CRUD.CUD(query, values, types) > 0;
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
    }

    public ArrayList<AvisoCancelacionUsuario> listarPorUsuario(String idUsuario) throws ExcepcionGeneralDB {
        String query = """
                SELECT cod_plaza_cancel, planta_cancel, matricula, tipo_vehiculo, cod_plaza_nueva, planta_nueva
                FROM aviso_cancelacion_admin WHERE id_usuario = ?
                """;
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(idUsuario);
        types.add("int");
        ArrayList<AvisoCancelacionUsuario> out = new ArrayList<>();
        try {
            ResultSet res = SQL_CRUD.Select(query, values, types);
            while (res.next()) {
                String nuevaCod = res.getString("cod_plaza_nueva");
                if (res.wasNull() || nuevaCod == null || nuevaCod.isBlank()) {
                    out.add(new AvisoCancelacionUsuario(
                            res.getString("cod_plaza_cancel"),
                            String.valueOf(res.getInt("planta_cancel")),
                            res.getString("matricula"),
                            res.getString("tipo_vehiculo"),
                            null,
                            null
                    ));
                } else {
                    out.add(new AvisoCancelacionUsuario(
                            res.getString("cod_plaza_cancel"),
                            String.valueOf(res.getInt("planta_cancel")),
                            res.getString("matricula"),
                            res.getString("tipo_vehiculo"),
                            nuevaCod,
                            String.valueOf(res.getInt("planta_nueva"))
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new ExcepcionGeneralDB();
        }
        return out;
    }

    public boolean eliminarTodosUsuario(String idUsuario) throws ExcepcionGeneralDB {
        String query = "DELETE FROM aviso_cancelacion_admin WHERE id_usuario = ?";
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        values.add(idUsuario);
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
