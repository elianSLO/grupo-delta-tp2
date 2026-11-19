package com.inmortales.combate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.inmortales.combate.estrategia.AccionCombate;
import com.inmortales.combate.estrategia.EstrategiaCombate;
import com.inmortales.eventos.Evento;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.eventos.TipoEvento;
import com.inmortales.eventos.VidaIntegrante;
import com.inmortales.modelo.Combatiente;
import com.inmortales.modelo.Pareja;
import com.inmortales.modelo.Tecnica;

public class Combate {

    private static final int DANIO_ATAQUE_BASICO = 10;

    private final Pareja pareja1;
    private final Pareja pareja2;
    private final EstrategiaCombate estrategia;
    private final ConfiguracionCombate configuracion;

    private final RegistroTecnicasCombate registro;

    private int numeroAsalto;
    private Pareja ganadora;
    
    private final GestorEventos gestorEventos;
    private final int numeroRonda;

    public Combate(
            Pareja pareja1,
            Pareja pareja2,
            EstrategiaCombate estrategia,
            ConfiguracionCombate configuracion,
            GestorEventos gestorEventos,
            int numeroRonda) {

        this.pareja1 = Objects.requireNonNull(pareja1);
        this.pareja2 = Objects.requireNonNull(pareja2);
        this.estrategia = Objects.requireNonNull(estrategia);
        this.configuracion = Objects.requireNonNull(configuracion);
        this.gestorEventos = Objects.requireNonNull(gestorEventos);

        if (pareja1.equals(pareja2)) {
            throw new IllegalArgumentException(
                "Una pareja no puede enfrentarse a sí misma."
            );
        }

        if (numeroRonda <= 0) {
            throw new IllegalArgumentException(
                "El número de ronda debe ser positivo."
            );
        }

        this.numeroRonda = numeroRonda;
        this.registro = new RegistroTecnicasCombate();
        this.numeroAsalto = 0;
    }

    public Pareja ejecutar() {

        prepararCombatientes();

        publicarEvento(
        	    TipoEvento.INICIO_COMBATE,
        	    "Comienza el combate entre las parejas "
        	        + pareja1.getId() + " y " + pareja2.getId() + ".",
        	    null,
        	    null,
        	    pareja1.getId(),
        	    pareja1.getVidaTotal(),
        	    pareja2.getId(),
        	    pareja2.getVidaTotal(),
        	    null
        	);

        while (!hayGanadora()
                && numeroAsalto < configuracion.getMaxAsaltos()) {

            numeroAsalto++;
            ejecutarAsalto();
        }

        determinarGanadora();

        publicarEvento(
                TipoEvento.FIN_COMBATE,
                "Finaliza el combate de la ronda " + numeroRonda
                    + ". Gana la pareja " + ganadora.getId()
                    + ". Vida total: pareja " + pareja1.getId()
                    + " = " + pareja1.getVidaTotal()
                    + ", pareja " + pareja2.getId()
                    + " = " + pareja2.getVidaTotal() + ".",
                null,
                null,
                pareja1.getId(),
                pareja1.getVidaTotal(),
                pareja2.getId(),
                pareja2.getVidaTotal(),
                ganadora.getId(),
                registrarVidasIntegrantes()
            );

            return ganadora;
        }

    
    private void publicarEvento(
            TipoEvento tipo,
            String descripcion,
            Integer idCombatiente,
            Integer idObjetivo,
            Integer idPareja1,
            Integer vidaPareja1,
            Integer idPareja2,
            Integer vidaPareja2,
            Integer idParejaGanadora) {

        publicarEvento(tipo, descripcion, idCombatiente, idObjetivo,
            idPareja1, vidaPareja1, idPareja2, vidaPareja2,
            idParejaGanadora, List.of());
    }

    private void publicarEvento(
            TipoEvento tipo,
            String descripcion,
            Integer idCombatiente,
            Integer idObjetivo,
            Integer idPareja1,
            Integer vidaPareja1,
            Integer idPareja2,
            Integer vidaPareja2,
            Integer idParejaGanadora,
            List<VidaIntegrante> vidasIntegrantes) {

        Evento evento = new Evento(
            tipo,
            descripcion,
            numeroRonda,
            numeroAsalto,
            idCombatiente,
            idObjetivo,
            idPareja1,
            vidaPareja1,
            idPareja2,
            vidaPareja2,
            idParejaGanadora,
            vidasIntegrantes
        );

        gestorEventos.publicar(evento);
    }

