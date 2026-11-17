
package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class ParejaTest {

    @Test
    void creaParejaConDosCombatientes() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");

        // Act
        Pareja pareja = new Pareja(1, c1, c2);

        // Assert
        assertEquals(1, pareja.getId());
        assertEquals(c1, pareja.getCombatiente1());
        assertEquals(c2, pareja.getCombatiente2());
    }

    @Test
    void noPermiteIdentificadorNegativoOCero() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");

        // Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Pareja(0, c1, c2)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Pareja(-1, c1, c2)
        );
    }

    @Test
    void noPermiteCombatienteNulo() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");

        // Assert
        assertThrows(
            NullPointerException.class,
            () -> new Pareja(1, null, c1)
        );

        assertThrows(
            NullPointerException.class,
            () -> new Pareja(1, c1, null)
        );
    }

    @Test
    void noPermiteElMismoCombatienteDosVeces() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero");

        // Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Pareja(1, c1, c1)
        );
    }

    @Test
    void noPermiteCombatientesConElMismoId() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(1, "Guerrero 2");

        // Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Pareja(1, c1, c2)
        );
    }

    @Test
    void devuelveLosDosCombatientesEnUnaLista() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Act
        List<Combatiente> combatientes = pareja.getCombatientes();

        // Assert
        assertEquals(2, combatientes.size());
        assertTrue(combatientes.contains(c1));
        assertTrue(combatientes.contains(c2));
    }

    @Test
    void listaDeCombatientesNoPuedeModificarse() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> pareja.getCombatientes().add(
                new HechiceroVacio(3, "Guerrero 3")
            )
        );
    }

    @Test
    void parejaNoEstaDerrotadaSiAmbosCombatientesEstanEnPie() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Assert
        assertFalse(pareja.estaDerrotada());
    }

    @Test
    void parejaNoEstaDerrotadaSiSoloUnCombatienteEstaEnPie() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Act
        c1.recibirDanio(1000);

        // Assert
        assertFalse(pareja.estaDerrotada());
    }

    @Test
    void parejaEstaDerrotadaCuandoAmbosCombatientesEstanFueraDeCombate() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Act
        c1.recibirDanio(1000);
        c2.recibirDanio(1000);

        // Assert
        assertTrue(pareja.estaDerrotada());
    }

    @Test
    void calculaCorrectamenteLaVidaTotal() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Act
        c1.recibirDanio(20);
        c2.recibirDanio(40);

        // Assert
        assertEquals(150, pareja.getVidaTotal());
    }

    @Test
    void noSumaLaVidaDeCombatientesEliminados() {
        // Arrange
        Combatiente c1 = new CultivadorEspada(1, "Guerrero 1");
        Combatiente c2 = new MonjeCuerpoCelestial(2, "Guerrero 2");
        Pareja pareja = new Pareja(1, c1, c2);

        // Act
        c1.recibirDanio(1000);

        // Assert
        assertEquals(100, pareja.getVidaTotal());
    }

    @Test
    void dosParejasConElMismoIdSonIguales() {
        // Arrange
        Pareja pareja1 = new Pareja(
            1,
            new CultivadorEspada(1, "Guerrero 1"),
            new MonjeCuerpoCelestial(2, "Guerrero 2")
        );

        Pareja pareja2 = new Pareja(
            1,
            new HechiceroVacio(3, "Guerrero 3"),
            new CultivadorEspada(4, "Guerrero 4")
        );

        // Assert
        assertEquals(pareja1, pareja2);
        assertEquals(pareja1.hashCode(), pareja2.hashCode());
    }

    @Test
    void dosParejasConDistintoIdNoSonIguales() {
        // Arrange
        Pareja pareja1 = new Pareja(
            1,
            new CultivadorEspada(1, "Guerrero 1"),
            new MonjeCuerpoCelestial(2, "Guerrero 2")
        );

        Pareja pareja2 = new Pareja(
            2,
            new CultivadorEspada(3, "Guerrero 3"),
            new MonjeCuerpoCelestial(4, "Guerrero 4")
        );

        // Assert
        assertNotEquals(pareja1, pareja2);
    }
}
