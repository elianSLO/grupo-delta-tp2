
package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.inmortales.modelo.equipamiento.Arma;
import com.inmortales.modelo.equipamiento.Armadura;

class CombatienteTest {

    @Test
    void cultivadorEspadaAumentaElDanioUnTreintaPorCiento() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act
        int danio = combatiente.calcularDanio(10);

        // Assert
        assertEquals(13, danio);
    }

    @Test
    void armaAumentaElDanioRealizado() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");
        combatiente.equipar(new Arma("Espada celestial", 8));

        // Act
        int danio = combatiente.calcularDanio(10);

        // Assert
        assertEquals(21, danio);
    }

    @Test
    void monjeReduceElDanioRecibidoUnVeinticincoPorCiento() {
        // Arrange
        Combatiente monje = new MonjeCuerpoCelestial(1, "Monje");

        // Act
        monje.recibirDanio(40);

        // Assert
        assertEquals(70, monje.getVida());
    }

    @Test
    void defensaActivaReduceElDanioRecibidoALaMitad() {
        // Arrange
        Combatiente monje = new MonjeCuerpoCelestial(1, "Monje");
        monje.activarDefensa();

        // Act
        monje.recibirDanio(40);

        // Assert
        assertEquals(85, monje.getVida());
    }

    @Test
    void armaduraReduceElDanioRecibido() {
        // Arrange
        Combatiente monje = new MonjeCuerpoCelestial(1, "Monje");
        monje.equipar(new Armadura("Armadura dorada", 5));

        // Act
        monje.recibirDanio(40);

        // Assert
        assertEquals(75, monje.getVida());
    }

    @Test
    void hechiceroReduceElCostoDeLasTecnicas() {
        // Arrange
        Combatiente hechicero = new HechiceroVacio(1, "Hechicero");

        // Act
        int costo = hechicero.calcularCostoEnergia(10);

        // Assert
        assertEquals(8, costo);
    }

    @Test
    void noSePuedeCalcularDanioConValorNegativo() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> combatiente.calcularDanio(-10)
        );
    }

    @Test
    void noSePuedeRecibirDanioNegativo() {
        // Arrange
        Combatiente combatiente = new CultivadorEspada(1, "Guerrero");

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> combatiente.recibirDanio(-10)
        );
    }
}
