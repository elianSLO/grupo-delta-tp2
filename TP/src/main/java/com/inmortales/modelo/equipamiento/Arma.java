package com.inmortales.modelo.equipamiento;

public class Arma extends Equipamiento {

    private final int bonusDanio;

    public Arma(String nombre, int bonusDanio) {
        super(nombre, CategoriaEquipamiento.ARMA);

        if (bonusDanio < 0) {
            throw new IllegalArgumentException(
                "El bonus de daño no puede ser negativo."
            );
        }

        this.bonusDanio = bonusDanio;
    }

    @Override
    public int modificarDanio() {
        return bonusDanio;
    }

}
