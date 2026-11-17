package com.inmortales.modelo;

public class HechiceroVacio extends Combatiente {

    private static final double MULTIPLICADOR_COSTO = 0.75;

    public HechiceroVacio(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected double multiplicadorDanio() {
        return 1.0;
    }

    @Override
    protected double multiplicadorDanioRecibido() {
        return 1.0;
    }

    @Override
    protected double multiplicadorCostoTecnicas() {
        return MULTIPLICADOR_COSTO;
    }

}
