package com.inmortales.modelo;

public class MonjeCuerpoCelestial extends Combatiente {

    private static final double MULTIPLICADOR_DEFENSA = 0.75;

    public MonjeCuerpoCelestial(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected double multiplicadorDanio() {
        return 1.0;
    }

    @Override
    protected double multiplicadorDanioRecibido() {
        return MULTIPLICADOR_DEFENSA;
    }

    @Override
    protected double multiplicadorCostoTecnicas() {
        return 1.0;
    }

}
