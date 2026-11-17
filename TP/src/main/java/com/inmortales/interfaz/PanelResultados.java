
package com.inmortales.interfaz;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.inmortales.eventos.ResultadoPareja;
import com.inmortales.modelo.Pareja;

public class PanelResultados extends JPanel {

    private static final long serialVersionUID = 1L;

    public PanelResultados(
        SesionTorneo sesion,
        Pareja campeona,
        Runnable volverInicio
    ) {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Encabezado
        JLabel titulo = new JLabel(
            "RESULTADOS DEL TORNEO",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Serif", Font.BOLD, 30));

        add(titulo, BorderLayout.NORTH);

        // Información del campeón
        JPanel panelCentral = new JPanel(new BorderLayout(10, 20));

        JLabel etiquetaCampeon = new JLabel(
            "¡La pareja " + campeona.getId()
                + " se consagra campeona!",
            SwingConstants.CENTER
        );

        etiquetaCampeon.setFont(
            new Font("Serif", Font.BOLD, 23)
        );

        panelCentral.add(etiquetaCampeon, BorderLayout.NORTH);

        // Tabla de posiciones
        String[] columnas = {
            "Posición", "Pareja", "Última ronda", "Vida final"
        };

        DefaultTableModel modelo = new DefaultTableModel(
            columnas, 0
        ) {
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

            modelo.addRow(new Object[]{
                i + 1,
                "Pareja " + resultado.getIdPareja(),
                resultado.getUltimaRonda(),
                resultado.getVidaFinal()
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 14)
        );

        JScrollPane scroll = new JScrollPane(tabla);
        panelCentral.add(scroll, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // Botón para volver al inicio
        JButton botonVolver = new JButton("Volver al inicio");
        botonVolver.addActionListener(e -> volverInicio.run());

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pie.add(botonVolver);

        add(pie, BorderLayout.SOUTH);
    }
}
