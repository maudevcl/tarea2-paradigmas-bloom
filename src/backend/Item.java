package backend;

import java.util.List;

public class Item {
    private String tipo;
    private String nivel_bloom;
    private String enunciado;
    private List<String> opciones;
    private String respuesta_correcta;
    private int tiempo_estimado_seg;

    public String getTipo() {
        return tipo;
    }

    public String getNivel_bloom() {
        return nivel_bloom;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public String getRespuesta_correcta() {
        return respuesta_correcta;
    }

    public int getTiempo_estimado_seg() {
        return tiempo_estimado_seg;
    }

    @Override
    public String toString() {
        return "Item{" +
                "tipo='" + tipo + '\'' +
                ", nivel_bloom= '" + nivel_bloom + '\'' +
                ", enunciado='" + enunciado + '\'' +
                ", opciones=" + opciones +
                ", respuesta_correcta='" + respuesta_correcta + '\'' +
                ", tiempo=" + tiempo_estimado_seg +
                '}';
    }
}
