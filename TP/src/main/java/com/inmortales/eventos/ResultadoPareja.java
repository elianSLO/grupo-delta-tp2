package com.inmortales.eventos;

public class ResultadoPareja {

    private final int idPareja;
    private final int ultimaRonda;
    private final int vidaFinal;

    public ResultadoPareja(int idPareja, int ultimaRonda, int vidaFinal) {
        if (idPareja <= 0 || ultimaRonda <= 0 || vidaFinal < 0) {
            throw new IllegalArgumentException(
                "Los datos del resultado no son válidos."
            );
        }

        this.idPareja = idPareja;
        this.ultimaRonda = ultimaRonda;
        this.vidaFinal = vidaFinal;
    }
    public int getIdPareja() {
        return idPareja;
    }

    public int getUltimaRonda() {
        return ultimaRonda;
    }

    public int getVidaFinal() {
        return vidaFinal;
    }
}