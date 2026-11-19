
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
    
 // Regla (sección 1 y prueba mínima de la sección 8):
    // una misma técnica provoca distinto daño según quién la ejecuta,
    // sin que la técnica conozca el tipo del combatiente.
    @Test
    void unaMismaTecnicaDaniaDistintoSegunElTipoDelEjecutor() {
        // Arrange: UNA sola técnica, compartida por los tres ejecutores
        Tecnica corte = new Tecnica(
            "Corte de los Mil Amaneceres", 0, new EfectoDanio(20)
        );

        Combatiente espada = new CultivadorEspada(1, "Espada");
        Combatiente monje = new MonjeCuerpoCelestial(2, "Monje");
        Combatiente hechicero = new HechiceroVacio(3, "Hechicero");

        // Objetivos iguales (Hechiceros: reciben el daño sin reducción)
        Combatiente objetivo1 = new HechiceroVacio(4, "Objetivo 1");
        Combatiente objetivo2 = new HechiceroVacio(5, "Objetivo 2");
        Combatiente objetivo3 = new HechiceroVacio(6, "Objetivo 3");

        // Act
        corte.ejecutar(espada, objetivo1);
        corte.ejecutar(monje, objetivo2);
        corte.ejecutar(hechicero, objetivo3);

        int danioEspada = objetivo1.getVidaMaxima() - objetivo1.getVida();
        int danioMonje = objetivo2.getVidaMaxima() - objetivo2.getVida();
        int danioHechicero = objetivo3.getVidaMaxima() - objetivo3.getVida();

        // Assert: el Cultivador de la Espada potencia el daño (20 * 1.3)
        assertEquals(26, danioEspada);
        assertEquals(20, danioMonje);
        assertEquals(20, danioHechicero);
        assertTrue(danioEspada > danioMonje);
    }

}
