package Persistencia.Config;

import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase que nos permitirá sacar información del fichero config.json
 */
public class ConfigJSONDAO {
    /**
     * Atributo con la dirección del fichero.
     */
    private static String FILE_PATH = "src/CONFIG/config.json"; ;
    /**
     * Instancia de JsonObject que nos permitirá trabajar con JSON.
     */
    private JsonObject config;

    /**
     * Constructor de la Clase ConfigJSONDAO
     * @throws ExcepcionFicheroNoEncontrado
     */
    public ConfigJSONDAO() throws ExcepcionFicheroNoEncontrado {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            config = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            System.out.println(e);
            existeElArchivo();
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    /**
     * Esta función comprueba si el fichero config.json existe y lo crea en el caso de que no exista.
     * @throws ExcepcionFicheroNoEncontrado
     */
    public void existeElArchivo() throws ExcepcionFicheroNoEncontrado {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write("{\n" +
                        "  \"database\":{"+
                        "  \"dbName\": \"nombre\",\n" +
                        "  \"dbPort\": \"port\",\n" +
                        "  \"dbIp\": \"ip\",\n" +
                        "  \"dbHost\": \"name\" \n"+
                        "  \"dbPassword\": \"name\" \n"+
                        "}, \n"+
                        "  \"nombreAdmin\": \"Admin\",\n" +
                        "  \"contrasenaAdmin\": \"admin\",\n" +
                        "  \"tiempoEntradaVehiculos\": 5,\n" +
                        "}");
                writer.close();
            }
        } catch (Exception e) {
            throw new ExcepcionFicheroNoEncontrado();
        }
    }

    /**
     * Esta función devuelve el host de la BBDD.
     * @return String con el host de la BBDD.
     */
    public String getDBHost(){
        return config.getAsJsonObject("database").get("dbHost").getAsString();
    }

    /**
     * Esta función devuelve el puerto de una BBDD.
     * @return Número con el puerto.
     */
    public int getDBPuerto() {
        return config.getAsJsonObject("database").get("dbPort").getAsInt();
    }

    /**
     * Esta función devuelve el nombre de la BBDD.
     * @return String con el nombre de la BBDD.
     */
    public String getDBNombre(){
        return config.getAsJsonObject("database").get("dbName").getAsString();
    }

    /**
     * Esta función devuelve la IP del servidor mySQL.
     * @return String con la ip.
     */
    public String getDBIp(){
        return config.getAsJsonObject("database").get("dbIp").getAsString();
    }

    /**
     * Esta función devuelve la contraseña de la BBDD.
     * @return String con la contraseña de la BBDD.
     */
    public String getDBContraseña(){
        return config.getAsJsonObject("database").get("dbPassword").getAsString();
    }

    /**
     * Esta función devuelve el nombre del usuario administrador del programa.
     * @return String con el nombre de admin.
     */
    public String getNombreAdmin(){
        return config.get("nombreAdmin").getAsString();
    };

    /**
     * Esta función devuelve la contraseña del administrador.
     * @return String con la contraseña.
     */
    public String getAdminPass(){
        return config.get("contrasenaAdmin").getAsString();
    };

    /**
     * Esta función devuelve el número de plazas disponibles.
     * @return String con el numero.
     */
    public String getPlazasDisp(){
        return config.get("cantidadPlazasDisponibles").getAsString();
    };

    /**
     * Esta función devuelve el tipo.
     * @return String con el tipo.
     */
    public String getTipo(){
        return config.get("tipo").getAsString();
    };

    /**
     * Máximo N (segundos) para el retardo aleatorio entre acciones del simulador de tráfico (enunciado: entre 1 y N).
     * Prioriza {@code tiempoEntradaVehiculos} del JSON; si no existe, {@code intervaloSimulacionSegundos}.
     * 0 = simulación desactivada.
     * @return Numero con el numero máximo de segundos.
     */
    public int getMaxSegundosEntreEventosSimulacion() {
        try {
            if (config != null && config.has("tiempoEntradaVehiculos")) {
                int n = config.get("tiempoEntradaVehiculos").getAsInt();
                if (n > 0) {
                    return n;
                }
            }
            if (config != null && config.has("intervaloSimulacionSegundos")) {
                int n = config.get("intervaloSimulacionSegundos").getAsInt();
                if (n > 0) {
                    return n;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

}
