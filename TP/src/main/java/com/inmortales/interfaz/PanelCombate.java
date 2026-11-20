package com.inmortales.interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.SwingWorker;

import com.inmortales.modelo.Pareja;
import com.inmortales.torneo.Torneo;

public class PanelCombate extends JPanel {

    private static final long serialVersionUID = 1L;

    private final SesionTorneo sesion;
    private final JEditorPane areaEventos;
    private final JLabel etiquetaEstado;

    private final JButton botonAnterior;
    private final JButton botonSiguiente;
    private final JButton botonResultados;

    private final ObservadorInterfaz observador;

    private Pareja campeona;

    private boolean torneoPreparado;
    private boolean torneoFinalizado;

    private int indiceCombateMostrado;

    public PanelCombate(
        SesionTorneo sesion,
        Consumer<Pareja> alFinalizar
    ) {
        this.sesion = sesion;

        this.torneoPreparado = false;
        this.torneoFinalizado = false;
        this.indiceCombateMostrado = -1;

        setLayout(new BorderLayout());
        setBackground(Estilo.FONDO);

        // Encabezado
        add(Estilo.encabezado(
            "Desarrollo del torneo",
            "Cada combate se juega al apretar el botón; con Anterior y"
                + " Siguiente se revisan los ya jugados."
        ), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 10));
        cuerpo.setBackground(Estilo.FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));

        etiquetaEstado = new JLabel("Preparado para comenzar el torneo");
        etiquetaEstado.setFont(Estilo.NEGRITA);
        etiquetaEstado.setForeground(Estilo.PRINCIPAL);
        cuerpo.add(etiquetaEstado, BorderLayout.NORTH);

        // Área de eventos (HTML con colores por tipo de acontecimiento)
        areaEventos = new JEditorPane("text/html", "");
        areaEventos.setEditable(false);
        areaEventos.setBackground(Estilo.TARJETA);

        JScrollPane scroll = new JScrollPane(areaEventos);
        scroll.setBorder(BorderFactory.createLineBorder(Estilo.BORDE));
        cuerpo.add(scroll, BorderLayout.CENTER);

        add(cuerpo, BorderLayout.CENTER);

        // Observador
        observador = new ObservadorInterfaz(areaEventos);
        sesion.getGestorEventos().suscribir(observador);

        // Botones de navegación
        botonAnterior = Estilo.boton("< Anterior", false);
        botonSiguiente = Estilo.boton("Iniciar torneo", true);
        botonResultados = Estilo.boton("Ver resultados", false);

        botonAnterior.addActionListener(e -> mostrarAnterior());

        botonSiguiente.addActionListener(e -> mostrarSiguiente());

        botonResultados.addActionListener(e -> {
            alFinalizar.accept(campeona);
        });

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        pie.setBackground(Estilo.FONDO);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 16, 6, 16));

        pie.add(botonAnterior);
        pie.add(botonSiguiente);
        pie.add(botonResultados);

        add(pie, BorderLayout.SOUTH);

        actualizarBotones();
    }

    private void mostrarAnterior() {

        if (indiceCombateMostrado > 0) {
            indiceCombateMostrado--;
            mostrarCombateSeleccionado();
        }
    }

    private void mostrarSiguiente() {

        int cantidad = observador.getCantidadCombates();

        // Si hay combates posteriores ya ejecutados, los mostramos.
        if (indiceCombateMostrado < cantidad - 1) {
            indiceCombateMostrado++;
            mostrarCombateSeleccionado();
            return;
        }

        // Si estamos en el último combate, ejecutamos el siguiente.
        if (!torneoFinalizado) {
            ejecutarSiguienteCombate();
        }
    }

    private void mostrarCombateSeleccionado() {

        observador.mostrarCombate(indiceCombateMostrado);

        etiquetaEstado.setText(
            "Mostrando el combate " + (indiceCombateMostrado + 1)
            + " de " + observador.getCantidadCombates() + " jugados"
        );

        actualizarBotones();
    }

    private void ejecutarSiguienteCombate() {

        botonAnterior.setEnabled(false);
        botonSiguiente.setEnabled(false);
        botonResultados.setEnabled(false);

        SwingWorker<Pareja, Void> trabajador =
            new SwingWorker<>() {

                @Override
                protected Pareja doInBackground() {

                    Torneo torneo = sesion.getTorneo();

                    if (!torneoPreparado) {
                        torneo.preparar();
                        torneoPreparado = true;
                    }

                    return torneo.avanzarCombate();
                }

                @Override
                protected void done() {

                    try {
                        get();

                        Torneo torneo = sesion.getTorneo();

                        if (torneo.estaFinalizado()) {
                            campeona = torneo.getCampeona();
                            torneoFinalizado = true;
                        }

                        // Seleccionamos automáticamente el combate recién ejecutado.
                        indiceCombateMostrado =
                            observador.getCantidadCombates() - 1;

                        mostrarCombateSeleccionado();

                        if (torneoFinalizado) {
                            etiquetaEstado.setText(
                                "¡Torneo finalizado! Campeona: Pareja "
                                + campeona.getId()
                            );
                        }

                    } catch (Exception ex) {

                        JOptionPane.showMessageDialog(
                            PanelCombate.this,
                            "No se pudo ejecutar el combate: "
                                + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );

                    } finally {
                        actualizarBotones();
                    }
                }
            };

        trabajador.execute();
    }

    private void actualizarBotones() {

        int cantidad = observador.getCantidadCombates();

        botonAnterior.setEnabled(
            cantidad > 0 && indiceCombateMostrado > 0
        );

        boolean hayCombatePosterior =
            indiceCombateMostrado < cantidad - 1;

        botonSiguiente.setEnabled(
            hayCombatePosterior || !torneoFinalizado
        );

        if (hayCombatePosterior) {
            botonSiguiente.setText("Siguiente >");
        } else if (torneoFinalizado) {
            botonSiguiente.setText("Torneo finalizado");
        } else if (cantidad == 0) {
            botonSiguiente.setText("Iniciar torneo");
        } else {
            botonSiguiente.setText("Jugar siguiente combate >");
        }

        botonResultados.setEnabled(torneoFinalizado);
    }
}
