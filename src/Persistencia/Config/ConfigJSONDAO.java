package Persistencia.Config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigJSONDAO {
    private static String FILE_PATH ;

    public ConfigJSONDAO() {
        this.FILE_PATH = "src/CONFIG/config.json";
    }


    public void existeElArchivo() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write("{\n" +
                        "  \"nombreBaseDatos\": \"nombre\",\n" +
                        "  \"puertoConexion\": \"port\",\n" +
                        "  \"ip\": \"ip\",\n" +
                        "  \"nombreAdmin\": \"Admin\",\n" +
                        "  \"contrasenaAdmin\": \"admin\",\n" +
                        "  \"cantidadPlazasDisponibles\": 5,\n" +
                        "  \"tipo\": \"coche\"\n" +
                        "}");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String[]  leerTodaConfigJSON(){
        FileReader reader = null;
        List<String> resultList = new ArrayList<>();
        ///  no deberia lanzar ninguna excepcion
        try {
            reader = new FileReader(FILE_PATH);
        } catch (FileNotFoundException e) {

        }
        Gson gson = new Gson();
        List<Map<String,Object>> providers = gson.fromJson(reader,new TypeToken<List<Map<String,Object>>>(){}.getType());
        ArrayList<String> res = new ArrayList<String>();
        StringBuilder result = new StringBuilder();
        result.append("nombreBaseDatos");
        result.append("-");
        result.append("puertoConexion");
        result.append("-");
        result.append("ip");
        result.append("-");
        result.append("contrasenia");
        result.append("-");
        result.append("cantidadPlazasDisponibles");
        result.append("-");
        result.append("tipo");
        result.append("-");

        res.add(result.toString());

        return resultList.toArray(new String[0]);
    }
}
