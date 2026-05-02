package Persistencia.Config;

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

    public ConfigJSONDAO() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            config = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void existeElArchivo() {
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
            e.printStackTrace();
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

    public String getDBNombreAdmin(){return config.getAsJsonObject("database").get("contrasenaAdmin").getAsString();}
}
