package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CombatienteTecnicasTest {

    private Tecnica crearTecnica(String nombre, int costo) {
        EfectoTecnica efecto = new EfectoTecnica() {

            @Override
            public void aplicar(Combatiente ejecutor, Combatiente objetivo) {
                // Efecto vacío: solo probamos el registro del uso.
            }

            @Override
            public TipoEfecto getTipo() {
                return TipoEfecto.OFENSIVO;
            }
        };

        return new Tecnica(nombre, costo, efecto);
    }

    @Test
    void combatienteComienzaConCincoUsosDisponibles() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Corte celestial", 5);
        combatiente.aprenderTecnica(tecnica);

        // Assert
        assertEquals(5, combatiente.getUsosRestantes(tecnica));
    }

    @Test
    void aprenderDosVecesLaMismaTecnicaNoLaDuplica() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Corte celestial", 5);

        // Act
        combatiente.aprenderTecnica(tecnica);
        combatiente.aprenderTecnica(tecnica);

        // Assert
        assertEquals(1, combatiente.getTecnicas().size());
        assertEquals(5, combatiente.getUsosRestantes(tecnica));
    }

    @Test
    void registrarUsoConsumeUnUsoYLaEnergiaCorrespondiente() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Corte celestial", 5);
        combatiente.aprenderTecnica(tecnica);

        // Act
        boolean resultado = combatiente.registrarUsoTecnica(tecnica);

        // Assert
        assertTrue(resultado);
        assertEquals(4, combatiente.getUsosRestantes(tecnica));
        assertEquals(35, combatiente.getEnergia());
    }

    @Test
    void permiteUtilizarUnaTecnicaCincoVeces() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Corte celestial", 5);
        combatiente.aprenderTecnica(tecnica);

        // Act
        for (int i = 0; i < 5; i++) {
            assertTrue(combatiente.registrarUsoTecnica(tecnica));
        }

        // Assert
        assertEquals(0, combatiente.getUsosRestantes(tecnica));
        assertEquals(15, combatiente.getEnergia());
    }

    @Test
    void noPermiteUtilizarUnaTecnicaMasDeCincoVeces() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Corte celestial", 5);
        combatiente.aprenderTecnica(tecnica);

        // Act
        for (int i = 0; i < 5; i++) {
            combatiente.registrarUsoTecnica(tecnica);
        }

        boolean resultado = combatiente.registrarUsoTecnica(tecnica);

        // Assert
        assertFalse(resultado);
        assertEquals(0, combatiente.getUsosRestantes(tecnica));
        assertEquals(15, combatiente.getEnergia());
    }

    @Test
    void noPermiteUtilizarUnaTecnicaQueNoConoce() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Dominio del vacío", 5);

        // Act
        boolean resultado = combatiente.registrarUsoTecnica(tecnica);

        // Assert
        assertFalse(resultado);
        assertEquals(40, combatiente.getEnergia());
    }

    @Test
    void noPermiteUtilizarTecnicaSiNoTieneEnergiaSuficiente() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        Tecnica tecnica = crearTecnica("Dominio del vacío", 50);
        combatiente.aprenderTecnica(tecnica);

        // Act
        boolean resultado = combatiente.registrarUsoTecnica(tecnica);

        // Assert
        assertFalse(resultado);
        assertEquals(40, combatiente.getEnergia());
        assertEquals(5, combatiente.getUsosRestantes(tecnica));
    }

    @Test
    void hechiceroPagaMenosEnergiaAlUtilizarUnaTecnica() {
        // Arrange
        Combatiente hechicero = new HechiceroVacio(1, "Hechicero");
        Tecnica tecnica = crearTecnica("Dominio del vacío", 10);
        hechicero.aprenderTecnica(tecnica);

        // Act
        boolean resultado = hechicero.registrarUsoTecnica(tecnica);

        // Assert
        assertTrue(resultado);
        assertEquals(32, hechicero.getEnergia());
        assertEquals(4, hechicero.getUsosRestantes(tecnica));
    }
}
