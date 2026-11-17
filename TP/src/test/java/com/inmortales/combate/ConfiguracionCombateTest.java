package com.inmortales.combate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfiguracionCombateTest {

    @Test
    void creaConfiguracionValida() {
        // Arrange & Act
        ConfiguracionCombate config = new ConfiguracionCombate(10, 5);

        // Assert
        assertEquals(10, config.getMaxAsaltos());
        assertEquals(5, config.getRecuperacionEnergia());
    }

    @Test
    void noPermiteCeroAsaltos() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ConfiguracionCombate(0, 5)
        );
    }

    @Test
    void noPermiteAsaltosNegativos() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ConfiguracionCombate(-1, 5)
        );
    }

    @Test
    void noPermiteRecuperacionEnergiaNegativa() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ConfiguracionCombate(10, -1)
        );
    }

    @Test
    void permiteRecuperacionEnergiaCero() {
        // Arrange & Act
        ConfiguracionCombate config = new ConfiguracionCombate(10, 0);

        // Assert
        assertEquals(0, config.getRecuperacionEnergia());
    }
}
