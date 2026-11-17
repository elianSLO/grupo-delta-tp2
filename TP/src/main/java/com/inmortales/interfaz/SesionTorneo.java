package com.inmortales.interfaz;

import com.inmortales.eventos.CronistaSecta;
import com.inmortales.eventos.GestorEventos;
import com.inmortales.eventos.HeraldoImperial;
import com.inmortales.torneo.Torneo;

public class SesionTorneo {

    private final Torneo torneo;
    private final GestorEventos gestorEventos;
    private final CronistaSecta cronista;
    private final HeraldoImperial heraldo;

    public SesionTorneo(
        Torneo torneo,
        GestorEventos gestorEventos,
        CronistaSecta cronista,
        HeraldoImperial heraldo
    ) {
        this.torneo = torneo;
        this.gestorEventos = gestorEventos;
        this.cronista = cronista;
        this.heraldo = heraldo;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public GestorEventos getGestorEventos() {
        return gestorEventos;
    }

    public CronistaSecta getCronista() {
        return cronista;
    }

    public HeraldoImperial getHeraldo() {
        return heraldo;
    }
}
