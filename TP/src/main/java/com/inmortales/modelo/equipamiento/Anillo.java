package com.inmortales.modelo.equipamiento;

public class Anillo extends Equipamiento {

    private final int bonusEnergiaMaxima;

    public Anillo(String nombre, int bonusEnergiaMaxima) {
        super(nombre, CategoriaEquipamiento.ANILLO);

        if (bonusEnergiaMaxima < 0) {
            throw new IllegalArgumentException(
                "El bonus de energía no puede ser negativo."
            );
        }

        this.bonusEnergiaMaxima = bonusEnergiaMaxima;
    }

    @Override
    public int modificarEnergiaMaxima() {
        return bonusEnergiaMaxima;
    }

}
