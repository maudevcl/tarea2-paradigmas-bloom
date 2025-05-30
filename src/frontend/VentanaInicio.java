package frontend;

import backend.CargadorJSON;
import backend.Evaluacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class VentanaInicio extends JFrame {

    private JLabel etiquetaResumen;
    private JButton botonCargar;
    private JButton botonIniciar;
    private Evaluacion evaluacionCargada;

    public VentanaInicio() {
        super("Tarea 2 - Evaluador Bloom");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setLayout(new BorderLayout());

        etiquetaResumen = new JLabel("Cargue un archivo con preguntas.");
        etiquetaResumen.setHorizontalAlignment(SwingConstants.CENTER);

        botonCargar = new JButton("Cargar archivo de ítems");
        botonIniciar = new JButton("Iniciar prueba");
        botonIniciar.setEnabled(false); 

        
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonCargar);
        panelBotones.add(botonIniciar);

        this.add(etiquetaResumen, BorderLayout.CENTER);
        this.add(panelBotones, BorderLayout.SOUTH);

        
        botonCargar.addActionListener((ActionEvent e) -> {
            JFileChooser selector = new JFileChooser();
            int resultado = selector.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                evaluacionCargada = CargadorJSON.cargarEvaluacionDesdeArchivo(archivo.getAbsolutePath());

                if (evaluacionCargada != null) {
                    int totalPreguntas = evaluacionCargada.getPreguntas().size();
                    int tiempoTotal = evaluacionCargada.getPreguntas()
                            .stream().mapToInt(p -> p.getTiempo_estimado_seg()).sum();

                    etiquetaResumen.setText("<html>Archivo cargado correctamente.<br>"
                            + "Preguntas: " + totalPreguntas + "<br>"
                            + "Tiempo estimado total: " + tiempoTotal + " segundos</html>");
                    botonIniciar.setEnabled(true);
                } else {
                    etiquetaResumen.setText("Error al cargar el archivo.");
                    botonIniciar.setEnabled(false);
                }
            }
        });

       
        botonIniciar.addActionListener((ActionEvent e) -> {
            new VentanaPrueba(evaluacionCargada); 
            this.dispose(); 
        });

        this.setVisible(true);
    }
}
