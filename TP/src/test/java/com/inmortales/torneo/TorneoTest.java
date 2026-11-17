package com.inmortales.torneo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import com.inmortales.eventos.Evento;
import com.inmortales.eventos.ObservadorEventos;
import com.inmortales.eventos.TipoEvento;
import com.inmortales.combate.ConfiguracionCombate;
import com.inmortales.combate.estrategia.AccionCombate;
import com.inmortales.combate.estrategia.EstrategiaCombate;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.CultivadorEspada;
import com.inmortales.modelo.MonjeCuerpoCelestial;
import com.inmortales.modelo.Pareja;

class TorneoTest {

    private Torneo crearTorneo() {
        EstrategiaCombate estrategia =
            (combatiente, pareja, registro) ->
                AccionCombate.ataqueBasico();

        ConfiguracionCombate configuracion =
            new ConfiguracionCombate(1, 0);

        GestorEventos gestorEventos = new GestorEventos();

        return new Torneo(
            estrategia,
            configuracion,
            gestorEventos
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

    private void inscribirCuatroParejas(Torneo torneo) {
        torneo.inscribirPareja(crearPareja(1, 1));
        torneo.inscribirPareja(crearPareja(2, 3));
        torneo.inscribirPareja(crearPareja(3, 5));
        torneo.inscribirPareja(crearPareja(4, 7));
    }

    @Test
    void permiteInscribirParejas() {
        // Arrange
        Torneo torneo = crearTorneo();
        Pareja pareja = crearPareja(1, 1);

        // Act
        torneo.inscribirPareja(pareja);

        // Assert
        assertEquals(1, torneo.getParejasInscritas().size());
        assertTrue(torneo.getParejasInscritas().contains(pareja));
    }

    @Test
    void noPermiteInscribirUnaParejaNula() {
        // Arrange
        Torneo torneo = crearTorneo();

        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> torneo.inscribirPareja(null)
        );
    }

    @Test
    void noPermiteInscribirDosParejasConElMismoId() {
        // Arrange
        Torneo torneo = crearTorneo();

        Pareja pareja1 = crearPareja(1, 1);
        Pareja pareja2 = crearPareja(1, 3);

        torneo.inscribirPareja(pareja1);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> torneo.inscribirPareja(pareja2)
        );

        assertEquals(1, torneo.getParejasInscritas().size());
    }

    @Test
    void noPermiteInscribirUnCombatienteEnDosParejas() {
        // Arrange
        Torneo torneo = crearTorneo();

        Combatiente compartido =
            new CultivadorEspada(1, "Guerrero compartido");

        Combatiente segundo =
            new MonjeCuerpoCelestial(2, "Guerrero 2");

        Combatiente tercero =
            new CultivadorEspada(3, "Guerrero 3");

        Pareja pareja1 = new Pareja(1, compartido, segundo);
        Pareja pareja2 = new Pareja(2, compartido, tercero);

        torneo.inscribirPareja(pareja1);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> torneo.inscribirPareja(pareja2)
        );

