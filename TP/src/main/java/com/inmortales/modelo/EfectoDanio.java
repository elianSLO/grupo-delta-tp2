package com.inmortales.modelo;

import java.util.Objects;

public class EfectoDanio implements EfectoTecnica {

    private final int danioBase;

    public EfectoDanio(int danioBase) {
        if (danioBase < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        }

        this.danioBase = danioBase;
    }

    @Override
    public void aplicar(Combatiente ejecutor, Combatiente objetivo) {
        Objects.requireNonNull(ejecutor);
        Objects.requireNonNull(objetivo);

        int danio = ejecutor.calcularDanio(danioBase);
        objetivo.recibirDanio(danio);
    }
    
    @Override
    public TipoEfecto getTipo() {
        return TipoEfecto.OFENSIVO;
    }
}