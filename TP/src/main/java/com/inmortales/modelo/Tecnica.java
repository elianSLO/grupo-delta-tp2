package com.inmortales.modelo;

public class Tecnica {

    private final String nombre;
    private final int costoEnergia;
    private final EfectoTecnica efecto;

    public Tecnica(String nombre, int costoEnergia, EfectoTecnica efecto) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                "El nombre de la técnica no puede estar vacío."
            );
        }

        if (costoEnergia < 0) {
            throw new IllegalArgumentException(
                "El costo de energía no puede ser negativo."
            );
        }

        if (efecto == null) {
            throw new IllegalArgumentException(
                "La técnica debe tener un efecto."
            );
        }

        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
        this.efecto = efecto;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }

    public EfectoTecnica getEfecto() {
        return efecto;
    }

    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        efecto.aplicar(ejecutor, objetivo);
    }

}
