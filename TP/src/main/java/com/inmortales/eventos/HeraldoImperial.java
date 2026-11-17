package com.inmortales.eventos;

import java.util.List;

import com.inmortales.modelo.Pareja;

public class HeraldoImperial implements ObservadorEventos {

    @Override
    public void actualizar(Evento evento) {
        System.out.println(evento.getDescripcion());
    }
    
    public void presentarPosiciones(List<ResultadoPareja> posiciones) {
        System.out.println("\n========== TABLA DE POSICIONES ==========");

        int puesto = 1;

        for (ResultadoPareja resultado : posiciones) {
            System.out.println(
                puesto + "° puesto - Pareja " + resultado.getIdPareja()
                + " | Última ronda: " + resultado.getUltimaRonda()
                + " | Vida final: " + resultado.getVidaFinal()
            );

            puesto++;
        }

        System.out.println("=========================================");
    }
    
    public void proclamarCampeona(Pareja campeona) {
        System.out.println("\n¡LA PAREJA CAMPEONA ES LA N° "
            + campeona.getId() + "!");
    }
}