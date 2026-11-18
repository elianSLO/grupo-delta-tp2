package com.inmortales.modelo;

import java.util.Objects;

public class EfectoVeneno implements EfectoTecnica {

    private final int danioPorTurno;
    private final int duracion;

    public EfectoVeneno(int danioPorTurno, int duracion) {
        if (danioPorTurno <= 0 || duracion <= 0) {
            throw new IllegalArgumentException();
        }

        this.danioPorTurno = danioPorTurno;
        this.duracion = duracion;
    }

    @Override
    public void aplicar(
            Combatiente ejecutor,
            Combatiente objetivo) {

        Objects.requireNonNull(ejecutor);
        Objects.requireNonNull(objetivo);

        objetivo.aplicarVeneno(danioPorTurno, duracion);
    }

    @Override
    public TipoEfecto getTipo() {
        return TipoEfecto.OFENSIVO;
    }
}
