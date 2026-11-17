package com.inmortales.fabricacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.CultivadorEspada;
import com.inmortales.modelo.Escuela;
import com.inmortales.modelo.HechiceroVacio;
import com.inmortales.modelo.MonjeCuerpoCelestial;

public class CreadorCombatientes {

    private int siguienteId;

    private final Map<String, ConstructorCombatiente> constructores;

    public CreadorCombatientes() {

        this.siguienteId = 1;
        this.constructores = new HashMap<>();

        registrarTipo("Espada", CultivadorEspada::new);
        registrarTipo("Monje", MonjeCuerpoCelestial::new);
        registrarTipo("Hechicero", HechiceroVacio::new);
    }

    public void registrarTipo(
            String tipo,
            ConstructorCombatiente constructor) {

        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException(
                "El tipo no puede estar vacío."
            );
        }

        Objects.requireNonNull(constructor);

        String clave = tipo.trim().toLowerCase();

        if (constructores.containsKey(clave)) {
            throw new IllegalArgumentException(
                "El tipo ya está registrado: " + tipo
            );
        }

        constructores.put(clave, constructor);
    }

    public Combatiente crear(
            String tipo,
            String nombre,
            Escuela escuela) {

        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException(
                "Debe especificarse un tipo."
            );
        }

        Objects.requireNonNull(escuela);

        String clave = tipo.trim().toLowerCase();

        ConstructorCombatiente constructor = constructores.get(clave);

        if (constructor == null) {
            throw new IllegalArgumentException(
                "Tipo desconocido: " + tipo
            );
        }

        Combatiente combatiente = constructor.crear(
            siguienteId,
            nombre
        );

        escuela.preparar(combatiente);

        siguienteId++;

        return combatiente;
    }

}
