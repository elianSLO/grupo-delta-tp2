package com.inmortales.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

// Colores y letras compartidos por todas las pantallas,
// así la interfaz se ve igual en todos lados.
public final class Estilo {

    // Paleta
    public static final Color FONDO = new Color(0xF4F1EA);
    public static final Color TARJETA = Color.WHITE;
    public static final Color PRINCIPAL = new Color(0x1F2A44);
    public static final Color DORADO = new Color(0xC9A227);
    public static final Color TEXTO = new Color(0x22252B);
    public static final Color TEXTO_SUAVE = new Color(0x6B6F78);
    public static final Color BORDE = new Color(0xD9D4C7);

    // Colores de cada tipo de acontecimiento (en formato HTML)
    public static final String HTML_ATAQUE = "#1F4E9A";
    public static final String HTML_TECNICA = "#7A4E00";
    public static final String HTML_VENENO = "#6A1B9A";
    public static final String HTML_ELIMINADO = "#B03A2E";
    public static final String HTML_SUAVE = "#6B6F78";

    // Letras
    public static final Font TITULO = new Font("Serif", Font.BOLD, 30);
    public static final Font SUBTITULO = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font NEGRITA = new Font("SansSerif", Font.BOLD, 14);

    private Estilo() {
    }

    // Aspecto general de Swing (Nimbus viene incluido en Java).
    public static void aplicarAspectoGeneral() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si no está disponible, queda el aspecto por defecto.
        }

        UIManager.put("control", FONDO);
        UIManager.put("nimbusBase", PRINCIPAL);
        UIManager.put("nimbusFocus", DORADO);
        UIManager.put("nimbusSelectionBackground", PRINCIPAL);
        UIManager.put("Table.alternateRowColor", new Color(0xF7F5EF));
    }

    // Franja oscura de arriba con título y subtítulo.
    public static JPanel encabezado(String titulo, String subtitulo) {

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBackground(PRINCIPAL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, DORADO),
            BorderFactory.createEmptyBorder(18, 28, 16, 28)
        ));

        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(TITULO);
        etiquetaTitulo.setForeground(Color.WHITE);
        panel.add(etiquetaTitulo, BorderLayout.NORTH);

        if (subtitulo != null) {
            JLabel etiquetaSubtitulo = new JLabel(subtitulo);
            etiquetaSubtitulo.setFont(SUBTITULO);
            etiquetaSubtitulo.setForeground(new Color(0xE6DFC8));
            panel.add(etiquetaSubtitulo, BorderLayout.SOUTH);
        }

        return panel;
    }

    // Botón principal (dorado) o secundario (claro).
    public static JButton boton(String texto, boolean principal) {

        JButton boton = new JButton(texto);
        boton.setFont(NEGRITA);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (principal) {
            boton.setBackground(DORADO);
            boton.setForeground(PRINCIPAL);
        }

        return boton;
    }
}
