package com.inmortales.modelo;

import java.util.Objects;

public class EfectoRecuperacion implements EfectoTecnica {

    private final int cantidadRecuperacion;

    public EfectoRecuperacion(int cantidadRecuperacion) {
        if (cantidadRecuperacion < 0) {
            throw new IllegalArgumentException(
                "La recuperación no puede ser negativa."
            );
        }

        this.cantidadRecuperacion = cantidadRecuperacion;
    }

    @Override
    public void aplicar(Combatiente ejecutor, Combatiente objetivo) {
        Objects.requireNonNull(ejecutor);

        ejecutor.recuperarVida(cantidadRecuperacion);
    }
    
    @Override
    public TipoEfecto getTipo() {
        return TipoEfecto.RECUPERACION;
    }
}