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
        setSize(1100, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        configuracion = new ConfiguracionJuego();

        // Pantalla inicial.
        JPanel inicio = crearPanelInicio();

        contenedor.add(inicio, "INICIO");

        add(contenedor);

        cardLayout.show(contenedor, "INICIO");
    }

    private JPanel crearPanelInicio() {

        // Pantalla de bienvenida: fondo oscuro con el título centrado.
        JPanel panel = new JPanel(new java.awt.GridBagLayout());
        panel.setBackground(Estilo.PRINCIPAL);

        javax.swing.JLabel titulo = new javax.swing.JLabel(
            "EL TORNEO DE LOS INMORTALES"
        );
        titulo.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 44));
        titulo.setForeground(java.awt.Color.WHITE);

        javax.swing.JLabel subtitulo = new javax.swing.JLabel(
            "La elección de los Dos Santos Supremos"
        );
        subtitulo.setFont(new java.awt.Font("Serif", java.awt.Font.ITALIC, 22));
        subtitulo.setForeground(Estilo.DORADO);

        javax.swing.JLabel descripcion = new javax.swing.JLabel(
            "Parejas de cultivadores se enfrentan en rondas eliminatorias"
            + " hasta que solo una queda en pie."
        );
        descripcion.setFont(Estilo.SUBTITULO);
        descripcion.setForeground(new java.awt.Color(0xC9CCD6));

        javax.swing.JButton comenzar = Estilo.boton("Comenzar torneo", true);
        comenzar.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        comenzar.setPreferredSize(new java.awt.Dimension(240, 48));

        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridx = 0;
        c.insets = new java.awt.Insets(6, 0, 6, 0);

        c.gridy = 0;
        panel.add(titulo, c);
        c.gridy = 1;
        panel.add(subtitulo, c);
        c.gridy = 2;
        c.insets = new java.awt.Insets(18, 0, 36, 0);
        panel.add(descripcion, c);
        c.gridy = 3;
        c.insets = new java.awt.Insets(6, 0, 6, 0);
        panel.add(comenzar, c);

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

        return panel;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Estilo.aplicarAspectoGeneral();
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
