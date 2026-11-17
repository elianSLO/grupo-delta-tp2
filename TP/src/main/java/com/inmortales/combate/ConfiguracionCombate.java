package com.inmortales.combate;

public class ConfiguracionCombate {

    private final int maxAsaltos;
    private final int recuperacionEnergia;

    public ConfiguracionCombate(int maxAsaltos, int recuperacionEnergia) {
        if (maxAsaltos <= 0 || recuperacionEnergia < 0) {
            throw new IllegalArgumentException(
                "Configuración de combate inválida."
            );
        }

        this.maxAsaltos = maxAsaltos;
        this.recuperacionEnergia = recuperacionEnergia;
    }

    public int getMaxAsaltos() {
        return maxAsaltos;
    }

    public int getRecuperacionEnergia() {
        return recuperacionEnergia;
    }
}