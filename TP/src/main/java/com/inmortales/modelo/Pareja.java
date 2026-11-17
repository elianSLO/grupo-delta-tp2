package com.inmortales.modelo;

import java.util.List;
import java.util.Objects;

public class Pareja {

    private final int id;
    private final Combatiente combatiente1;
    private final Combatiente combatiente2;

    public Pareja(
            int id,
            Combatiente combatiente1,
            Combatiente combatiente2) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "El identificador de la pareja debe ser positivo."
            );
        }

        Objects.requireNonNull(combatiente1);
        Objects.requireNonNull(combatiente2);

        if (combatiente1.equals(combatiente2)) {
            throw new IllegalArgumentException(
                "Una pareja debe tener dos combatientes diferentes."
            );
        }

        this.id = id;
        this.combatiente1 = combatiente1;
        this.combatiente2 = combatiente2;
    }

    public int getId() {
        return id;
    }

    public Combatiente getCombatiente1() {
        return combatiente1;
    }

    public Combatiente getCombatiente2() {
        return combatiente2;
    }

    public List<Combatiente> getCombatientes() {
        return List.of(combatiente1, combatiente2);
    }

    public boolean estaDerrotada() {
        return !combatiente1.estaEnPie()
            && !combatiente2.estaEnPie();
    }

    public int getVidaTotal() {

        int vida1 = combatiente1.estaEnPie()
            ? combatiente1.getVida()
            : 0;

        int vida2 = combatiente2.estaEnPie()
            ? combatiente2.getVida()
            : 0;

        return vida1 + vida2;
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Pareja otra)) {
            return false;
        }

        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
