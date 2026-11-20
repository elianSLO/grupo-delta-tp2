package com.inmortales.eventos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CronistaSecta implements ObservadorEventos {

    private final List<Evento> historial;
    private final Map<Integer, ResultadoPareja> resultados;

    public CronistaSecta() {
        historial = new ArrayList<>();
        resultados = new HashMap<>();
    }

    @Override
    public void actualizar(Evento evento) {
        historial.add(evento);

        if (evento.getTipo() == TipoEvento.FIN_COMBATE) {
            registrarResultado(
                evento.getIdPareja1(),
                evento.getVidaPareja1(),
                evento.getNumeroRonda(),
                evento.getVidasIntegrantes()
            );

            registrarResultado(
                evento.getIdPareja2(),
                evento.getVidaPareja2(),
                evento.getNumeroRonda(),
                evento.getVidasIntegrantes()
            );
        }
    }

    private void registrarResultado(
            Integer idPareja,
            Integer vidaFinal,
            int ronda,
            List<VidaIntegrante> vidasIntegrantes) {

        if (idPareja == null || vidaFinal == null) {
            return;
        }

        // Nos quedamos con los integrantes de esta pareja
        List<VidaIntegrante> integrantes = vidasIntegrantes.stream()
            .filter(v -> v.idPareja() == idPareja)
            .toList();

        // put reemplaza el resultado anterior: la tabla usa la vida
        // del último combate, no la suma de todas las rondas.
        resultados.put(
            idPareja,
            new ResultadoPareja(idPareja, ronda, vidaFinal, integrantes)
        );
    }

    public List<Evento> getHistorial() {
        return List.copyOf(historial);
    }

    public List<ResultadoPareja> obtenerPosiciones() {

        return resultados.values().stream()
            .sorted(
                Comparator
                    .comparingInt(ResultadoPareja::getUltimaRonda)
                    .reversed()
                    .thenComparing(
                        Comparator.comparingInt(
                            ResultadoPareja::getVidaFinal
                        ).reversed()
                    )
                    .thenComparingInt(ResultadoPareja::getIdPareja)
            )
            .toList();
    }
}