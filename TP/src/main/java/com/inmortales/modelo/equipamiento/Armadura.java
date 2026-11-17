package com.inmortales.modelo.equipamiento;

public class Armadura extends Equipamiento {

    private final int bonusDefensa;

    public Armadura(String nombre, int bonusDefensa) {
        super(nombre, CategoriaEquipamiento.ARMADURA);

        if (bonusDefensa < 0) {
            throw new IllegalArgumentException(
                "El bonus de defensa no puede ser negativo."
            );
        }

        this.bonusDefensa = bonusDefensa;
    }

    @Override
    public int modificarDefensa() {
        return bonusDefensa;
    }

}
