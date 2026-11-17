package com.inmortales.eventos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class GestorEventosTest {

    private Evento crearEvento() {
        return new Evento(
            TipoEvento.INICIO_TORNEO,
            "Comienza el torneo.",
            0, 0,
            null, null,
            null, null,
            null, null,
            null
        );
    }

    @Test
    void suscribeUnObservador() {
        GestorEventos gestor = new GestorEventos();
        AtomicInteger notificaciones = new AtomicInteger(0);

        ObservadorEventos observador =
            evento -> notificaciones.incrementAndGet();

        gestor.suscribir(observador);
        gestor.publicar(crearEvento());

        assertEquals(1, notificaciones.get());
    }

    @Test
    void notificaATodosLosObservadores() {
        GestorEventos gestor = new GestorEventos();

        AtomicInteger contador1 = new AtomicInteger(0);
        AtomicInteger contador2 = new AtomicInteger(0);

        gestor.suscribir(evento -> contador1.incrementAndGet());
        gestor.suscribir(evento -> contador2.incrementAndGet());

        gestor.publicar(crearEvento());

        assertEquals(1, contador1.get());
        assertEquals(1, contador2.get());
    }

    @Test
    void noSuscribeDosVecesElMismoObservador() {
        GestorEventos gestor = new GestorEventos();
        AtomicInteger contador = new AtomicInteger(0);

        ObservadorEventos observador =
            evento -> contador.incrementAndGet();

        gestor.suscribir(observador);
        gestor.suscribir(observador);

        gestor.publicar(crearEvento());

        assertEquals(1, contador.get());
    }

    @Test
    void desuscribeUnObservador() {
        GestorEventos gestor = new GestorEventos();
        AtomicInteger contador = new AtomicInteger(0);

        ObservadorEventos observador =
            evento -> contador.incrementAndGet();

        gestor.suscribir(observador);
        gestor.desuscribir(observador);

        gestor.publicar(crearEvento());

        assertEquals(0, contador.get());
    }

    @Test
    void noNotificaAUnObservadorDesuscripto() {
        GestorEventos gestor = new GestorEventos();

        AtomicInteger contador1 = new AtomicInteger(0);
        AtomicInteger contador2 = new AtomicInteger(0);

        ObservadorEventos observador1 =
            evento -> contador1.incrementAndGet();

        ObservadorEventos observador2 =
            evento -> contador2.incrementAndGet();

        gestor.suscribir(observador1);
        gestor.suscribir(observador2);
        gestor.desuscribir(observador1);

        gestor.publicar(crearEvento());

        assertEquals(0, contador1.get());
        assertEquals(1, contador2.get());
    }

    @Test
    void noPermiteSuscribirUnObservadorNulo() {
        GestorEventos gestor = new GestorEventos();

        assertThrows(
            NullPointerException.class,
            () -> gestor.suscribir(null)
        );
    }

    @Test
    void noPermitePublicarUnEventoNulo() {
        GestorEventos gestor = new GestorEventos();

        assertThrows(
            NullPointerException.class,
            () -> gestor.publicar(null)
        );
    }

    @Test
    void permiteDesuscribirUnObservadorNoRegistrado() {
        GestorEventos gestor = new GestorEventos();

        assertDoesNotThrow(
            () -> gestor.desuscribir(evento -> {})
        );
    }
}
