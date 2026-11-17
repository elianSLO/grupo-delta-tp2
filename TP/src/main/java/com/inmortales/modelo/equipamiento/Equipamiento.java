package com.inmortales.modelo.equipamiento;

public abstract class Equipamiento {

    private final String nombre;
    private final CategoriaEquipamiento categoria;

    public Equipamiento(String nombre, CategoriaEquipamiento categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                "El nombre del equipamiento no puede estar vacío."
            );
        }

        if (categoria == null) {
            throw new IllegalArgumentException(
                "La categoría no puede ser nula."
            );
        }

        this.nombre = nombre;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaEquipamiento getCategoria() {
        return categoria;
    }

    public int modificarDanio() {
        return 0;
    }

    public int modificarDefensa() {
        return 0;
    }

    public int modificarVidaMaxima() {
        return 0;
    }

    public int modificarEnergiaMaxima() {
        return 0;
    }

}
