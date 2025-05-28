package frontend;

import backend.Evaluacion;
import backend.Item;
import backend.ResultadoEvaluacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VentanaRevision extends JFrame {

    private final Evaluacion evaluacion;
    private final List<String> respuestasUsuario;
    private int indiceActual = 0;

    // UI
    private JLabel enunciadoLabel;
    private JPanel opcionesPanel;
    private JLabel resultadoLabel;
    private JButton botonAtras;
    private JButton botonSiguiente;
    private JButton botonResumen;

    public VentanaRevision(Evaluacion evaluacion, List<String> respuestasUsuario) {
        this.evaluacion = evaluacion;
        this.respuestasUsuario = respuestasUsuario;

        setTitle("Revisión de Preguntas");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        enunciadoLabel = new JLabel();
        enunciadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enunciadoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(enunciadoLabel, BorderLayout.NORTH);

        opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new BoxLayout(opcionesPanel, BoxLayout.Y_AXIS));
        add(opcionesPanel, BorderLayout.CENTER);

        resultadoLabel = new JLabel();
        resultadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultadoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(resultadoLabel, BorderLayout.SOUTH);

        // Botones
        botonAtras = new JButton("← Atrás");
        botonSiguiente = new JButton("→ Siguiente");
        botonResumen = new JButton("Volver a resumen");

        JPanel botones = new JPanel();
        botones.add(botonAtras);
        botones.add(botonSiguiente);
        botones.add(botonResumen);
        add(botones, BorderLayout.SOUTH);

        botonAtras.addActionListener((ActionEvent e) -> {
            indiceActual--;
            mostrarPregunta();
        });

        botonSiguiente.addActionListener((ActionEvent e) -> {
            indiceActual++;
            mostrarPregunta();
        });

        botonResumen.addActionListener((ActionEvent e) -> {
            new VentanaResumen(new ResultadoEvaluacion(evaluacion, respuestasUsuario),
                    evaluacion, respuestasUsuario);
            this.dispose();
        });

        mostrarPregunta();
        setVisible(true);
    }

    private void mostrarPregunta() {
        Item item = evaluacion.getPreguntas().get(indiceActual);
        enunciadoLabel.setText("<html><div style='text-align: center;'>Pregunta " + (indiceActual + 1) + " de " +
                evaluacion.getPreguntas().size() + "<br><br>" + item.getEnunciado() + "</div></html>");

        opcionesPanel.removeAll();

        String respuestaUsuario = respuestasUsuario.get(indiceActual);
        String respuestaCorrecta = item.getRespuesta_correcta();

        if (item.getTipo().equalsIgnoreCase("seleccion_multiple") && item.getOpciones() != null) {
            for (String opcion : item.getOpciones()) {
                JLabel etiqueta = new JLabel(opcion);
                if (opcion.equals(respuestaCorrecta)) {
                    etiqueta.setForeground(Color.GREEN.darker());
                    etiqueta.setText("✔️ " + opcion + " (correcta)");
                } else if (respuestaUsuario != null && opcion.equals(respuestaUsuario)) {
                    etiqueta.setForeground(Color.RED);
                    etiqueta.setText("❌ " + opcion + " (tu respuesta)");
                }
                opcionesPanel.add(etiqueta);
            }
        } else {
            String[] opcionesVF = {"V", "F"};
            for (String opcion : opcionesVF) {
                JLabel etiqueta = new JLabel(opcion);
                if (opcion.equals(respuestaCorrecta)) {
                    etiqueta.setForeground(Color.GREEN.darker());
                    etiqueta.setText("✔️ " + opcion + " (correcta)");
                } else if (respuestaUsuario != null && opcion.equals(respuestaUsuario)) {
                    etiqueta.setForeground(Color.RED);
                    etiqueta.setText("❌ " + opcion + " (tu respuesta)");
                }
                opcionesPanel.add(etiqueta);
            }
        }

        if (respuestaUsuario == null || respuestaUsuario.isBlank()) {
            resultadoLabel.setText("❗ Pregunta no respondida");
            resultadoLabel.setForeground(Color.ORANGE.darker());
        } else if (respuestaUsuario.equalsIgnoreCase(respuestaCorrecta)) {
            resultadoLabel.setText("✅ Respuesta correcta");
            resultadoLabel.setForeground(Color.GREEN.darker());
        } else {
            resultadoLabel.setText("❌ Respuesta incorrecta");
            resultadoLabel.setForeground(Color.RED.darker());
        }

        opcionesPanel.revalidate();
        opcionesPanel.repaint();

        botonAtras.setEnabled(indiceActual > 0);
        botonSiguiente.setEnabled(indiceActual < evaluacion.getPreguntas().size() - 1);
    }
}
