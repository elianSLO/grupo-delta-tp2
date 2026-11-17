package com.inmortales.modelo.equipamiento;

public class Sello extends Equipamiento {

    private final int bonusVidaMaxima;

    public Sello(String nombre, int bonusVidaMaxima) {
        super(nombre, CategoriaEquipamiento.SELLO);

        if (bonusVidaMaxima < 0) {
            throw new IllegalArgumentException(
                "El bonus de vida no puede ser negativo."
            );
        }

        this.bonusVidaMaxima = bonusVidaMaxima;
    }

    @Override
    public int modificarVidaMaxima() {
        return bonusVidaMaxima;
    }

}
