package com.inmortales;

import java.util.List;
import java.util.Map;

import com.inmortales.combate.ConfiguracionCombate;
import com.inmortales.combate.estrategia.EstrategiaPredeterminada;
import com.inmortales.fabricacion.CreadorCombatientes;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.EfectoDanio;
import com.inmortales.modelo.EfectoDanioYEnergia;
import com.inmortales.modelo.EfectoDefensivo;
import com.inmortales.modelo.EfectoRecuperacion;
import com.inmortales.modelo.Escuela;
import com.inmortales.modelo.Tecnica;
import com.inmortales.modelo.equipamiento.Arma;
import com.inmortales.modelo.equipamiento.Armadura;
import com.inmortales.modelo.equipamiento.Anillo;
import com.inmortales.torneo.Torneo;
import com.inmortales.eventos.GestorEventos;

public class ConfiguracionJuego {

    private final CreadorCombatientes creador;
    private final Map<String, Escuela> escuelas;

    public ConfiguracionJuego() {

        // Técnicas
        Tecnica corteCelestial = new Tecnica(
            "Corte Celestial", 8, new EfectoDanio(20)
        );

        Tecnica muroDorado = new Tecnica(
            "Muro Dorado", 10, new EfectoDefensivo()
        );

        Tecnica dominioVacio = new Tecnica(
            "Dominio del Vacío", 12,
            new EfectoDanioYEnergia(18, 8)
        );

        Tecnica meditacion = new Tecnica(
            "Meditación Inmortal", 10,
            new EfectoRecuperacion(20)
        );

        // Equipamientos
        Arma espada = new Arma("Espada Celestial", 8);
        Armadura armadura = new Armadura("Armadura Dorada", 5);
        Anillo anillo = new Anillo("Anillo de Energía", 15);

        // Escuelas
        Escuela escuelaEspada = new Escuela(
            "Escuela de la Espada Celestial",
            List.of(corteCelestial, meditacion),
            List.of(espada)
        );

        Escuela escuelaMonje = new Escuela(
            "Monasterio del Cuerpo Dorado",
            List.of(muroDorado, meditacion),
            List.of(armadura)
        );

        Escuela escuelaVacio = new Escuela(
            "Palacio del Vacío",
            List.of(dominioVacio, meditacion),
            List.of(anillo)
        );

        escuelas = Map.of(
            "Escuela de la Espada Celestial", escuelaEspada,
            "Monasterio del Cuerpo Dorado", escuelaMonje,
            "Palacio del Vacío", escuelaVacio
        );

        // Factory
        creador = new CreadorCombatientes();
    }

    public List<String> getNombresEscuelas() {
        return List.of(
            "Escuela de la Espada Celestial",
            "Monasterio del Cuerpo Dorado",
            "Palacio del Vacío"
        );
    }

    public Escuela getEscuela(String nombre) {
        Escuela escuela = escuelas.get(nombre);

        if (escuela == null) {
            throw new IllegalArgumentException("Escuela inexistente.");
        }

        return escuela;
    }

    public Combatiente crearCombatiente(
        String tipo, String nombre, String nombreEscuela
    ) {
        return creador.crear(tipo, nombre, getEscuela(nombreEscuela));
    }

    public Torneo crearTorneo(GestorEventos gestorEventos) {
        return new Torneo(
            new EstrategiaPredeterminada(),
            new ConfiguracionCombate(10, 5),
            gestorEventos
        );
    }
}
