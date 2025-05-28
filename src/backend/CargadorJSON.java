package backend;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class CargadorJSON {
    public static Evaluacion cargarEvaluacionDesdeArchivo(String ruta) {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(ruta));
            Evaluacion evaluacion = gson.fromJson(reader, Evaluacion.class);
            return evaluacion;
        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo: " + ruta);
            return null;
        }
    }
}
