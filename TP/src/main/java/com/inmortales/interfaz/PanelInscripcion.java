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

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("INSCRIPCIÓN DE PAREJAS");
        titulo.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 26));

        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        encabezado.add(titulo);
        encabezado.add(new JLabel("Cantidad de parejas:"));

        cantidadParejas = new JComboBox<>(new Integer[]{4, 8});
        encabezado.add(cantidadParejas);

        add(encabezado, BorderLayout.NORTH);

        panelCombatientes = new JPanel();
        panelCombatientes.setLayout(
            new BoxLayout(panelCombatientes, BoxLayout.Y_AXIS)
        );

        JScrollPane scroll = new JScrollPane(panelCombatientes);
        add(scroll, BorderLayout.CENTER);

        JButton confirmar = new JButton("Confirmar inscripciones");
        confirmar.addActionListener(e -> confirmarInscripciones());

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

            JPanel panelPareja = new JPanel(new GridLayout(0, 1, 5, 5));
            panelPareja.setBorder(BorderFactory.createTitledBorder(
                "Pareja " + i
            ));

            FormularioCombatiente primero = new FormularioCombatiente();
            FormularioCombatiente segundo = new FormularioCombatiente();

            panelPareja.add(new JLabel("Primer combatiente"));
            panelPareja.add(primero.panel);

            panelPareja.add(new JLabel("Segundo combatiente"));
            panelPareja.add(segundo.panel);

            formularios.add(primero);
            formularios.add(segundo);

            panelCombatientes.add(panelPareja);
        }

        panelCombatientes.revalidate();
        panelCombatientes.repaint();
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

            panel = new JPanel(new GridLayout(1, 3, 5, 5));

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
