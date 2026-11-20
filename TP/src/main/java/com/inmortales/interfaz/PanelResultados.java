package com.inmortales.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.inmortales.eventos.ResultadoPareja;
import com.inmortales.modelo.Pareja;

public class PanelResultados extends JPanel {

    private static final long serialVersionUID = 1L;

    public PanelResultados(
        SesionTorneo sesion,
        Pareja campeona,
        Runnable volverInicio
    ) {
        setLayout(new BorderLayout());
        setBackground(Estilo.FONDO);

        // Encabezado
        add(Estilo.encabezado(
            "Resultados del torneo",
            "Tabla de posiciones armada por el Cronista de la Secta"
        ), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 18));
        cuerpo.setBackground(Estilo.FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(22, 30, 10, 30));

        // Tarjeta de la pareja campeona
        cuerpo.add(crearTarjetaCampeona(campeona), BorderLayout.NORTH);

        // Tabla de posiciones
        String[] columnas = {
            "Puesto", "Pareja", "Última ronda", "Vida final", "Integrantes (vida final)"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        List<ResultadoPareja> posiciones =
            sesion.getCronista().obtenerPosiciones();

        for (int i = 0; i < posiciones.size(); i++) {

            ResultadoPareja resultado = posiciones.get(i);

            String integrantes = resultado.getIntegrantes().stream()
                .map(v -> v.nombre() + " " + v.vida())
                .collect(Collectors.joining(" + "));

            modelo.addRow(new Object[]{
                (i + 1) + "°",
                "Pareja " + resultado.getIdPareja(),
                resultado.getUltimaRonda(),
                resultado.getVidaFinal(),
                integrantes
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(34);
        tabla.setFont(Estilo.NORMAL);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(Estilo.BORDE);
        tabla.setFillsViewportHeight(true);

        // Columnas numéricas centradas y la última más ancha
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int columna = 0; columna < 4; columna++) {
            tabla.getColumnModel().getColumn(columna).setCellRenderer(centrado);
            tabla.getColumnModel().getColumn(columna).setPreferredWidth(90);
        }
        tabla.getColumnModel().getColumn(4).setPreferredWidth(380);

        JTableHeader cabecera = tabla.getTableHeader();
        cabecera.setFont(Estilo.NEGRITA);
        cabecera.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Estilo.BORDE));
        cuerpo.add(scroll, BorderLayout.CENTER);

        add(cuerpo, BorderLayout.CENTER);

        // Botón para volver al inicio
        JButton botonVolver = Estilo.boton("Volver al inicio", false);
        botonVolver.addActionListener(e -> volverInicio.run());

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        pie.setBackground(Estilo.FONDO);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 20, 6, 20));
        pie.add(botonVolver);

        add(pie, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaCampeona(Pareja campeona) {

        JPanel tarjeta = new JPanel(new BorderLayout(0, 6));
        tarjeta.setBackground(new Color(0xFFF6D6));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilo.DORADO, 2),
            BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        JLabel titulo = new JLabel(
            "¡La pareja " + campeona.getId() + " se consagra campeona!",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(Estilo.PRINCIPAL);

        String nombres = campeona.getCombatientes().stream()
            .map(c -> c.getNombre())
            .collect(Collectors.joining(" y "));

        JLabel santos = new JLabel(
            nombres + " son los Dos Santos Supremos",
            SwingConstants.CENTER
        );
        santos.setFont(Estilo.SUBTITULO);
        santos.setForeground(Estilo.TEXTO_SUAVE);

        tarjeta.add(titulo, BorderLayout.NORTH);
        tarjeta.add(santos, BorderLayout.SOUTH);

        return tarjeta;
    }
}
