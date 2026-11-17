package com.inmortales;

import java.util.List;

import com.inmortales.combate.ConfiguracionCombate;
import com.inmortales.combate.estrategia.EstrategiaPredeterminada;
import com.inmortales.eventos.CronistaSecta;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.eventos.HeraldoImperial;
import com.inmortales.fabricacion.CreadorCombatientes;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.EfectoDanio;
import com.inmortales.modelo.EfectoDanioYEnergia;
import com.inmortales.modelo.EfectoDefensivo;
import com.inmortales.modelo.EfectoRecuperacion;
import com.inmortales.modelo.Escuela;
import com.inmortales.modelo.Pareja;
import com.inmortales.modelo.Tecnica;
import com.inmortales.modelo.equipamiento.Arma;
import com.inmortales.modelo.equipamiento.Armadura;
import com.inmortales.modelo.equipamiento.Anillo;
import com.inmortales.torneo.Torneo;

public class Main {

    public static void main(String[] args) {

        // 1. Crear las técnicas
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

        // 2. Crear equipamientos
        Arma espada = new Arma("Espada Celestial", 8);
        Armadura armadura = new Armadura("Armadura Dorada", 5);
        Anillo anillo = new Anillo("Anillo de Energía", 15);

        // 3. Crear las escuelas
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

        // 4. Crear combatientes mediante Factory
        CreadorCombatientes creador = new CreadorCombatientes();

        Combatiente c1 = creador.crear(
            "Espada", "Kratos", escuelaEspada
        );

        Combatiente c2 = creador.crear(
            "Hechicero", "Loki", escuelaVacio
        );

        Combatiente c3 = creador.crear(
            "Monje", "Goku", escuelaMonje
        );

        Combatiente c4 = creador.crear(
            "Espada", "Geralt de Rivia", escuelaEspada
        );

        Combatiente c5 = creador.crear(
            "Espada", "Doom Slayer", escuelaEspada
        );

        Combatiente c6 = creador.crear(
            "Hechicero", "Elsa", escuelaVacio
        );

        Combatiente c7 = creador.crear(
            "Hechicero", "Gandalf", escuelaVacio
        );

        Combatiente c8 = creador.crear(
            "Espada", "John Wick", escuelaEspada
        );

        // 5. Formar parejas
        Pareja pareja1 = new Pareja(1, c1, c2);
        Pareja pareja2 = new Pareja(2, c3, c4);
        Pareja pareja3 = new Pareja(3, c5, c6);
        Pareja pareja4 = new Pareja(4, c7, c8);

        // 6. Configurar los observadores
        GestorEventos gestorEventos = new GestorEventos();

        HeraldoImperial heraldo = new HeraldoImperial();
        CronistaSecta cronista = new CronistaSecta();

        gestorEventos.suscribir(heraldo);
        gestorEventos.suscribir(cronista);

        // 7. Configurar la estrategia y los combates
        EstrategiaPredeterminada estrategia =
            new EstrategiaPredeterminada();

        ConfiguracionCombate configuracion =
            new ConfiguracionCombate(10, 5);

        // 8. Crear el torneo
        Torneo torneo = new Torneo(
            estrategia,
            configuracion,
            gestorEventos
        );

        torneo.inscribirPareja(pareja1);
        torneo.inscribirPareja(pareja2);
        torneo.inscribirPareja(pareja3);
        torneo.inscribirPareja(pareja4);

        // 9. Iniciar el torneo
        Pareja campeona = torneo.iniciar();

        // 10. Mostrar resultados
        heraldo.proclamarCampeona(campeona);
        heraldo.presentarPosiciones(cronista.obtenerPosiciones());
    }

}
