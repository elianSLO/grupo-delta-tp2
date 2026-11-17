package com.inmortales.modelo;

public class CultivadorEspada extends Combatiente {

    private static final double MULTIPLICADOR_DANIO = 1.30;

    public CultivadorEspada(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected double multiplicadorDanio() {
        return MULTIPLICADOR_DANIO;
    }

    @Override
    protected double multiplicadorDanioRecibido() {
        return 1.0;
    }

    @Override
    protected double multiplicadorCostoTecnicas() {
        return 1.0;
    }

}