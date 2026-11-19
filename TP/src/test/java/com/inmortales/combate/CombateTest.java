package com.inmortales.combate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import com.inmortales.eventos.Evento;
import com.inmortales.eventos.TipoEvento;
import com.inmortales.combate.estrategia.AccionCombate;
import com.inmortales.combate.estrategia.EstrategiaCombate;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.CultivadorEspada;
import com.inmortales.modelo.MonjeCuerpoCelestial;
import com.inmortales.modelo.Pareja;
import com.inmortales.modelo.EfectoDanio;
import com.inmortales.modelo.EfectoRecuperacion;
import com.inmortales.modelo.Tecnica;

class CombateTest {

    private EstrategiaCombate crearEstrategiaBasica() {
        return (combatiente, pareja, registro) ->
            AccionCombate.ataqueBasico();
    }

    private Combate crearCombate(
            Pareja pareja1,
            Pareja pareja2,
            int maxAsaltos) {

        return new Combate(
            pareja1,
            pareja2,
            crearEstrategiaBasica(),
            new ConfiguracionCombate(maxAsaltos, 0),
            new GestorEventos(),
            1
        );
    }

    private Pareja crearPareja(int idPareja, int idCombatiente) {
        Combatiente c1 = new CultivadorEspada(
            idCombatiente,
            "Guerrero " + idCombatiente
        );

        Combatiente c2 = new MonjeCuerpoCelestial(
            idCombatiente + 1,
            "Guerrero " + (idCombatiente + 1)
        );

        return new Pareja(idPareja, c1, c2);
    }

    @Test
    void creaCombateEntreDosParejas() {
        // Arrange
        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(2, 3);

        // Act
        Combate combate = crearCombate(pareja1, pareja2, 10);

        // Assert
        assertNotNull(combate);
        assertNull(combate.getGanadora());
        assertEquals(0, combate.getNumeroAsalto());
    }

