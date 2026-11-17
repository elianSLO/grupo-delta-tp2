package com.inmortales.modelo;

import java.util.Objects;

public class EfectoDefensivo implements EfectoTecnica {

    @Override
    public void aplicar(Combatiente ejecutor, Combatiente objetivo) {
        Objects.requireNonNull(ejecutor);

        ejecutor.activarDefensa();
    }
    
    @Override
    public TipoEfecto getTipo() {
        return TipoEfecto.DEFENSIVO;
    }
}