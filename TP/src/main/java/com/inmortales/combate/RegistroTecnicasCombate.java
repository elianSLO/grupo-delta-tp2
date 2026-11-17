package com.inmortales.combate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.Tecnica;

public class RegistroTecnicasCombate {

    private final Map<Combatiente, Set<Tecnica>> tecnicasUtilizadas;

    public RegistroTecnicasCombate() {
        tecnicasUtilizadas = new HashMap<>();
    }

    public void registrarUso(Combatiente combatiente, Tecnica tecnica) {
        tecnicasUtilizadas
            .computeIfAbsent(combatiente, c -> new HashSet<>())
            .add(tecnica);
    }

    public boolean utilizoTecnica(
            Combatiente combatiente,
            Tecnica tecnica) {

        return tecnicasUtilizadas
            .getOrDefault(combatiente, Set.of())
            .contains(tecnica);
    }
}