package com.inmortales.interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.inmortales.ConfiguracionJuego;
import com.inmortales.eventos.CronistaSecta;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.eventos.HeraldoImperial;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.Pareja;
import com.inmortales.torneo.Torneo;

public class PanelInscripcion extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ConfiguracionJuego configuracion;
    private final Consumer<SesionTorneo> alConfirmar;

    private final JComboBox<Integer> cantidadParejas;
    private final JPanel panelCombatientes;
    private final List<FormularioCombatiente> formularios;

    public PanelInscripcion(
        ConfiguracionJuego configuracion,
        Consumer<SesionTorneo> alConfirmar
    ) {
        this.configuracion = configuracion;
        this.alConfirmar = alConfirmar;
        this.formularios = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(Estilo.FONDO);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(Estilo.encabezado(
            "Inscripción de parejas",
            "Cada combatiente necesita nombre, tipo y escuela."
                + " La escuela define sus técnicas y su equipamiento."
        ), BorderLayout.NORTH);

        // Barra de opciones: cantidad de parejas y ejemplo rápido
        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        opciones.setBackground(Estilo.FONDO);
        opciones.setBorder(BorderFactory.createEmptyBorder(6, 18, 0, 18));

        JLabel etiquetaCantidad = new JLabel("Cantidad de parejas:");
        etiquetaCantidad.setFont(Estilo.NEGRITA);
        opciones.add(etiquetaCantidad);

        cantidadParejas = new JComboBox<>(new Integer[]{4, 8});
        opciones.add(cantidadParejas);

        JButton ejemplo = Estilo.boton("Cargar ejemplo", false);
        ejemplo.setToolTipText("Completa las 4 parejas con los mismos combatientes del Main");
        ejemplo.addActionListener(e -> cargarEjemplo());
        opciones.add(ejemplo);

        arriba.add(opciones, BorderLayout.SOUTH);
        add(arriba, BorderLayout.NORTH);

        panelCombatientes = new JPanel();
        panelCombatientes.setLayout(
            new BoxLayout(panelCombatientes, BoxLayout.Y_AXIS)
        );
        panelCombatientes.setBackground(Estilo.FONDO);
        panelCombatientes.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        JScrollPane scroll = new JScrollPane(panelCombatientes);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        JButton confirmar = Estilo.boton("Confirmar inscripciones", true);
        confirmar.addActionListener(e -> confirmarInscripciones());

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        pie.setBackground(Estilo.FONDO);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 16, 6, 16));
        pie.add(confirmar);
        add(pie, BorderLayout.SOUTH);

        cantidadParejas.addActionListener(e -> actualizarFormularios());

        actualizarFormularios();
    }

    private void actualizarFormularios() {
        formularios.clear();
        panelCombatientes.removeAll();

        int cantidad = (Integer) cantidadParejas.getSelectedItem();

        for (int i = 1; i <= cantidad; i++) {

            JPanel panelPareja = new JPanel(new GridLayout(0, 1, 6, 6));
            panelPareja.setBackground(Estilo.TARJETA);
            panelPareja.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Estilo.BORDE),
                    "  Pareja " + i + "  ",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    Estilo.NEGRITA,
                    Estilo.PRINCIPAL
                ),
                BorderFactory.createEmptyBorder(4, 10, 10, 10)
            ));
            panelPareja.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 150));

            FormularioCombatiente primero = new FormularioCombatiente();
            FormularioCombatiente segundo = new FormularioCombatiente();

            JPanel titulos = new JPanel(new GridLayout(1, 3, 8, 8));
            titulos.setOpaque(false);
            for (String columna : new String[]{"Nombre", "Tipo", "Escuela"}) {
                JLabel etiqueta = new JLabel(columna);
                etiqueta.setForeground(Estilo.TEXTO_SUAVE);
                titulos.add(etiqueta);
            }

            panelPareja.add(titulos);
            panelPareja.add(primero.panel);
            panelPareja.add(segundo.panel);

            formularios.add(primero);
            formularios.add(segundo);

            panelCombatientes.add(panelPareja);
        }

        panelCombatientes.revalidate();
        panelCombatientes.repaint();
    }

    // Completa 4 parejas con los mismos combatientes que usa el Main,
    // para no tener que escribirlos a mano en la demostración.
    private void cargarEjemplo() {

        cantidadParejas.setSelectedItem(4);

        String espada = "Escuela de la Espada Celestial";
        String monasterio = "Monasterio del Cuerpo Dorado";
        String palacio = "Palacio del Vacío";

        String[][] ejemplo = {
            {"Kratos", "Espada", espada},
            {"Loki", "Hechicero", palacio},
            {"Goku", "Monje", monasterio},
            {"Geralt de Rivia", "Espada", espada},
            {"Doom Slayer", "Espada", espada},
            {"Elsa", "Hechicero", palacio},
            {"Gandalf", "Hechicero", palacio},
            {"John Wick", "Espada", espada}
        };

        for (int i = 0; i < ejemplo.length && i < formularios.size(); i++) {
            formularios.get(i).completar(
                ejemplo[i][0], ejemplo[i][1], ejemplo[i][2]
            );
        }
    }

    private void confirmarInscripciones() {

        try {
            GestorEventos gestor = new GestorEventos();

            HeraldoImperial heraldo = new HeraldoImperial();
            CronistaSecta cronista = new CronistaSecta();

            gestor.suscribir(cronista);

            Torneo torneo = configuracion.crearTorneo(gestor);

            for (int i = 0; i < formularios.size(); i += 2) {

                FormularioCombatiente formulario1 = formularios.get(i);
                FormularioCombatiente formulario2 = formularios.get(i + 1);

                Combatiente combatiente1 = formulario1.crearCombatiente();
                Combatiente combatiente2 = formulario2.crearCombatiente();

                int idPareja = (i / 2) + 1;

                Pareja pareja = new Pareja(
                    idPareja,
                    combatiente1,
                    combatiente2
                );

                torneo.inscribirPareja(pareja);
            }

            SesionTorneo sesion = new SesionTorneo(
                torneo, gestor, cronista, heraldo
            );

            alConfirmar.accept(sesion);

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error en la inscripción",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private class FormularioCombatiente {

        private final JPanel panel;
        private final JTextField nombre;
        private final JComboBox<String> tipo;
        private final JComboBox<String> escuela;

        private FormularioCombatiente() {

            panel = new JPanel(new GridLayout(1, 3, 8, 8));
            panel.setOpaque(false);

            nombre = new JTextField();
            tipo = new JComboBox<>(new String[]{
                "Espada", "Monje", "Hechicero"
            });

            escuela = new JComboBox<>(
                configuracion.getNombresEscuelas().toArray(new String[0])
            );

            panel.add(nombre);
            panel.add(tipo);
            panel.add(escuela);

            nombre.setToolTipText("Nombre del combatiente");
            tipo.setToolTipText("Tipo de cultivador");
            escuela.setToolTipText("Escuela");
        }

        private void completar(String nombreEjemplo, String tipoEjemplo, String escuelaEjemplo) {
            nombre.setText(nombreEjemplo);
            tipo.setSelectedItem(tipoEjemplo);
            escuela.setSelectedItem(escuelaEjemplo);
        }

        private Combatiente crearCombatiente() {

            String nombreIngresado = nombre.getText().trim();

            if (nombreIngresado.isEmpty()) {
                throw new IllegalArgumentException(
                    "Todos los combatientes deben tener un nombre."
                );
            }

            return configuracion.crearCombatiente(
                (String) tipo.getSelectedItem(),
                nombreIngresado,
                (String) escuela.getSelectedItem()
            );
        }
    }
}