    // Foto de la vida de cada integrante al terminar el combate
    private List<VidaIntegrante> registrarVidasIntegrantes() {

        List<VidaIntegrante> vidas = new ArrayList<>();

        for (Pareja pareja : List.of(pareja1, pareja2)) {
            for (Combatiente combatiente : pareja.getCombatientes()) {
                vidas.add(new VidaIntegrante(
                    pareja.getId(),
                    combatiente.getId(),
                    combatiente.getNombre(),
                    combatiente.getVida()
                ));
            }
        }

        return vidas;
    }

    private void prepararCombatientes() {

        for (Combatiente combatiente : obtenerCombatientes()) {
            combatiente.reiniciarParaCombate();
        }
    }

    private void ejecutarAsalto() {

        recuperarEnergia();

        for (Combatiente combatiente : obtenerOrdenActuacion()) {

            if (hayGanadora()) {
                return;
            }

            if (combatiente.estaEnPie()) {
                ejecutarTurno(combatiente);
            }
        }
    }

    private void recuperarEnergia() {

        int cantidad = configuracion.getRecuperacionEnergia();

        for (Combatiente combatiente : obtenerCombatientes()) {

            if (combatiente.estaEnPie()) {
                combatiente.recuperarEnergia(cantidad);
            }
        }
    }

    private void ejecutarTurno(Combatiente combatiente) {

        int danioVeneno = combatiente.aplicarEfectosInicioTurno();

        if (danioVeneno > 0) {
            int turnosRestantes = combatiente.getTurnosVeneno();

            publicarEvento(
                TipoEvento.EFECTO_TEMPORAL,
                combatiente.getNombre() + " sufre " + danioVeneno
                    + " puntos de daño por veneno"
                    + (turnosRestantes > 0
                        ? (turnosRestantes == 1
                            ? " (le queda 1 turno)."
                            : " (le quedan " + turnosRestantes + " turnos).")
                        : " y el veneno se termina."),
                combatiente.getId(),
                null,
                null, null, null, null, null
            );
        }

        if (!combatiente.estaEnPie()) {
            verificarEliminacion(combatiente);
            return;
        }

        Pareja pareja = obtenerPareja(combatiente);
        Pareja rival = obtenerRival(pareja);

        AccionCombate accion = estrategia.elegirAccion(
            combatiente,
            pareja,
            registro
        );

        if (accion.esAtaqueBasico()) {
            ejecutarAtaqueBasico(combatiente, rival);
        } else {
            ejecutarTecnica(combatiente, rival, accion.getTecnica());
        }
    }

    private void ejecutarAtaqueBasico(
            Combatiente ejecutor,
            Pareja rival) {

        Combatiente objetivo = seleccionarObjetivo(rival);

        if (objetivo == null) {
            return;
        }

        int vidaAnterior = objetivo.getVida();

        int danio = ejecutor.calcularDanio(DANIO_ATAQUE_BASICO);

        objetivo.recibirDanio(danio);

        publicarEvento(
            TipoEvento.ATAQUE_BASICO,
            ejecutor.getNombre() + " realiza un ataque básico contra "
                + objetivo.getNombre() + " y causa "
                + (vidaAnterior - objetivo.getVida()) + " puntos de daño.",
            ejecutor.getId(),
            objetivo.getId(),
            null, null, null, null, null
        );

        verificarEliminacion(objetivo);
    }

