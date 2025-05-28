package backend;

import java.util.List;

public class Evaluacion {
    private String asignatura;
    private int anio;
    private List<Item> preguntas;

    public String getAsignatura() {
        return asignatura;
    }

    public int getAnio() {
        return anio;
    }

    public List<Item> getPreguntas() {
        return preguntas;
    }

    @Override
    public String toString() {
        return "Evaluacion{" +
                "asignatura='" + asignatura + '\'' +
                ", anio=" + anio +
                ", preguntas=" + preguntas +
                '}';
    }
}