        assertEquals(1, torneo.getParejasInscritas().size());
    }

    @Test
    void noPermiteIniciarConMenosDeCuatroParejas() {
        // Arrange
        Torneo torneo = crearTorneo();

        torneo.inscribirPareja(crearPareja(1, 1));
        torneo.inscribirPareja(crearPareja(2, 3));
        torneo.inscribirPareja(crearPareja(3, 5));

        // Act & Assert
        assertThrows(
            IllegalStateException.class,
            torneo::iniciar
        );

        assertFalse(torneo.estaIniciado());
    }

    @Test
    void noPermiteIniciarConCincoParejas() {
        // Arrange
        Torneo torneo = crearTorneo();

        for (int i = 0; i < 5; i++) {
            torneo.inscribirPareja(
                crearPareja(i + 1, i * 2 + 1)
            );
        }

        // Act & Assert
        assertThrows(
            IllegalStateException.class,
            torneo::iniciar
        );

        assertFalse(torneo.estaIniciado());
    }

    @Test
    void noPermiteModificarLaListaDeParejasInscritas() {
        // Arrange
        Torneo torneo = crearTorneo();
        torneo.inscribirPareja(crearPareja(1, 1));

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> torneo.getParejasInscritas().clear()
        );

        assertEquals(1, torneo.getParejasInscritas().size());
    }

    @Test
    void iniciaCorrectamenteUnTorneoConCuatroParejas() {
        // Arrange
        Torneo torneo = crearTorneo();
        inscribirCuatroParejas(torneo);

        // Act
        Pareja campeona = torneo.iniciar();

        // Assert
        assertTrue(torneo.estaIniciado());
        assertNotNull(campeona);
        assertNotNull(torneo.getCampeona());
        assertEquals(campeona, torneo.getCampeona());
        assertTrue(torneo.getParejasInscritas().contains(campeona));
    }

    @Test
    void noPermiteInscribirParejasDespuesDeIniciarElTorneo() {
        // Arrange
        Torneo torneo = crearTorneo();
        inscribirCuatroParejas(torneo);

        torneo.iniciar();

        // Act & Assert
        assertThrows(
            IllegalStateException.class,
            () -> torneo.inscribirPareja(crearPareja(5, 9))
        );
    }

    @Test
    void noPermiteIniciarDosVecesElMismoTorneo() {
        // Arrange
        Torneo torneo = crearTorneo();
        inscribirCuatroParejas(torneo);

        torneo.iniciar();

        // Act & Assert
        assertThrows(
            IllegalStateException.class,
            torneo::iniciar
        );
    }
    
    @Test
    void permiteIniciarUnTorneoConOchoParejas() {
        // Arrange
        Torneo torneo = crearTorneo();

        for (int i = 0; i < 8; i++) {
            torneo.inscribirPareja(
                crearPareja(i + 1, i * 2 + 1)
            );
        }

        // Act
        Pareja campeona = torneo.iniciar();

        // Assert
        assertNotNull(campeona);
        assertTrue(torneo.estaIniciado());
        assertEquals(campeona, torneo.getCampeona());
    }
    
    @Test
    void mantieneLasInscripcionesOriginalesSiSeIntentaRepetirUnCombatiente() {
        // Arrange
        Torneo torneo = crearTorneo();

        Combatiente compartido =
            new CultivadorEspada(1, "Guerrero compartido");

        Combatiente segundo =
            new MonjeCuerpoCelestial(2, "Guerrero 2");

        Combatiente tercero =
            new CultivadorEspada(3, "Guerrero 3");

        Pareja pareja1 = new Pareja(1, compartido, segundo);
        Pareja pareja2 = new Pareja(2, compartido, tercero);

        torneo.inscribirPareja(pareja1);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> torneo.inscribirPareja(pareja2)
        );

        assertEquals(1, torneo.getParejasInscritas().size());
        assertTrue(torneo.getParejasInscritas().contains(pareja1));
    }
    
    @Test
    void publicaEventosDeInicioYProclamacionDelCampeon() {
        // Arrange
        Torneo torneo = crearTorneo();
        List<Evento> eventos = new ArrayList<>();

        GestorEventos gestor = new GestorEventos();
        gestor.suscribir(eventos::add);

        torneo = new Torneo(
            (combatiente, pareja, registro) ->
                AccionCombate.ataqueBasico(),
            new ConfiguracionCombate(1, 0),
            gestor
        );

        inscribirCuatroParejas(torneo);

        // Act
        torneo.iniciar();

        // Assert
        assertTrue(eventos.stream().anyMatch(
            e -> e.getTipo() == TipoEvento.INICIO_TORNEO
        ));

        assertTrue(eventos.stream().anyMatch(
            e -> e.getTipo() == TipoEvento.CAMPEON_PROCLAMADO
        ));
    }
    
    @Test
    void respetaElOrdenDeInscripcionEnLaPrimeraRonda() {
        // Arrange
        Torneo torneo = crearTorneo();
        List<Evento> eventos = new ArrayList<>();

        GestorEventos gestor = new GestorEventos();
        gestor.suscribir(eventos::add);

        torneo = new Torneo(
            (combatiente, pareja, registro) ->
                AccionCombate.ataqueBasico(),
            new ConfiguracionCombate(1, 0),
            gestor
        );

        inscribirCuatroParejas(torneo);

        // Act
        torneo.iniciar();

        // Assert
        List<Evento> inicios = eventos.stream()
            .filter(e -> e.getTipo() == TipoEvento.INICIO_COMBATE)
            .toList();

        assertEquals(3, inicios.size());

        assertEquals(1, inicios.get(0).getIdPareja1());
        assertEquals(2, inicios.get(0).getIdPareja2());

        assertEquals(3, inicios.get(1).getIdPareja1());
        assertEquals(4, inicios.get(1).getIdPareja2());
    }
    
    @Test
    void ejecutaCorrectamenteUnTorneoConOchoParejasYTresRondas() {

        // Arrange
        EstrategiaCombate estrategia =
            (combatiente, pareja, registro) ->
                AccionCombate.ataqueBasico();

        ConfiguracionCombate configuracion =
            new ConfiguracionCombate(1, 0);

        GestorEventos gestorEventos = new GestorEventos();

        List<Evento> eventos = new ArrayList<>();

        ObservadorEventos observador = eventos::add;
        gestorEventos.suscribir(observador);

        Torneo torneo = new Torneo(
            estrategia,
            configuracion,
            gestorEventos
        );

        // Inscribir ocho parejas.
        for (int i = 0; i < 8; i++) {
            torneo.inscribirPareja(
                crearPareja(i + 1, i * 2 + 1)
            );
        }

        // Act
        Pareja campeona = torneo.iniciar();

        // Assert: el torneo finalizó correctamente.
        assertTrue(torneo.estaIniciado());
        assertNotNull(campeona);
        assertEquals(campeona, torneo.getCampeona());

        // Verificar la cantidad de combates.
        long combatesIniciados = eventos.stream()
            .filter(e -> e.getTipo() == TipoEvento.INICIO_COMBATE)
            .count();

        long combatesFinalizados = eventos.stream()
            .filter(e -> e.getTipo() == TipoEvento.FIN_COMBATE)
            .count();

        assertEquals(7, combatesIniciados);
        assertEquals(7, combatesFinalizados);

        // Verificar la cantidad de combates por ronda.
        assertEquals(
            4,
            eventos.stream()
                .filter(e -> e.getTipo() == TipoEvento.INICIO_COMBATE
                    && e.getNumeroRonda() == 1)
                .count()
        );

        assertEquals(
            2,
            eventos.stream()
                .filter(e -> e.getTipo() == TipoEvento.INICIO_COMBATE
                    && e.getNumeroRonda() == 2)
                .count()
        );

        assertEquals(
            1,
            eventos.stream()
                .filter(e -> e.getTipo() == TipoEvento.INICIO_COMBATE
                    && e.getNumeroRonda() == 3)
                .count()
        );

        // Verificar que se proclamó un campeón.
        assertEquals(
            1,
            eventos.stream()
                .filter(e -> e.getTipo() == TipoEvento.CAMPEON_PROCLAMADO)
                .count()
        );
    }
}
