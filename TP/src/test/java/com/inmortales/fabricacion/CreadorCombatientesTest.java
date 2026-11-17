package com.inmortales.fabricacion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.CultivadorEspada;
import com.inmortales.modelo.EfectoDanio;
import com.inmortales.modelo.Escuela;
import com.inmortales.modelo.HechiceroVacio;
import com.inmortales.modelo.MonjeCuerpoCelestial;
import com.inmortales.modelo.Tecnica;
import com.inmortales.modelo.equipamiento.Arma;
import com.inmortales.modelo.equipamiento.Equipamiento;

class CreadorCombatientesTest {

    private CreadorCombatientes creador;
    private Escuela escuela;

    @BeforeEach
    void setUp() {
        creador = new CreadorCombatientes();

        Tecnica corte = new Tecnica(
            "Corte Celestial",
            10,
            new EfectoDanio(20)
        );

        Equipamiento arma = new Arma("Espada sagrada", 8);

        escuela = new Escuela(
            "Escuela Celestial",
            List.of(corte),
            List.of(arma)
        );
    }

    @Test
    void creaUnCultivadorDeEspada() {
        Combatiente combatiente = creador.crear(
            "Espada", "Guerrero", escuela
        );

        assertInstanceOf(CultivadorEspada.class, combatiente);
    }

    @Test
    void creaUnMonjeDelCuerpoCelestial() {
        Combatiente combatiente = creador.crear(
            "Monje", "Monje", escuela
        );

        assertInstanceOf(MonjeCuerpoCelestial.class, combatiente);
    }

    @Test
    void creaUnHechiceroDelVacio() {
        Combatiente combatiente = creador.crear(
            "Hechicero", "Hechicero", escuela
        );

        assertInstanceOf(HechiceroVacio.class, combatiente);
    }

    @Test
    void asignaIdentificadoresConsecutivos() {
        Combatiente primero = creador.crear(
            "Espada", "Primero", escuela
        );

        Combatiente segundo = creador.crear(
            "Monje", "Segundo", escuela
        );

        Combatiente tercero = creador.crear(
            "Hechicero", "Tercero", escuela
        );

        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
        assertEquals(3, tercero.getId());
    }

    @Test
    void aplicaLasTecnicasInicialesDeLaEscuela() {
        Combatiente combatiente = creador.crear(
            "Espada", "Guerrero", escuela
        );

        assertEquals(1, combatiente.getTecnicas().size());
        assertEquals(
            "Corte Celestial",
            combatiente.getTecnicas().get(0).getNombre()
        );
    }

    @Test
    void aplicaElEquipamientoInicialDeLaEscuela() {
        Combatiente combatiente = creador.crear(
            "Espada", "Guerrero", escuela
        );

        assertEquals(1, combatiente.getEquipamientos().size());
        assertEquals(
            "Espada sagrada",
            combatiente.getEquipamientos().get(0).getNombre()
        );
    }

    @Test
    void aplicaLosEfectosDelEquipamiento() {
        Combatiente combatiente = creador.crear(
            "Espada", "Guerrero", escuela
        );

        assertEquals(21, combatiente.calcularDanio(10));
        assertEquals(100, combatiente.getVidaMaxima());
    }

    @Test
    void aceptaTiposSinDistinguirMayusculas() {
        Combatiente combatiente = creador.crear(
            "eSpAdA", "Guerrero", escuela
        );

        assertInstanceOf(CultivadorEspada.class, combatiente);
    }

    @Test
    void aceptaEspaciosAlrededorDelTipo() {
        Combatiente combatiente = creador.crear(
            "  Espada  ", "Guerrero", escuela
        );

        assertInstanceOf(CultivadorEspada.class, combatiente);
    }

    @Test
    void permiteRegistrarUnNuevoTipo() {
        creador.registrarTipo(
            "GuerreroEspecial",
            CultivadorEspada::new
        );

        Combatiente combatiente = creador.crear(
            "GuerreroEspecial", "Especial", escuela
        );

        assertInstanceOf(CultivadorEspada.class, combatiente);
    }

    @Test
    void noPermiteRegistrarUnTipoExistente() {
        assertThrows(
            IllegalArgumentException.class,
            () -> creador.registrarTipo(
                "Espada",
                CultivadorEspada::new
            )
        );
    }

    @Test
    void noPermiteRegistrarUnTipoConNombreVacio() {
        assertThrows(
            IllegalArgumentException.class,
            () -> creador.registrarTipo(
                " ",
                CultivadorEspada::new
            )
        );
    }

    @Test
    void noPermiteRegistrarUnConstructorNulo() {
        assertThrows(
            NullPointerException.class,
            () -> creador.registrarTipo("Nuevo", null)
        );
    }

    @Test
    void noPermiteCrearUnTipoDesconocido() {
        assertThrows(
            IllegalArgumentException.class,
            () -> creador.crear("Desconocido", "Guerrero", escuela)
        );
    }

    @Test
    void noPermiteCrearUnTipoNulo() {
        assertThrows(
            IllegalArgumentException.class,
            () -> creador.crear(null, "Guerrero", escuela)
        );
    }

    @Test
    void noPermiteCrearUnTipoVacio() {
        assertThrows(
            IllegalArgumentException.class,
            () -> creador.crear(" ", "Guerrero", escuela)
        );
    }

    @Test
    void noPermiteCrearSinEscuela() {
        assertThrows(
            NullPointerException.class,
            () -> creador.crear("Espada", "Guerrero", null)
        );
    }

    @Test
    void noConsumeUnIdentificadorCuandoFallaLaCreacion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> creador.crear("Espada", "", escuela)
        );

        Combatiente combatiente = creador.crear(
            "Espada", "Guerrero válido", escuela
        );

        assertEquals(1, combatiente.getId());
    }
}
