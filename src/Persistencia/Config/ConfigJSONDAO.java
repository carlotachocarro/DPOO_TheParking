package Persistencia.Config;

import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigJSONDAO {
    private static String FILE_PATH = "src/CONFIG/config.json"; ;

    private JsonObject config;

    public ConfigJSONDAO() throws ExcepcionFicheroNoEncontrado {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            config = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            System.out.println(e);
            throw new ExcepcionFicheroNoEncontrado();
        }
    }


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

    public String getDBHost(){
        return config.getAsJsonObject("database").get("dbHost").getAsString();
    }

    public int getDBPuerto() {
        return config.getAsJsonObject("database").get("dbPort").getAsInt();
    }

    public String getDBNombre(){
        return config.getAsJsonObject("database").get("dbName").getAsString();
    }

    public String getDBIp(){
        return config.getAsJsonObject("database").get("dbIp").getAsString();
    }

    public String getDBContraseña(){
        return config.getAsJsonObject("database").get("dbPassword").getAsString();
    }


    public String getNombreAdmin(){
        return config.get("nombreAdmin").getAsString();
    };

    public String getAdminPass(){
        return config.get("contrasenaAdmin").getAsString();
    };

    public String getPlazasDisp(){
        return config.get("cantidadPlazasDisponibles").getAsString();
    };

    public String getTipo(){
        return config.get("tipo").getAsString();
    };

    /**
     * Máximo N (segundos) para el retardo aleatorio entre acciones del simulador de tráfico (enunciado: entre 1 y N).
     * Prioriza {@code tiempoEntradaVehiculos} del JSON; si no existe, {@code intervaloSimulacionSegundos}.
     * 0 = simulación desactivada.
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

    /**
     * @deprecated Usar {@link #getMaxSegundosEntreEventosSimulacion()} (incluye {@code tiempoEntradaVehiculos}).
     */
    public int getIntervaloSimulacionSegundos() {
        return getMaxSegundosEntreEventosSimulacion();
    }
}
