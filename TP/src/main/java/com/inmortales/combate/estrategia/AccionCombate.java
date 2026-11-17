package com.inmortales.combate.estrategia;

import com.inmortales.modelo.Tecnica;

public class AccionCombate {

    private final Tecnica tecnica;

    private AccionCombate(Tecnica tecnica) {
        this.tecnica = tecnica;
    }

    public static AccionCombate ataqueBasico() {
        return new AccionCombate(null);
    }

    public static AccionCombate usarTecnica(Tecnica tecnica) {
        if (tecnica == null) {
            throw new IllegalArgumentException(
                "La técnica no puede ser nula."
            );
        }

        return new AccionCombate(tecnica);
    }

    public boolean esAtaqueBasico() {
        return tecnica == null;
    }

    public Tecnica getTecnica() {
        return tecnica;
    }
}