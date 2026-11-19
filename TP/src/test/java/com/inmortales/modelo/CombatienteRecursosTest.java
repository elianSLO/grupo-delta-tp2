
package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.inmortales.modelo.equipamiento.Arma;
import com.inmortales.modelo.equipamiento.Armadura;
import com.inmortales.modelo.equipamiento.Equipamiento;
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
    
 // Regla (sección 4): equipar un cuarto objeto o repetir una categoría
    // se rechaza SIN cambios en el combatiente.
    @Test
    void equipamientoRechazadoNoModificaAlCombatiente() {

        // Caso A: ya lleva tres objetos e intenta equipar un cuarto
        Combatiente conTres = new CultivadorEspada(1, "Guerrero");
        conTres.equipar(new Arma("Espada", 8));
        conTres.equipar(new Armadura("Armadura", 5));
        conTres.equipar(new Anillo("Anillo", 15));

        List<Equipamiento> equiposAntes = conTres.getEquipamientos();
        int vidaMaximaAntes = conTres.getVidaMaxima();
        int energiaMaximaAntes = conTres.getEnergiaMaxima();
        int danioAntes = conTres.calcularDanio(10);

        boolean cuarto = conTres.equipar(new Sello("Sello del Fénix", 20));

        assertFalse(cuarto);
        assertEquals(equiposAntes, conTres.getEquipamientos());
        assertEquals(vidaMaximaAntes, conTres.getVidaMaxima());
        assertEquals(energiaMaximaAntes, conTres.getEnergiaMaxima());
        assertEquals(danioAntes, conTres.calcularDanio(10));

        // Caso B: lleva un arma e intenta equipar otra arma
        Combatiente conArma = new CultivadorEspada(2, "Guerrero 2");
        conArma.equipar(new Arma("Espada", 8));

        List<Equipamiento> equiposAntesB = conArma.getEquipamientos();
        int danioAntesB = conArma.calcularDanio(10);

        boolean repetida = conArma.equipar(new Arma("Lanza", 10));

        assertFalse(repetida);
        assertEquals(equiposAntesB, conArma.getEquipamientos());
        assertEquals(danioAntesB, conArma.calcularDanio(10));
    }
}
