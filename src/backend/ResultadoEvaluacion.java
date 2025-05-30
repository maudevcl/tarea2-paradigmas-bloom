package backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultadoEvaluacion {
    private final Evaluacion evaluacion;
    private final List<String> respuestasUsuario;

    private Map<String, Integer> correctasPorNivel = new HashMap<>();
    private Map<String, Integer> totalPorNivel = new HashMap<>();

    private Map<String, Integer> correctasPorTipo = new HashMap<>();
    private Map<String, Integer> totalPorTipo = new HashMap<>();

    public ResultadoEvaluacion(Evaluacion evaluacion, List<String> respuestasUsuario) {
        this.evaluacion = evaluacion;
        this.respuestasUsuario = respuestasUsuario;
        calcularResultados();
    }

    private void calcularResultados() {
        List<Item> preguntas = evaluacion.getPreguntas();

        for (int i = 0; i < preguntas.size(); i++) {
            Item pregunta = preguntas.get(i);
            String tipo = pregunta.getTipo();
            String nivel = pregunta.getNivel_bloom();
            String respuestaUsuario = respuestasUsuario.get(i);

           
            if (respuestaUsuario == null || respuestaUsuario.isBlank()) {
                continue;
            }

            
            totalPorTipo.put(tipo, totalPorTipo.getOrDefault(tipo, 0) + 1);
            totalPorNivel.put(nivel, totalPorNivel.getOrDefault(nivel, 0) + 1);

           
            if (pregunta.getRespuesta_correcta().equalsIgnoreCase(respuestaUsuario)) {
                correctasPorTipo.put(tipo, correctasPorTipo.getOrDefault(tipo, 0) + 1);
                correctasPorNivel.put(nivel, correctasPorNivel.getOrDefault(nivel, 0) + 1);
            }
        }
    }

    public Map<String, Double> getPorcentajePorNivel() {
        return calcularPorcentaje(correctasPorNivel, totalPorNivel);
    }

    public Map<String, Double> getPorcentajePorTipo() {
        return calcularPorcentaje(correctasPorTipo, totalPorTipo);
    }

    private Map<String, Double> calcularPorcentaje(Map<String, Integer> correctas, Map<String, Integer> totales) {
        Map<String, Double> resultado = new HashMap<>();
        for (String clave : totales.keySet()) {
            int total = totales.getOrDefault(clave, 0);
            int correctasValor = correctas.getOrDefault(clave, 0);
            double porcentaje = total > 0 ? (correctasValor * 100.0 / total) : 0.0;
            resultado.put(clave, porcentaje);
        }
        return resultado;
    }
}
