package com.inmortales.interfaz;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.inmortales.eventos.Evento;
import com.inmortales.eventos.ObservadorEventos;
import com.inmortales.eventos.TipoEvento;

public class ObservadorInterfaz implements ObservadorEventos {

    private final JTextArea areaEventos;
    private final List<List<String>> eventosPorCombate;

    private int indiceCombateActual;

    public ObservadorInterfaz(JTextArea areaEventos) {
        this.areaEventos = areaEventos;
        this.eventosPorCombate = new ArrayList<>();
        this.indiceCombateActual = -1;
    }

    @Override
    public synchronized void actualizar(Evento evento) {

        // Cada INICIO_COMBATE crea un nuevo grupo de eventos.
        if (evento.getTipo() == TipoEvento.INICIO_COMBATE) {
            eventosPorCombate.add(new ArrayList<>());
            indiceCombateActual = eventosPorCombate.size() - 1;
        }

        // Ignoramos los eventos que no pertenecen a un combate.
        if (indiceCombateActual < 0) {
            return;
        }

        List<String> eventos =
            eventosPorCombate.get(indiceCombateActual);

        eventos.add(evento.getDescripcion());

        int indiceActual = indiceCombateActual;

        SwingUtilities.invokeLater(() -> {
            synchronized (ObservadorInterfaz.this) {
                // Actualizamos la pantalla solamente si sigue seleccionado
                // el combate al que pertenece el evento.
                if (indiceCombateActual == indiceActual) {
                    mostrarCombate(indiceActual);
                }
            }
        });
    }

    public synchronized void mostrarCombate(int indice) {

        if (indice < 0 || indice >= eventosPorCombate.size()) {
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (String evento : eventosPorCombate.get(indice)) {
            texto.append(evento).append("\n");
        }

        areaEventos.setText(texto.toString());
        areaEventos.setCaretPosition(0);
    }

    public synchronized int getCantidadCombates() {
        return eventosPorCombate.size();
    }

    public synchronized int getIndiceCombateActual() {
        return indiceCombateActual;
    }
}
