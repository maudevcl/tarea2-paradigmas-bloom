package frontend;

import backend.Evaluacion;
import backend.Item;
import backend.ResultadoEvaluacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class VentanaResumen extends JFrame {

    private Evaluacion evaluacion;
    private List<String> respuestasUsuario;

    public VentanaResumen(ResultadoEvaluacion resultado, Evaluacion evaluacion, List<String> respuestasUsuario) {
        this.evaluacion = evaluacion;
        this.respuestasUsuario = respuestasUsuario;

        setTitle("Resumen de Resultados");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // =============================
        // Barra de progreso visual
        // =============================
        int totalPreguntas = evaluacion.getPreguntas().size();
        int correctas = contarRespuestasCorrectas();
        JProgressBar barra = new JProgressBar(0, totalPreguntas);
        barra.setValue(correctas);
        barra.setStringPainted(true);
        barra.setString("Respuestas correctas: " + correctas + "/" + totalPreguntas);
        add(barra, BorderLayout.NORTH);

        // =============================
        // Área de texto de resumen
        // =============================
        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));

        StringBuilder resumen = new StringBuilder();
        resumen.append("📌 Total de preguntas: ").append(totalPreguntas).append("\n");
        resumen.append("✅ Total de respuestas correctas: ").append(correctas).append("\n");

        double porcentajeGlobal = (correctas * 100.0 / totalPreguntas);
        resumen.append(String.format("📈 Desempeño global: %.2f%%\n\n", porcentajeGlobal));

        if (porcentajeGlobal == 100.0) {
            resumen.append("🎯 ¡Excelente trabajo! Has respondido todo correctamente.\n\n");
        } else if (porcentajeGlobal >= 70.0) {
            resumen.append("👍 Buen desempeño. Puedes reforzar algunos conceptos.\n\n");
        } else {
            resumen.append("⚠️ Es recomendable repasar ciertos niveles de la taxonomía.\n\n");
        }

        resumen.append("📊 Porcentaje de respuestas correctas por nivel Bloom:\n");
        Map<String, Double> porNivel = resultado.getPorcentajePorNivel();
        for (String nivel : porNivel.keySet()) {
            resumen.append(String.format("  %-10s : %.2f%%\n", nivel, porNivel.get(nivel)));
        }

        resumen.append("\n📊 Porcentaje de respuestas correctas por tipo de ítem:\n");
        Map<String, Double> porTipo = resultado.getPorcentajePorTipo();
        for (String tipo : porTipo.keySet()) {
            resumen.append(String.format("  %-18s : %.2f%%\n", tipo, porTipo.get(tipo)));
        }

        areaResultados.setText(resumen.toString());
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.CENTER);

        // =============================
        // Botón inferior
        // =============================
        JButton botonRevisar = new JButton("Revisar preguntas una a una");
        add(botonRevisar, BorderLayout.SOUTH);

        botonRevisar.addActionListener((ActionEvent e) -> {
            new VentanaRevision(evaluacion, respuestasUsuario);
            this.dispose(); // cerrar esta ventana
        });

        setVisible(true);
    }

    private int contarRespuestasCorrectas() {
        int correctas = 0;
        List<Item> preguntas = evaluacion.getPreguntas();
        for (int i = 0; i < preguntas.size(); i++) {
            String usuario = respuestasUsuario.get(i);
            String correcta = preguntas.get(i).getRespuesta_correcta();
            if (usuario != null && usuario.equalsIgnoreCase(correcta)) {
                correctas++;
            }
        }
        return correctas;
    }
}
