package com.inmortales.modelo;

import java.util.Objects;

public class EfectoDanioYEnergia implements EfectoTecnica {

    private final int danioBase;
    private final int energiaDrenada;

    public EfectoDanioYEnergia(int danioBase, int energiaDrenada) {
        if (danioBase < 0 || energiaDrenada < 0) {
            throw new IllegalArgumentException(
                "Los valores no pueden ser negativos."
            );
        }

        this.danioBase = danioBase;
        this.energiaDrenada = energiaDrenada;
    }

    @Override
    public void aplicar(Combatiente ejecutor, Combatiente objetivo) {
        Objects.requireNonNull(ejecutor);
        Objects.requireNonNull(objetivo);

        int danio = ejecutor.calcularDanio(danioBase);

        objetivo.recibirDanio(danio);
        objetivo.perderEnergia(energiaDrenada);
    }
    
    @Override
    public TipoEfecto getTipo() {
        return TipoEfecto.OFENSIVO;
    }
}