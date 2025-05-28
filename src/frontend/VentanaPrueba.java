package frontend;

import backend.Evaluacion;
import backend.Item;
import backend.ResultadoEvaluacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrueba extends JFrame {

    private final Evaluacion evaluacion;
    private int indiceActual = 0;
    private final List<String> respuestasUsuario;

    // Componentes visuales
    private JLabel enunciadoLabel;
    private JPanel opcionesPanel;
    private JButton botonAtras;
    private JButton botonSiguiente;

    public VentanaPrueba(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
        this.respuestasUsuario = new ArrayList<>(evaluacion.getPreguntas().size());
        for (int i = 0; i < evaluacion.getPreguntas().size(); i++) {
            respuestasUsuario.add(null); // Reservar espacio para cada respuesta
        }

        setTitle("Aplicación de Prueba");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Enunciado
        enunciadoLabel = new JLabel();
        enunciadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enunciadoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(enunciadoLabel, BorderLayout.NORTH);

        // Panel para opciones
        opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new BoxLayout(opcionesPanel, BoxLayout.Y_AXIS));
        add(opcionesPanel, BorderLayout.CENTER);

        // Botones navegación
        botonAtras = new JButton("← Volver atrás");
        botonSiguiente = new JButton("→ Siguiente");

        JPanel panelNavegacion = new JPanel();
        panelNavegacion.add(botonAtras);
        panelNavegacion.add(botonSiguiente);
        add(panelNavegacion, BorderLayout.SOUTH);

        // Listeners
        botonAtras.addActionListener((ActionEvent e) -> {
            guardarRespuestaSeleccionada();
            indiceActual--;
            mostrarPregunta();
        });

        botonSiguiente.addActionListener((ActionEvent e) -> {
            guardarRespuestaSeleccionada();

            if (respuestasUsuario.get(indiceActual) == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar una respuesta antes de continuar.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (indiceActual < evaluacion.getPreguntas().size() - 1) {
                indiceActual++;
                mostrarPregunta();
            } else {
                ResultadoEvaluacion resultado = new ResultadoEvaluacion(evaluacion, respuestasUsuario);
                new VentanaResumen(resultado, evaluacion, respuestasUsuario);
                dispose();
            }
        });

        mostrarPregunta();
        setVisible(true);
    }

    private void mostrarPregunta() {
        Item item = evaluacion.getPreguntas().get(indiceActual);
        enunciadoLabel.setText("<html><div style='text-align: center;'>Pregunta " + (indiceActual + 1) + " de " +
                evaluacion.getPreguntas().size() + "<br><br>" + item.getEnunciado() + "</div></html>");

        opcionesPanel.removeAll();

        ButtonGroup grupo = new ButtonGroup();

        if (item.getTipo().equalsIgnoreCase("seleccion_multiple") && item.getOpciones() != null) {
            for (String opcion : item.getOpciones()) {
                JRadioButton botonOpcion = new JRadioButton(opcion);
                grupo.add(botonOpcion);
                opcionesPanel.add(botonOpcion);

                if (opcion.equals(respuestasUsuario.get(indiceActual))) {
                    botonOpcion.setSelected(true);
                }
            }
        } else if (item.getTipo().equalsIgnoreCase("verdadero_falso")) {
            String[] opcionesVF = {"V", "F"};
            for (String opcion : opcionesVF) {
                JRadioButton botonOpcion = new JRadioButton(opcion);
                grupo.add(botonOpcion);
                opcionesPanel.add(botonOpcion);

                if (opcion.equals(respuestasUsuario.get(indiceActual))) {
                    botonOpcion.setSelected(true);
                }
            }
        }

        opcionesPanel.revalidate();
        opcionesPanel.repaint();

        botonAtras.setEnabled(indiceActual > 0);
        botonSiguiente.setText(indiceActual < evaluacion.getPreguntas().size() - 1 ? "→ Siguiente" : "Enviar respuestas");
    }

    private void guardarRespuestaSeleccionada() {
        for (Component c : opcionesPanel.getComponents()) {
            if (c instanceof JRadioButton radio && radio.isSelected()) {
                respuestasUsuario.set(indiceActual, radio.getText());
                return;
            }
        }
        respuestasUsuario.set(indiceActual, null); // sin respuesta
    }
}