    private void ejecutarTecnica(
            Combatiente ejecutor,
            Pareja rival,
            Tecnica tecnica) {

        Combatiente objetivo;

        switch (tecnica.getEfecto().getTipo()) {
            case OFENSIVO:
                objetivo = seleccionarObjetivo(rival);
                break;

            case DEFENSIVO:
            case RECUPERACION:
                objetivo = ejecutor;
                break;

            default:
                throw new IllegalStateException(
                    "Tipo de efecto no reconocido."
                );
        }

        if (objetivo == null) {
            return;
        }

        if (!ejecutor.registrarUsoTecnica(tecnica)) {
            return;
        }

        registro.registrarUso(ejecutor, tecnica);

        int vidaAnterior = objetivo.getVida();
        int energiaAnterior = objetivo.getEnergia();
        int venenoAnterior = objetivo.getTurnosVeneno();

        tecnica.ejecutar(ejecutor, objetivo);

        publicarEvento(
            TipoEvento.USO_TECNICA,
            ejecutor.getNombre() + " utiliza " + tecnica.getNombre()
                + " sobre " + objetivo.getNombre()
                + describirEfecto(objetivo, vidaAnterior, energiaAnterior,
                    venenoAnterior)
                + ".",
            ejecutor.getId(),
            objetivo.getId(),
            null, null, null, null, null
        );

        verificarEliminacion(objetivo);
    }
    
    private String describirEfecto(
            Combatiente objetivo,
            int vidaAnterior,
            int energiaAnterior,
            int venenoAnterior) {

        int diferenciaVida = objetivo.getVida() - vidaAnterior;
        int energiaQuitada = energiaAnterior - objetivo.getEnergia();

        StringBuilder detalle = new StringBuilder();

        if (diferenciaVida < 0) {
            detalle.append(" y causa ")
                .append(-diferenciaVida)
                .append(" puntos de daño");
        } else if (diferenciaVida > 0) {
            detalle.append(" y recupera ")
                .append(diferenciaVida)
                .append(" puntos de vida");
        }

        if (energiaQuitada > 0) {
            detalle.append(" y le quita ")
                .append(energiaQuitada)
                .append(" puntos de energía");
        }

        if (objetivo.getTurnosVeneno() > venenoAnterior) {
            detalle.append(" y lo envenena por ")
                .append(objetivo.getTurnosVeneno())
                .append(" turnos");
        }

        if (detalle.length() == 0 && objetivo.tieneDefensaActiva()) {
            detalle.append(" y activa su defensa");
        }

        return detalle.toString();
    }

    private void verificarEliminacion(Combatiente combatiente) {

        if (combatiente.estaEnPie()) {
            return;
        }

        publicarEvento(
            TipoEvento.COMBATIENTE_ELIMINADO,
            combatiente.getNombre() + " ha sido eliminado del combate.",
            combatiente.getId(),
            null,
            null, null, null, null, null
        );
    }
    
    private Combatiente seleccionarObjetivo(Pareja rival) {

        return rival.getCombatientes().stream()
            .filter(Combatiente::estaEnPie)
            .min(Comparator
                .comparingInt(Combatiente::getVida)
                .thenComparingInt(Combatiente::getId))
            .orElse(null);
    }

    private List<Combatiente> obtenerOrdenActuacion() {

        return obtenerCombatientes().stream()
            .sorted(Comparator
                .comparingInt((Combatiente c) -> obtenerPareja(c).getId())
                .thenComparingInt(Combatiente::getId))
            .toList();
    }

    private List<Combatiente> obtenerCombatientes() {

        return List.of(
            pareja1.getCombatientes().get(0),
            pareja1.getCombatientes().get(1),
            pareja2.getCombatientes().get(0),
            pareja2.getCombatientes().get(1)
        );
    }

    private Pareja obtenerPareja(Combatiente combatiente) {

        if (pareja1.getCombatientes().contains(combatiente)) {
            return pareja1;
        }

        return pareja2;
    }

    private Pareja obtenerRival(Pareja pareja) {

        return pareja.equals(pareja1) ? pareja2 : pareja1;
    }

    private boolean hayGanadora() {

        return pareja1.estaDerrotada() || pareja2.estaDerrotada();
    }

    private void determinarGanadora() {

        if (pareja1.estaDerrotada()) {
            ganadora = pareja2;
        } else if (pareja2.estaDerrotada()) {
            ganadora = pareja1;
        } else if (pareja1.getVidaTotal() > pareja2.getVidaTotal()) {
            ganadora = pareja1;
        } else if (pareja2.getVidaTotal() > pareja1.getVidaTotal()) {
            ganadora = pareja2;
        } else {
            ganadora = pareja1.getId() < pareja2.getId()
                ? pareja1
                : pareja2;
        }
    }

    public Pareja getGanadora() {
        return ganadora;
    }

    public int getNumeroAsalto() {
        return numeroAsalto;
    }
}