    @Test
    void noPermiteEnfrentarUnaParejaContraSiMisma() {
        // Arrange
        Pareja pareja = crearPareja(1, 1);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> crearCombate(pareja, pareja, 10)
        );
    }

    @Test
    void noPermiteEnfrentarParejasConElMismoId() {
        // Arrange
        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(1, 3);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> crearCombate(pareja1, pareja2, 10)
        );
    }

    @Test
    void noPermiteUnNumeroDeRondaInvalido() {
        // Arrange
        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(2, 3);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Combate(
                pareja1,
                pareja2,
                crearEstrategiaBasica(),
                new ConfiguracionCombate(10, 0),
                new GestorEventos(),
                0
            )
        );
    }

    @Test
    void ejecutaCombateYDeterminaUnaGanadora() {
        // Arrange
        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(2, 3);

        Combate combate = crearCombate(pareja1, pareja2, 1);

        // Act
        Pareja ganadora = combate.ejecutar();

        // Assert
        assertNotNull(ganadora);
        assertTrue(
            ganadora.equals(pareja1) || ganadora.equals(pareja2)
        );
        assertEquals(ganadora, combate.getGanadora());
    }

    @Test
    void respetaElLimiteMaximoDeAsaltos() {
        // Arrange
        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(2, 3);

        Combate combate = crearCombate(pareja1, pareja2, 2);

        // Act
        combate.ejecutar();

        // Assert
        assertEquals(2, combate.getNumeroAsalto());
    }

    @Test
    void reiniciaVidaYEnergiaAlComenzarElCombate() {
        // Arrange
        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(2, 3);

        for (Combatiente combatiente : pareja1.getCombatientes()) {
            combatiente.recibirDanio(50);
            combatiente.consumirEnergia(20);
        }

        for (Combatiente combatiente : pareja2.getCombatientes()) {
            combatiente.recibirDanio(50);
            combatiente.consumirEnergia(20);
        }

        Combate combate = crearCombate(pareja1, pareja2, 1);

        // Act
        combate.ejecutar();

        // Assert
        for (Combatiente combatiente : pareja1.getCombatientes()) {
            assertTrue(combatiente.getVida() > 50);
            assertEquals(
                combatiente.getEnergiaMaxima(),
                combatiente.getEnergia()
            );
        }

        for (Combatiente combatiente : pareja2.getCombatientes()) {
            assertTrue(combatiente.getVida() > 50);
            assertEquals(
                combatiente.getEnergiaMaxima(),
                combatiente.getEnergia()
            );
        }
    }

    @Test
    void enCasoDeEmpateGanaLaParejaConMenorId() {
        // Arrange
        Pareja pareja1 = new Pareja(
            10,
            new MonjeCuerpoCelestial(1, "Guerrero 1"),
            new MonjeCuerpoCelestial(2, "Guerrero 2")
        );

        Pareja pareja2 = new Pareja(
            2,
            new MonjeCuerpoCelestial(3, "Guerrero 3"),
            new MonjeCuerpoCelestial(4, "Guerrero 4")
        );

        Combate combate = crearCombate(pareja1, pareja2, 1);

        // Act
        Pareja ganadora = combate.ejecutar();

        // Assert
        assertEquals(pareja2, ganadora);
        assertEquals(
            pareja1.getVidaTotal(),
            pareja2.getVidaTotal()
        );
    }
    
    @Test
    void utilizarTecnicaConsumeEnergiaYUnUso() {

        // Arrange: crear técnica
        Tecnica tecnica = new Tecnica(
            "Corte Celestial",
            10,
            new EfectoDanio(20)
        );

        // Crear combatientes
        Combatiente atacante = new CultivadorEspada(1, "Guerrero");
        Combatiente companero = new CultivadorEspada(2, "Compañero");

        Combatiente rival1 = new CultivadorEspada(3, "Rival 1");
        Combatiente rival2 = new CultivadorEspada(4, "Rival 2");

        // El atacante aprende la técnica
        atacante.aprenderTecnica(tecnica);

        // Crear parejas
        Pareja pareja1 = new Pareja(1, atacante, companero);
        Pareja pareja2 = new Pareja(2, rival1, rival2);

        // Configurar una estrategia que haga utilizar la técnica
        // al atacante y ataques básicos al resto.
        EstrategiaCombate estrategia = (combatiente, pareja, registro) -> {

            if (combatiente.equals(atacante)) {
                return AccionCombate.usarTecnica(tecnica);
            }

            return AccionCombate.ataqueBasico();
        };

        ConfiguracionCombate configuracion =
            new ConfiguracionCombate(1, 0);

        GestorEventos gestorEventos = new GestorEventos();

        Combate combate = new Combate(
            pareja1,
            pareja2,
            estrategia,
            configuracion,
            gestorEventos,
            1
        );

        // Act
        combate.ejecutar();

        // Assert
        assertEquals(30, atacante.getEnergia());
        assertEquals(4, atacante.getUsosRestantes(tecnica));
    }
    
    @Test
    void seleccionaComoObjetivoAlRivalConMenorVida() {

        Tecnica tecnica = new Tecnica(
            "Golpe poderoso",
            0,
            new EfectoDanio(50)
        );

        Combatiente atacante1 = new CultivadorEspada(1, "Atacante 1");
        Combatiente atacante2 = new CultivadorEspada(2, "Atacante 2");
        Combatiente rival1 = new CultivadorEspada(3, "Rival 1");
        Combatiente rival2 = new CultivadorEspada(4, "Rival 2");

        atacante1.aprenderTecnica(tecnica);

        Pareja pareja1 = new Pareja(1, atacante1, atacante2);
        Pareja pareja2 = new Pareja(2, rival1, rival2);

        EstrategiaCombate estrategia = (combatiente, pareja, registro) -> {
            if (combatiente.equals(atacante1)
                    && !registro.utilizoTecnica(atacante1, tecnica)) {
                return AccionCombate.usarTecnica(tecnica);
            }

            return AccionCombate.ataqueBasico();
        };

        GestorEventos gestor = new GestorEventos();
        List<Evento> eventos = new java.util.ArrayList<>();
        gestor.suscribir(eventos::add);

        Combate combate = new Combate(
            pareja1,
            pareja2,
            estrategia,
            new ConfiguracionCombate(1, 0),
            gestor,
            1
        );

        combate.ejecutar();

        Evento segundoAtaque = eventos.stream()
            .filter(e -> e.getTipo() == TipoEvento.ATAQUE_BASICO)
            .filter(e -> e.getIdCombatiente() == 2)
            .findFirst()
            .orElseThrow();

        assertEquals(3, segundoAtaque.getIdObjetivo());
    }
    
    @Test
    void recuperaEnergiaAlComenzarCadaAsalto() {

        Tecnica tecnica = new Tecnica(
            "Corte Celestial",
            10,
            new EfectoDanio(1)
        );

        Combatiente atacante = new CultivadorEspada(1, "Atacante");
        Combatiente companero = new CultivadorEspada(2, "Compañero");
        Combatiente rival1 = new CultivadorEspada(3, "Rival 1");
        Combatiente rival2 = new CultivadorEspada(4, "Rival 2");

        atacante.aprenderTecnica(tecnica);

        Pareja pareja1 = new Pareja(1, atacante, companero);
        Pareja pareja2 = new Pareja(2, rival1, rival2);

        EstrategiaCombate estrategia = (combatiente, pareja, registro) -> {
            if (combatiente.equals(atacante)
                    && !registro.utilizoTecnica(atacante, tecnica)) {
                return AccionCombate.usarTecnica(tecnica);
            }

            return AccionCombate.ataqueBasico();
        };

        Combate combate = new Combate(
            pareja1,
            pareja2,
            estrategia,
            new ConfiguracionCombate(2, 5),
            new GestorEventos(),
            1
        );

        combate.ejecutar();

        // 40 iniciales - 10 de costo + 5 de recuperación.
        assertEquals(35, atacante.getEnergia());
        assertEquals(4, atacante.getUsosRestantes(tecnica));
    }
    
    @Test
    void publicaEventoCuandoUnCombatienteEsEliminado() {

        Tecnica tecnica = new Tecnica(
            "Golpe Mortal",
            0,
            new EfectoDanio(1000)
        );

        Combatiente atacante = new CultivadorEspada(1, "Atacante");
        Combatiente companero = new CultivadorEspada(2, "Compañero");
        Combatiente rival1 = new CultivadorEspada(3, "Rival 1");
        Combatiente rival2 = new CultivadorEspada(4, "Rival 2");

        atacante.aprenderTecnica(tecnica);

        Pareja pareja1 = new Pareja(1, atacante, companero);
        Pareja pareja2 = new Pareja(2, rival1, rival2);

        EstrategiaCombate estrategia = (combatiente, pareja, registro) -> {
            if (combatiente.equals(atacante)) {
                return AccionCombate.usarTecnica(tecnica);
            }

            return AccionCombate.ataqueBasico();
        };

        GestorEventos gestor = new GestorEventos();
        List<Evento> eventos = new java.util.ArrayList<>();
        gestor.suscribir(eventos::add);

        Combate combate = new Combate(
            pareja1,
            pareja2,
            estrategia,
            new ConfiguracionCombate(1, 0),
            gestor,
            1
        );

        combate.ejecutar();

        assertTrue(eventos.stream().anyMatch(evento ->
            evento.getTipo() == TipoEvento.COMBATIENTE_ELIMINADO
                && evento.getIdCombatiente() == 3
        ));

        assertFalse(rival1.estaEnPie());
    }
    
    @Test
    void ejecutaLosTurnosEnOrdenDeParejaYCombatiente() {

        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(2, 3);

        GestorEventos gestor = new GestorEventos();
        List<Evento> eventos = new java.util.ArrayList<>();
        gestor.suscribir(eventos::add);

        Combate combate = new Combate(
            pareja1,
            pareja2,
            crearEstrategiaBasica(),
            new ConfiguracionCombate(1, 0),
            gestor,
            1
        );

        combate.ejecutar();

        List<Integer> orden = eventos.stream()
            .filter(evento ->
                evento.getTipo() == TipoEvento.ATAQUE_BASICO)
            .map(Evento::getIdCombatiente)
            .toList();

        assertEquals(List.of(1, 2, 3, 4), orden);
    }
    
    @Test
    void mantieneLosUsosDeTecnicasEntreCombates() {
        // Arrange
        Combatiente atacante =
            new CultivadorEspada(1, "Guerrero");

        Combatiente companero =
            new MonjeCuerpoCelestial(2, "Compañero");

        Combatiente rival1 =
            new CultivadorEspada(3, "Rival 1");

        Combatiente rival2 =
            new MonjeCuerpoCelestial(4, "Rival 2");

        Tecnica tecnica = new Tecnica(
            "Meditación",
            0,
            new EfectoRecuperacion(10)
        );

        atacante.aprenderTecnica(tecnica);

        Pareja pareja1 = new Pareja(1, atacante, companero);
        Pareja pareja2 = new Pareja(2, rival1, rival2);

        EstrategiaCombate estrategia = (combatiente, pareja, registro) -> {
            if (combatiente.equals(atacante)
                    && atacante.getUsosRestantes(tecnica) > 0) {
                return AccionCombate.usarTecnica(tecnica);
            }

            return AccionCombate.ataqueBasico();
        };

        ConfiguracionCombate configuracion =
            new ConfiguracionCombate(5, 0);

        GestorEventos gestor = new GestorEventos();

        Combate primerCombate = new Combate(
            pareja1,
            pareja2,
            estrategia,
            configuracion,
            gestor,
            1
        );

        // Act: ejecutar el primer combate
        primerCombate.ejecutar();

        // Assert: consumió los cinco usos
        assertEquals(0, atacante.getUsosRestantes(tecnica));

        // Arrange: crear un segundo combate con el mismo combatiente
        Combatiente rival3 =
            new CultivadorEspada(5, "Rival 3");

        Combatiente rival4 =
            new MonjeCuerpoCelestial(6, "Rival 4");

        Pareja pareja3 = new Pareja(3, rival3, rival4);

        Combate segundoCombate = new Combate(
            pareja1,
            pareja3,
            estrategia,
            configuracion,
            gestor,
            2
        );

        // Act
        segundoCombate.ejecutar();

        // Assert: los usos siguen agotados
        assertEquals(0, atacante.getUsosRestantes(tecnica));
    }
    
 // Regla (sección 6): al llegar a cero de vida, el combatiente queda
    // fuera de combate: no actúa ni puede ser objetivo en ese combate.
    @Test
    void unCaidoNoActuaNiEsElegidoComoObjetivo() {

        // Golpe que elimina de un solo uso
        Tecnica golpeMortal = new Tecnica(
            "Golpe Mortal", 0, new EfectoDanio(1000)
        );

        Combatiente atacante = new CultivadorEspada(1, "Atacante");
        Combatiente companero = new CultivadorEspada(2, "Compañero");
        Combatiente rival1 = new CultivadorEspada(3, "Rival 1");
        Combatiente rival2 = new MonjeCuerpoCelestial(4, "Rival 2");

        atacante.aprenderTecnica(golpeMortal);

        Pareja pareja1 = new Pareja(1, atacante, companero);
        Pareja pareja2 = new Pareja(2, rival1, rival2);

        // El atacante usa el golpe una sola vez (elimina a Rival 1);
        // después todos hacen ataques básicos.
        EstrategiaCombate estrategia = (combatiente, pareja, registro) -> {
            if (combatiente.equals(atacante)
                    && !registro.utilizoTecnica(atacante, golpeMortal)) {
                return AccionCombate.usarTecnica(golpeMortal);
            }
            return AccionCombate.ataqueBasico();
        };

        GestorEventos gestor = new GestorEventos();
        List<Evento> eventos = new java.util.ArrayList<>();
        gestor.suscribir(eventos::add);

        Combate combate = new Combate(
            pareja1, pareja2, estrategia,
            new ConfiguracionCombate(3, 0), gestor, 1
        );

        // Act
        combate.ejecutar();

        // Buscamos el momento en que cae Rival 1 (id 3)
        int indiceEliminacion = -1;
        for (int i = 0; i < eventos.size(); i++) {
            Evento evento = eventos.get(i);
            if (evento.getTipo() == TipoEvento.COMBATIENTE_ELIMINADO
                    && Integer.valueOf(3).equals(evento.getIdCombatiente())) {
                indiceEliminacion = i;
                break;
            }
        }

        assertTrue(indiceEliminacion >= 0, "Rival 1 debería haber caído");
        assertFalse(rival1.estaEnPie());

        List<Evento> accionesPosteriores = eventos
            .subList(indiceEliminacion + 1, eventos.size())
            .stream()
            .filter(evento -> evento.getTipo() == TipoEvento.ATAQUE_BASICO
                || evento.getTipo() == TipoEvento.USO_TECNICA)
            .toList();

        // Hubo acciones después de la caída (la prueba no es vacía)
        assertFalse(accionesPosteriores.isEmpty());

        // Ninguna la hizo Rival 1, y nadie lo eligió como objetivo,
        // aunque con 0 de vida sería "el rival con menos vida".
        for (Evento accion : accionesPosteriores) {
            assertNotEquals(Integer.valueOf(3), accion.getIdCombatiente(),
                "Un caído no debe actuar");
            assertNotEquals(Integer.valueOf(3), accion.getIdObjetivo(),
                "Un caído no debe ser elegido como objetivo");
        }
    }
}
