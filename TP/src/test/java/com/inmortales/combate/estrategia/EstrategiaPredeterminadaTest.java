package com.inmortales.combate.estrategia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.inmortales.combate.RegistroTecnicasCombate;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.CultivadorEspada;
import com.inmortales.modelo.EfectoDanio;
import com.inmortales.modelo.EfectoDefensivo;
import com.inmortales.modelo.EfectoRecuperacion;
import com.inmortales.modelo.MonjeCuerpoCelestial;
import com.inmortales.modelo.Pareja;
import com.inmortales.modelo.Tecnica;

class EstrategiaPredeterminadaTest {

    private EstrategiaPredeterminada estrategia;
    private RegistroTecnicasCombate registro;
    private Combatiente combatiente;
    private Combatiente companero;
    private Pareja pareja;

    @BeforeEach
    void setUp() {
        estrategia = new EstrategiaPredeterminada();
        registro = new RegistroTecnicasCombate();

        combatiente = new CultivadorEspada(1, "Guerrero");
        companero = new MonjeCuerpoCelestial(2, "Monje");

        pareja = new Pareja(1, combatiente, companero);
    }

    @Test
    void eligeAtaqueBasicoCuandoNoTieneTecnicas() {
        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertTrue(accion.esAtaqueBasico());
    }

    @Test
    void priorizaRecuperacionCuandoLeFaltaVida() {
        Tecnica recuperacion = new Tecnica(
            "Meditación",
            0,
            new EfectoRecuperacion(20)
        );

        Tecnica ofensiva = new Tecnica(
            "Corte",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(ofensiva);
        combatiente.aprenderTecnica(recuperacion);
        combatiente.recibirDanio(30);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertFalse(accion.esAtaqueBasico());
        assertEquals(recuperacion, accion.getTecnica());
    }

    @Test
    void priorizaDefensaCuandoNoTieneDefensaActiva() {
        Tecnica defensa = new Tecnica(
            "Muro Dorado",
            0,
            new EfectoDefensivo()
        );

        Tecnica ofensiva = new Tecnica(
            "Corte",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(ofensiva);
        combatiente.aprenderTecnica(defensa);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertFalse(accion.esAtaqueBasico());
        assertEquals(defensa, accion.getTecnica());
    }

    @Test
    void noVuelveAElegirDefensaSiYaEstaActiva() {
        Tecnica defensa = new Tecnica(
            "Muro Dorado",
            0,
            new EfectoDefensivo()
        );

        Tecnica ofensiva = new Tecnica(
            "Corte",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(defensa);
        combatiente.aprenderTecnica(ofensiva);
        combatiente.activarDefensa();

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertEquals(ofensiva, accion.getTecnica());
    }

    @Test
    void eligeLaTecnicaOfensivaConMasUsosRestantes() {
        Tecnica tecnica1 = new Tecnica(
            "Corte",
            0,
            new EfectoDanio(20)
        );

        Tecnica tecnica2 = new Tecnica(
            "Golpe Celestial",
            0,
            new EfectoDanio(30)
        );

        combatiente.aprenderTecnica(tecnica1);
        combatiente.aprenderTecnica(tecnica2);

        // La primera técnica queda con 4 usos disponibles.
        combatiente.registrarUsoTecnica(tecnica1);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertEquals(tecnica2, accion.getTecnica());
    }

    @Test
    void desempataPorOrdenDeAprendizajeDeLasTecnicas() {
        Tecnica primera = new Tecnica(
            "Primera",
            0,
            new EfectoDanio(20)
        );

        Tecnica segunda = new Tecnica(
            "Segunda",
            0,
            new EfectoDanio(30)
        );

        combatiente.aprenderTecnica(primera);
        combatiente.aprenderTecnica(segunda);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertEquals(primera, accion.getTecnica());
    }

    @Test
    void eligeAtaqueBasicoCuandoNoTieneEnergiaSuficiente() {
        Tecnica tecnica = new Tecnica(
            "Técnica costosa",
            100,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(tecnica);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertTrue(accion.esAtaqueBasico());
    }

    @Test
    void eligeAtaqueBasicoCuandoAgotoLosUsosDeSusTecnicas() {
        Tecnica tecnica = new Tecnica(
            "Corte",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(tecnica);

        for (int i = 0; i < 5; i++) {
            assertTrue(combatiente.registrarUsoTecnica(tecnica));
        }

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertTrue(accion.esAtaqueBasico());
    }

    @Test
    void noUtilizaTecnicaQueYaUsoSuCompaneroMientrasSigaEnPie() {
        Tecnica tecnicaCompartida = new Tecnica(
            "Corte compartido",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(tecnicaCompartida);
        companero.aprenderTecnica(tecnicaCompartida);

        assertTrue(companero.registrarUsoTecnica(tecnicaCompartida));
        registro.registrarUso(companero, tecnicaCompartida);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertTrue(accion.esAtaqueBasico());
    }

    @Test
    void permiteUsarTecnicaSiElCompaneroFueEliminado() {
        Tecnica tecnicaCompartida = new Tecnica(
            "Corte compartido",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(tecnicaCompartida);
        companero.aprenderTecnica(tecnicaCompartida);

        companero.recibirDanio(1000);

        assertTrue(companero.registrarUsoTecnica(tecnicaCompartida));
        registro.registrarUso(companero, tecnicaCompartida);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente, pareja, registro
        );

        assertEquals(tecnicaCompartida, accion.getTecnica());
    }

    @Test
    void noPermiteElegirAccionConArgumentosNulos() {
        assertThrows(
            NullPointerException.class,
            () -> estrategia.elegirAccion(null, pareja, registro)
        );

        assertThrows(
            NullPointerException.class,
            () -> estrategia.elegirAccion(combatiente, null, registro)
        );

        assertThrows(
            NullPointerException.class,
            () -> estrategia.elegirAccion(combatiente, pareja, null)
        );
    }
    
    @Test
    void permiteUsarTecnicaSiElCompaneroAgotoSusUsos() {

        Tecnica tecnicaCompartida = new Tecnica(
            "Corte compartido",
            0,
            new EfectoDanio(20)
        );

        combatiente.aprenderTecnica(tecnicaCompartida);
        companero.aprenderTecnica(tecnicaCompartida);

        // El compañero consume sus cinco usos.
        for (int i = 0; i < 5; i++) {
            assertTrue(
                companero.registrarUsoTecnica(tecnicaCompartida)
            );
        }

        // Registramos que utilizó esta técnica durante el combate.
        registro.registrarUso(companero, tecnicaCompartida);

        // El compañero sigue en pie.
        assertTrue(companero.estaEnPie());

        // El otro combatiente puede utilizarla.
        AccionCombate accion = estrategia.elegirAccion(
            combatiente,
            pareja,
            registro
        );

        assertEquals(tecnicaCompartida, accion.getTecnica());
    }
}
