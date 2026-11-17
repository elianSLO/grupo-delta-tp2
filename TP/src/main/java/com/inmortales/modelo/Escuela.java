package com.inmortales.modelo;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.inmortales.modelo.equipamiento.CategoriaEquipamiento;
import com.inmortales.modelo.equipamiento.Equipamiento;

public class Escuela {

    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientosIniciales;

    public Escuela(
            String nombre,
            List<Tecnica> tecnicasIniciales,
            List<Equipamiento> equipamientosIniciales) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                "El nombre de la escuela no puede estar vacío."
            );
        }

        Objects.requireNonNull(tecnicasIniciales);
        Objects.requireNonNull(equipamientosIniciales);

        if (equipamientosIniciales.size() > 3) {
            throw new IllegalArgumentException(
                "Una escuela no puede asignar más de tres equipamientos."
            );
        }

        Set<CategoriaEquipamiento> categorias = new HashSet<>();

        for (Equipamiento equipo : equipamientosIniciales) {

            Objects.requireNonNull(equipo);

            if (!categorias.add(equipo.getCategoria())) {
                throw new IllegalArgumentException(
                    "La escuela no puede asignar dos equipamientos "
                    + "de la misma categoría."
                );
            }
        }

        this.nombre = nombre;
        this.tecnicasIniciales = List.copyOf(tecnicasIniciales);
        this.equipamientosIniciales = List.copyOf(equipamientosIniciales);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tecnica> getTecnicasIniciales() {
        return tecnicasIniciales;
    }

    public List<Equipamiento> getEquipamientosIniciales() {
        return equipamientosIniciales;
    }

    public void preparar(Combatiente combatiente) {

        Objects.requireNonNull(combatiente);

        for (Tecnica tecnica : tecnicasIniciales) {
            combatiente.aprenderTecnica(tecnica);
        }

        for (Equipamiento equipo : equipamientosIniciales) {

            if (!combatiente.equipar(equipo)) {
                throw new IllegalStateException(
                    "No se pudo asignar el equipamiento: "
                    + equipo.getNombre()
                );
            }
        }
    }

}
