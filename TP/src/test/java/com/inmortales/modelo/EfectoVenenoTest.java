package com.inmortales.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.inmortales.modelo.equipamiento.Armadura;

// Bonus: efecto temporal (veneno).
// Cuándo se aplica: al comienzo de cada turno del combatiente afectado.
// Cuánto dura: la cantidad de turnos que indica el efecto.
// Qué pasa al terminar: deja de hacer daño; y se borra al empezar otro combate.
class EfectoVenenoTest {

    private Tecnica crearVeneno() {
        return new Tecnica(
            "Veneno de la Serpiente Celestial", 0, new EfectoVeneno(5, 3)
        );
    }

    @Test
    void venenoDaniaAlComienzoDeCadaTurnoDelAfectado() {
        // Arrange
        Combatiente atacante = new HechiceroVacio(1, "Atacante");
        Combatiente afectado = new CultivadorEspada(2, "Afectado");

        // Act: la técnica envenena, pero no daña en el momento
        crearVeneno().ejecutar(atacante, afectado);

        // Assert
        assertEquals(100, afectado.getVida());
        assertEquals(3, afectado.getTurnosVeneno());

        // Act: comienza el turno del afectado
        int danio = afectado.aplicarEfectosInicioTurno();

        // Assert
        assertEquals(5, danio);
        assertEquals(95, afectado.getVida());
        assertEquals(2, afectado.getTurnosVeneno());
    }

    @Test
    void venenoTerminaAlCumplirSuDuracion() {
        // Arrange
        Combatiente afectado = new CultivadorEspada(1, "Afectado");
        afectado.aplicarVeneno(5, 3);

        // Act: pasan los tres turnos del veneno
        afectado.aplicarEfectosInicioTurno();
        afectado.aplicarEfectosInicioTurno();
        afectado.aplicarEfectosInicioTurno();

        // Assert
        assertEquals(85, afectado.getVida());
        assertEquals(0, afectado.getTurnosVeneno());

        // Act: un cuarto turno ya no hace daño
        int danio = afectado.aplicarEfectosInicioTurno();

        // Assert
        assertEquals(0, danio);
        assertEquals(85, afectado.getVida());
    }

    @Test
    void venenoIgnoraLaArmaduraYLasDefensas() {
        // Arrange: Monje (recibe menos daño) + Armadura Dorada + defensa activa
        Combatiente monje = new MonjeCuerpoCelestial(1, "Monje");
        monje.equipar(new Armadura("Armadura Dorada", 5));
        monje.activarDefensa();
        monje.aplicarVeneno(5, 3);

        // Act
        monje.aplicarEfectosInicioTurno();
        monje.aplicarEfectosInicioTurno();
        monje.aplicarEfectosInicioTurno();

        // Assert: recibe los 15 completos, porque es daño interno
        assertEquals(85, monje.getVida());
    }

    @Test
    void venenoSeBorraAlComenzarOtroCombate() {
        // Arrange
        Combatiente afectado = new CultivadorEspada(1, "Afectado");
        afectado.aplicarVeneno(5, 3);
        afectado.aplicarEfectosInicioTurno();

        // Act: empieza un combate nuevo
        afectado.reiniciarParaCombate();
        int danio = afectado.aplicarEfectosInicioTurno();

        // Assert
        assertEquals(0, afectado.getTurnosVeneno());
        assertEquals(0, danio);
        assertEquals(100, afectado.getVida());
    }
}
