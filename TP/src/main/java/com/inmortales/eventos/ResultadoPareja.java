package com.inmortales.eventos;

import java.util.List;
import java.util.Objects;

public class ResultadoPareja {

    private final int idPareja;
    private final int ultimaRonda;
    private final int vidaFinal;
    private final List<VidaIntegrante> integrantes;

    public ResultadoPareja(int idPareja, int ultimaRonda, int vidaFinal) {
        this(idPareja, ultimaRonda, vidaFinal, List.of());
    }

    public ResultadoPareja(
            int idPareja,
            int ultimaRonda,
            int vidaFinal,
            List<VidaIntegrante> integrantes) {

        if (idPareja <= 0 || ultimaRonda <= 0 || vidaFinal < 0) {
            throw new IllegalArgumentException(
                "Los datos del resultado no son válidos."
            );
        }

        Objects.requireNonNull(integrantes);

        // Si se informan los integrantes, su vida tiene que sumar
        // exactamente la vida final de la pareja.
        int suma = integrantes.stream()
            .mapToInt(VidaIntegrante::vida)
            .sum();

        if (!integrantes.isEmpty() && suma != vidaFinal) {
            throw new IllegalArgumentException(
                "La vida de los integrantes (" + suma
                + ") no suma la vida final de la pareja ("
                + vidaFinal + ")."
            );
        }

        this.idPareja = idPareja;
        this.ultimaRonda = ultimaRonda;
        this.vidaFinal = vidaFinal;
        this.integrantes = List.copyOf(integrantes);
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

    public List<VidaIntegrante> getIntegrantes() {
        return integrantes;
    }
}
