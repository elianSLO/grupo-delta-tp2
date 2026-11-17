package com.inmortales.combate.estrategia;

import com.inmortales.combate.RegistroTecnicasCombate;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.Pareja;

public interface EstrategiaCombate {

    AccionCombate elegirAccion(
        Combatiente combatiente,
        Pareja pareja,
        RegistroTecnicasCombate registro
    );
}