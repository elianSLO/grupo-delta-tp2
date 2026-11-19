package com.inmortales.eventos;

import java.util.List;

import com.inmortales.modelo.Pareja;

public class HeraldoImperial implements ObservadorEventos {

	private static final String SEPARADOR =
		    "\n" + "-".repeat(80) + "\n";

		@Override
		public void actualizar(Evento evento) {

		    if (evento.getTipo() == TipoEvento.INICIO_COMBATE) {
		        System.out.println(SEPARADOR);
		        System.out.println("INICIO DEL COMBATE");
		    }

		    System.out.println(evento.getDescripcion());

		    if (evento.getTipo() == TipoEvento.FIN_COMBATE) {
		        System.out.println("FIN DEL COMBATE");
		        System.out.println(SEPARADOR);
		    }
		}
    
    public void presentarPosiciones(List<ResultadoPareja> posiciones) {
        System.out.println("\n========== TABLA DE POSICIONES ==========");

        int puesto = 1;

        for (ResultadoPareja resultado : posiciones) {
            System.out.println(
                puesto + "° puesto - Pareja " + resultado.getIdPareja()
                + " | Última ronda: " + resultado.getUltimaRonda()
                + " | Vida final: " + resultado.getVidaFinal()
                + detalleIntegrantes(resultado)
            );

            puesto++;
        }

        System.out.println("=========================================");
    }
    
    // Muestra cómo se llegó a la vida final: " (Goku 100 + Geralt 79)"
    private String detalleIntegrantes(ResultadoPareja resultado) {

        if (resultado.getIntegrantes().isEmpty()) {
            return "";
        }

        List<String> partes = resultado.getIntegrantes().stream()
            .map(v -> v.nombre() + " " + v.vida())
            .toList();

        return " (" + String.join(" + ", partes) + ")";
    }

    public void proclamarCampeona(Pareja campeona) {
        System.out.println("\n¡LA PAREJA CAMPEONA ES LA N° "
            + campeona.getId() + "!");
    }
}