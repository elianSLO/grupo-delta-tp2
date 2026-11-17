
package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.inmortales.modelo.equipamiento.Arma;
import com.inmortales.modelo.equipamiento.Armadura;
import com.inmortales.modelo.equipamiento.Anillo;
import com.inmortales.modelo.equipamiento.Sello;

class CombatienteRecursosTest {

    @Test
    void recuperarVidaNoDebeSuperarLaVidaMaxima() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        combatiente.recibirDanio(30);

        // Act
        combatiente.recuperarVida(100);

        // Assert
        assertEquals(100, combatiente.getVida());
    }

    @Test
    void recuperarEnergiaNoDebeSuperarLaEnergiaMaxima() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        combatiente.perderEnergia(20);

        // Act
        combatiente.recuperarEnergia(100);

        // Assert
        assertEquals(40, combatiente.getEnergia());
    }

    @Test
    void perderEnergiaNoDebeDejarEnergiaNegativa() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act
        combatiente.perderEnergia(100);

        // Assert
        assertEquals(0, combatiente.getEnergia());
    }

    @Test
    void recibirDanioNoDebeDejarVidaNegativa() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act
        combatiente.recibirDanio(500);

        // Assert
        assertEquals(0, combatiente.getVida());
    }

    @Test
    void noPermiteEquiparMasDeTresObjetos() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act
        boolean primero = combatiente.equipar(new Arma("Espada", 8));
        boolean segundo = combatiente.equipar(new Armadura("Armadura", 5));
        boolean tercero = combatiente.equipar(new Anillo("Anillo", 15));
        boolean cuarto = combatiente.equipar(new Sello("Sello", 10));

        // Assert
        assertTrue(primero);
        assertTrue(segundo);
        assertTrue(tercero);
        assertFalse(cuarto);
    }

    @Test
    void noPermiteEquiparDosObjetosDeLaMismaCategoria() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act
        boolean primero = combatiente.equipar(new Arma("Espada", 8));
        boolean segundo = combatiente.equipar(new Arma("Lanza", 10));

        // Assert
        assertTrue(primero);
        assertFalse(segundo);
    }

    @Test
    void noPermiteEquiparUnObjetoNulo() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act
        boolean resultado = combatiente.equipar(null);

        // Assert
        assertFalse(resultado);
    }
}
