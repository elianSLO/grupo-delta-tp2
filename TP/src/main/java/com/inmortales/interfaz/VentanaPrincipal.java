package com.inmortales.interfaz;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.inmortales.ConfiguracionJuego;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final ConfiguracionJuego configuracion;

    public VentanaPrincipal() {

        setTitle("Torneo de los Inmortales");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        configuracion = new ConfiguracionJuego();

        // Por ahora mostramos una pantalla inicial.
        JPanel inicio = crearPanelInicio();

        contenedor.add(inicio, "INICIO");

        add(contenedor);

        cardLayout.show(contenedor, "INICIO");
    }

    private JPanel crearPanelInicio() {

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.BorderLayout());

        javax.swing.JLabel titulo = new javax.swing.JLabel(
            "TORNEO DE LOS INMORTALES",
            javax.swing.SwingConstants.CENTER
        );

        titulo.setFont(
            new java.awt.Font("Serif", java.awt.Font.BOLD, 32)
        );

        javax.swing.JButton comenzar = new javax.swing.JButton(
            "Comenzar torneo"
        );

        comenzar.addActionListener(e -> {

            PanelInscripcion inscripcion = new PanelInscripcion(
                configuracion,
                sesion -> {

                    PanelCombate combate = new PanelCombate(
                        sesion,
                        campeona -> {

                            PanelResultados resultados =
                                new PanelResultados(
                                    sesion,
                                    campeona,
                                    () -> cardLayout.show(
                                        contenedor, "INICIO"
                                    )
                                );

                            contenedor.add(resultados, "RESULTADOS");
                            cardLayout.show(contenedor, "RESULTADOS");
                        }
                    );

                    contenedor.add(combate, "COMBATE");
                    cardLayout.show(contenedor, "COMBATE");
                }
            );

            contenedor.add(inscripcion, "INSCRIPCION");
            cardLayout.show(contenedor, "INSCRIPCION");
        });

        panel.add(titulo, java.awt.BorderLayout.CENTER);
        panel.add(comenzar, java.awt.BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
