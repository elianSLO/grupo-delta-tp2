package com.inmortales.torneo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.inmortales.combate.Combate;
import com.inmortales.combate.ConfiguracionCombate;
import com.inmortales.combate.estrategia.EstrategiaCombate;
import com.inmortales.eventos.Evento;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.eventos.TipoEvento;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.Pareja;

public class Torneo {

    private final List<Pareja> parejasInscritas;
    private final EstrategiaCombate estrategia;
    private final ConfiguracionCombate configuracion;
    private final GestorEventos gestorEventos;
    private final Set<Combatiente> combatientesInscritos;

    private List<Pareja> participantesRonda;
    private List<Pareja> ganadorasRonda;

    private Pareja campeona;

    private boolean iniciado;
    private boolean finalizado;

    private int numeroRonda;
    private int indiceCombate;

    public Torneo(
        EstrategiaCombate estrategia,
        ConfiguracionCombate configuracion,
        GestorEventos gestorEventos
    ) {
        this.estrategia = Objects.requireNonNull(estrategia);
        this.configuracion = Objects.requireNonNull(configuracion);
        this.gestorEventos = Objects.requireNonNull(gestorEventos);

        this.parejasInscritas = new ArrayList<>();
        this.combatientesInscritos = new HashSet<>();

        this.participantesRonda = new ArrayList<>();
        this.ganadorasRonda = new ArrayList<>();

        this.iniciado = false;
        this.finalizado = false;

        this.numeroRonda = 1;
        this.indiceCombate = 0;
    }

    public void inscribirPareja(Pareja pareja) {

        Objects.requireNonNull(pareja);

        if (iniciado) {
            throw new IllegalStateException(
                "No se pueden inscribir parejas una vez iniciado el torneo."
            );
        }

        if (parejasInscritas.contains(pareja)) {
            throw new IllegalArgumentException(
                "La pareja ya está inscripta."
            );
        }

        for (Combatiente combatiente : pareja.getCombatientes()) {

            if (combatientesInscritos.contains(combatiente)) {
                throw new IllegalArgumentException(
                    "Un combatiente no puede participar en más de una pareja."
                );
            }
        }

        parejasInscritas.add(pareja);
        combatientesInscritos.addAll(pareja.getCombatientes());
    }

    // Prepara el torneo, pero no ejecuta ningún combate.
    public void preparar() {

        if (iniciado) {
            throw new IllegalStateException(
                "El torneo ya fue iniciado."
            );
        }

        validarCantidadParejas();

        iniciado = true;
        finalizado = false;

        participantesRonda = new ArrayList<>(parejasInscritas);
        ganadorasRonda = new ArrayList<>();

        numeroRonda = 1;
        indiceCombate = 0;

        publicarEvento(
            TipoEvento.INICIO_TORNEO,
            "Comienza el Torneo de los Inmortales con "
                + parejasInscritas.size() + " parejas."
        );
    }

    // Ejecuta solamente el próximo combate pendiente.
    public Pareja avanzarCombate() {

        if (!iniciado) {
            throw new IllegalStateException(
                "Primero se debe preparar el torneo."
            );
        }

        if (finalizado) {
            throw new IllegalStateException(
                "El torneo ya finalizó."
            );
        }

        int indicePareja1 = indiceCombate * 2;

        Pareja pareja1 = participantesRonda.get(indicePareja1);
        Pareja pareja2 = participantesRonda.get(indicePareja1 + 1);

        Combate combate = new Combate(
            pareja1,
            pareja2,
            estrategia,
            configuracion,
            gestorEventos,
            numeroRonda
        );

        Pareja ganadora = combate.ejecutar();

        ganadorasRonda.add(ganadora);
        indiceCombate++;

        // ¿Se terminaron los combates de esta ronda?
        if (indiceCombate == participantesRonda.size() / 2) {

            if (ganadorasRonda.size() == 1) {

                // Solo queda una pareja: es la campeona.
                campeona = ganadorasRonda.get(0);
                finalizarTorneo();

            } else {

                // Preparamos la siguiente ronda.
                participantesRonda = new ArrayList<>(ganadorasRonda);
                ganadorasRonda.clear();

                numeroRonda++;
                indiceCombate = 0;
            }
        }

        return ganadora;
    }

    private void finalizarTorneo() {

        finalizado = true;

        publicarEvento(
            TipoEvento.CAMPEON_PROCLAMADO,
            "¡La pareja " + campeona.getId()
                + " se consagra campeona del Torneo de los Inmortales!"
        );
    }

    // Conservamos el método original para la versión de consola.
    public Pareja iniciar() {

        preparar();

        while (!finalizado) {
            avanzarCombate();
        }

        return campeona;
    }

    private void validarCantidadParejas() {

        int cantidad = parejasInscritas.size();

        if (cantidad != 4 && cantidad != 8) {
            throw new IllegalStateException(
                "El torneo debe tener exactamente 4 u 8 parejas."
            );
        }
    }

    private void publicarEvento(
        TipoEvento tipo,
        String descripcion
    ) {

        Evento evento = new Evento(
            tipo,
            descripcion,
            0,
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            campeona == null ? null : campeona.getId()
        );

        gestorEventos.publicar(evento);
    }

    public List<Pareja> getParejasInscritas() {
        return List.copyOf(parejasInscritas);
    }

    public Pareja getCampeona() {
        return campeona;
    }

    public boolean estaIniciado() {
        return iniciado;
    }

    public boolean estaFinalizado() {
        return finalizado;
    }

    public int getNumeroRonda() {
        return numeroRonda;
    }

    public int getNumeroCombateActual() {
        return indiceCombate + 1;
    }

    public int getCantidadCombatesRonda() {
        return participantesRonda.size() / 2;
    }
}
