package com.inmortales.combate.estrategia;

import java.util.Objects;

import com.inmortales.combate.RegistroTecnicasCombate;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.Pareja;
import com.inmortales.modelo.Tecnica;
import com.inmortales.modelo.TipoEfecto;

public class EstrategiaPredeterminada implements EstrategiaCombate {

    @Override
    public AccionCombate elegirAccion(
            Combatiente combatiente,
            Pareja pareja,
            RegistroTecnicasCombate registro) {

        Objects.requireNonNull(combatiente);
        Objects.requireNonNull(pareja);
        Objects.requireNonNull(registro);

        // 1. Prioridad: recuperación
        if (combatiente.getVida() < combatiente.getVidaMaxima()) {
            for (Tecnica tecnica : combatiente.getTecnicas()) {

                if (esUtilizable(combatiente, pareja, tecnica, registro)
                        && tecnica.getEfecto().getTipo()
                            == TipoEfecto.RECUPERACION) {

                    return AccionCombate.usarTecnica(tecnica);
                }
            }
        }

        // 2. Prioridad: defensa
        if (!combatiente.tieneDefensaActiva()) {
            for (Tecnica tecnica : combatiente.getTecnicas()) {

                if (esUtilizable(combatiente, pareja, tecnica, registro)
                        && tecnica.getEfecto().getTipo()
                            == TipoEfecto.DEFENSIVO) {

                    return AccionCombate.usarTecnica(tecnica);
                }
            }
        }

        // 3. Prioridad: técnica ofensiva con más usos restantes
        Tecnica mejorTecnica = null;
        int mayorCantidadUsos = -1;

        for (Tecnica tecnica : combatiente.getTecnicas()) {

            if (esUtilizable(combatiente, pareja, tecnica, registro)
                    && tecnica.getEfecto().getTipo()
                        == TipoEfecto.OFENSIVO) {

                int usos = combatiente.getUsosRestantes(tecnica);

                if (usos > mayorCantidadUsos) {
                    mayorCantidadUsos = usos;
                    mejorTecnica = tecnica;
                }
            }
        }

        if (mejorTecnica != null) {
            return AccionCombate.usarTecnica(mejorTecnica);
        }

        // 4. Si no puede usar técnicas, realiza ataque básico
        return AccionCombate.ataqueBasico();
    }

    private boolean esUtilizable(
            Combatiente combatiente,
            Pareja pareja,
            Tecnica tecnica,
            RegistroTecnicasCombate registro) {

        if (!combatiente.puedeUsarTecnica(tecnica)) {
            return false;
        }

        Combatiente companero = pareja.getCombatientes().stream()
            .filter(c -> !c.equals(combatiente))
            .findFirst()
            .orElseThrow();

        // Regla del compañero
        if (companero.estaEnPie()
                && companero.getUsosRestantes(tecnica) > 0
                && registro.utilizoTecnica(companero, tecnica)) {

            return false;
        }

        return true;
    }
}