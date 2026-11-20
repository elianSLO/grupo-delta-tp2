package com.inmortales.eventos;

import java.util.List;
import java.util.Objects;

public class Evento {

    private final TipoEvento tipo;
    private final String descripcion;
    private final int numeroRonda;
    private final int numeroAsalto;

    private final Integer idCombatiente;
    private final Integer idObjetivo;

    private final Integer idPareja1;
    private final Integer vidaPareja1;

    private final Integer idPareja2;
    private final Integer vidaPareja2;

    private final Integer idParejaGanadora;

    // Solo lo completa FIN_COMBATE: la vida de cada integrante al terminar.
    private final List<VidaIntegrante> vidasIntegrantes;

    public Evento(
            TipoEvento tipo,
            String descripcion,
            int numeroRonda,
            int numeroAsalto,
            Integer idCombatiente,
            Integer idObjetivo,
            Integer idPareja1,
            Integer vidaPareja1,
            Integer idPareja2,
            Integer vidaPareja2,
            Integer idParejaGanadora) {

        this(tipo, descripcion, numeroRonda, numeroAsalto,
            idCombatiente, idObjetivo,
            idPareja1, vidaPareja1, idPareja2, vidaPareja2,
            idParejaGanadora, List.of());
    }

    public Evento(
            TipoEvento tipo,
            String descripcion,
            int numeroRonda,
            int numeroAsalto,
            Integer idCombatiente,
            Integer idObjetivo,
            Integer idPareja1,
            Integer vidaPareja1,
            Integer idPareja2,
            Integer vidaPareja2,
            Integer idParejaGanadora,
            List<VidaIntegrante> vidasIntegrantes) {

    	this.tipo = Objects.requireNonNull(tipo);
    	this.descripcion = Objects.requireNonNull(descripcion);
        this.numeroRonda = numeroRonda;
        this.numeroAsalto = numeroAsalto;
        this.idCombatiente = idCombatiente;
        this.idObjetivo = idObjetivo;
        this.idPareja1 = idPareja1;
        this.vidaPareja1 = vidaPareja1;
        this.idPareja2 = idPareja2;
        this.vidaPareja2 = vidaPareja2;
        this.idParejaGanadora = idParejaGanadora;
        this.vidasIntegrantes = List.copyOf(
            Objects.requireNonNull(vidasIntegrantes)
        );
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNumeroRonda() {
        return numeroRonda;
    }

    public int getNumeroAsalto() {
        return numeroAsalto;
    }

    public Integer getIdCombatiente() {
        return idCombatiente;
    }

    public Integer getIdObjetivo() {
        return idObjetivo;
    }

    public Integer getIdPareja1() {
        return idPareja1;
    }

    public Integer getVidaPareja1() {
        return vidaPareja1;
    }

    public Integer getIdPareja2() {
        return idPareja2;
    }

    public Integer getVidaPareja2() {
        return vidaPareja2;
    }

    public Integer getIdParejaGanadora() {
        return idParejaGanadora;
    }

    public List<VidaIntegrante> getVidasIntegrantes() {
        return vidasIntegrantes;
    }
}
