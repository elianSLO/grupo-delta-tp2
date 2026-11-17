package com.inmortales.eventos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class CronistaSectaTest {

    private Evento crearEvento(
            TipoEvento tipo,
            Integer idPareja1,
            Integer vidaPareja1,
            Integer idPareja2,
            Integer vidaPareja2) {

        return new Evento(
            tipo,
            "Evento de prueba.",
            1, 1,
            null, null,
            idPareja1, vidaPareja1,
            idPareja2, vidaPareja2,
            null
        );
    }

    @Test
    void registraTodosLosEventosEnElHistorial() {
        CronistaSecta cronista = new CronistaSecta();

        Evento evento1 = crearEvento(
            TipoEvento.INICIO_COMBATE, null, null, null, null
        );

        Evento evento2 = crearEvento(
            TipoEvento.ATAQUE_BASICO, null, null, null, null
        );

        cronista.actualizar(evento1);
        cronista.actualizar(evento2);

        assertEquals(2, cronista.getHistorial().size());
        assertEquals(evento1, cronista.getHistorial().get(0));
        assertEquals(evento2, cronista.getHistorial().get(1));
    }

    @Test
    void registraResultadosAlFinalizarUnCombate() {
        CronistaSecta cronista = new CronistaSecta();

        Evento evento = crearEvento(
            TipoEvento.FIN_COMBATE,
            1, 80,
            2, 40
        );

        cronista.actualizar(evento);

        List<ResultadoPareja> posiciones =
            cronista.obtenerPosiciones();

        assertEquals(2, posiciones.size());

        assertTrue(posiciones.stream().anyMatch(
            r -> r.getIdPareja() == 1
                && r.getUltimaRonda() == 1
                && r.getVidaFinal() == 80
        ));

        assertTrue(posiciones.stream().anyMatch(
            r -> r.getIdPareja() == 2
                && r.getUltimaRonda() == 1
                && r.getVidaFinal() == 40
        ));
    }

    @Test
    void noRegistraResultadosDeEventosQueNoSeanFinDeCombate() {
        CronistaSecta cronista = new CronistaSecta();

        Evento evento = crearEvento(
            TipoEvento.INICIO_COMBATE,
            1, 100,
            2, 100
        );

        cronista.actualizar(evento);

        assertTrue(cronista.obtenerPosiciones().isEmpty());
    }

    @Test
    void ordenaPrimeroPorUltimaRondaAlcanzada() {
        CronistaSecta cronista = new CronistaSecta();

        cronista.actualizar(crearEvento(
            TipoEvento.FIN_COMBATE, 1, 100, 2, 80
        ));

        Evento segundaRonda = new Evento(
            TipoEvento.FIN_COMBATE,
            "Final.",
            2, 1,
            null, null,
            3, 10,
            4, 20,
            null
        );

        cronista.actualizar(segundaRonda);

        List<ResultadoPareja> posiciones =
            cronista.obtenerPosiciones();

        assertEquals(4, posiciones.get(0).getIdPareja());
        assertEquals(3, posiciones.get(1).getIdPareja());
        assertEquals(1, posiciones.get(2).getIdPareja());
        assertEquals(2, posiciones.get(3).getIdPareja());
    }

    @Test
    void ordenaPorVidaCuandoLasParejasLleganALaMismaRonda() {
        CronistaSecta cronista = new CronistaSecta();

        cronista.actualizar(crearEvento(
            TipoEvento.FIN_COMBATE, 1, 30, 2, 90
        ));

        List<ResultadoPareja> posiciones =
            cronista.obtenerPosiciones();

        assertEquals(2, posiciones.get(0).getIdPareja());
        assertEquals(1, posiciones.get(1).getIdPareja());
    }

    @Test
    void desempataPorMenorIdCuandoLaRondaYVidaSonIguales() {
        CronistaSecta cronista = new CronistaSecta();

        cronista.actualizar(crearEvento(
            TipoEvento.FIN_COMBATE, 5, 70, 2, 70
        ));

        List<ResultadoPareja> posiciones =
            cronista.obtenerPosiciones();

        assertEquals(2, posiciones.get(0).getIdPareja());
        assertEquals(5, posiciones.get(1).getIdPareja());
    }

    @Test
    void actualizaElResultadoDeUnaParejaEnRondasPosteriores() {
        CronistaSecta cronista = new CronistaSecta();

        cronista.actualizar(crearEvento(
            TipoEvento.FIN_COMBATE, 1, 60, 2, 40
        ));

        Evento segundaRonda = new Evento(
            TipoEvento.FIN_COMBATE,
            "Semifinal.",
            2, 1,
            null, null,
            1, 35,
            3, 20,
            null
        );

        cronista.actualizar(segundaRonda);

        ResultadoPareja resultado = cronista.obtenerPosiciones()
            .stream()
            .filter(r -> r.getIdPareja() == 1)
            .findFirst()
            .orElseThrow();

        assertEquals(2, resultado.getUltimaRonda());
        assertEquals(35, resultado.getVidaFinal());
    }

    @Test
    void devuelveUnaCopiaInmodificableDelHistorial() {
        CronistaSecta cronista = new CronistaSecta();

        cronista.actualizar(crearEvento(
            TipoEvento.INICIO_TORNEO, null, null, null, null
        ));

        List<Evento> historial = cronista.getHistorial();

        assertThrows(
            UnsupportedOperationException.class,
            () -> historial.clear()
        );

        assertEquals(1, cronista.getHistorial().size());
    }
}
