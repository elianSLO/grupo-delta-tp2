package com.inmortales.eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GestorEventos {

    private final List<ObservadorEventos> observadores;

    public GestorEventos() {
        observadores = new ArrayList<>();
    }

    public void suscribir(ObservadorEventos observador) {
        Objects.requireNonNull(observador);

        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(ObservadorEventos observador) {
        observadores.remove(observador);
    }

    public void publicar(Evento evento) {
        Objects.requireNonNull(evento);

        for (ObservadorEventos observador : List.copyOf(observadores)) {
            observador.actualizar(evento);
        }
    }
}