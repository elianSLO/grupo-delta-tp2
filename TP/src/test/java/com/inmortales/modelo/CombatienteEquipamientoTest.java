
package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.inmortales.modelo.equipamiento.Anillo;
import com.inmortales.modelo.equipamiento.Sello;

class CombatienteEquipamientoTest {

    @Test
    void equiparSelloAumentaLaVidaMaxima() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Sello sello = new Sello("Sello vital", 30);

        // Act
        combatiente.equipar(sello);

        // Assert
        assertEquals(130, combatiente.getVidaMaxima());
    }

    @Test
    void equiparAnilloAumentaLaEnergiaMaxima() {
        // Arrange
        Combatiente combatiente = new HechiceroVacio(2, "Hechicero");
        Anillo anillo = new Anillo("Anillo mágico", 15);

        // Act
        combatiente.equipar(anillo);

        // Assert
        assertEquals(55, combatiente.getEnergiaMaxima());
    }

    @Test
    void quitarSelloReduceLaVidaMaximaYAjustaLaVidaActual() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Sello sello = new Sello("Sello vital", 30);

        combatiente.equipar(sello);
        combatiente.reiniciarParaCombate();
        combatiente.recibirDanio(10);

        assertEquals(120, combatiente.getVida());

        // Act
        boolean resultado = combatiente.quitarEquipamiento(sello);

        // Assert
        assertTrue(resultado);
        assertEquals(100, combatiente.getVidaMaxima());
        assertEquals(100, combatiente.getVida());
    }

    @Test
    void quitarAnilloReduceLaEnergiaMaximaYAjustaLaEnergiaActual() {
        // Arrange
        Combatiente combatiente = new HechiceroVacio(2, "Hechicero");
        Anillo anillo = new Anillo("Anillo mágico", 15);

        combatiente.equipar(anillo);
        combatiente.reiniciarParaCombate();
        combatiente.perderEnergia(5);

        assertEquals(50, combatiente.getEnergia());

        // Act
        boolean resultado = combatiente.quitarEquipamiento(anillo);

        // Assert
        assertTrue(resultado);
        assertEquals(40, combatiente.getEnergiaMaxima());
        assertEquals(40, combatiente.getEnergia());
    }

    @Test
    void quitarEquipamientoInexistenteNoModificaAlCombatiente() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Sello sello = new Sello("Sello vital", 30);

        combatiente.equipar(sello);
        combatiente.reiniciarParaCombate();

        // Act
        boolean resultado = combatiente.quitarEquipamiento(
            new Sello("Otro sello", 20)
        );

        // Assert
        assertFalse(resultado);
        assertEquals(130, combatiente.getVidaMaxima());
        assertEquals(130, combatiente.getVida());
    }
}
