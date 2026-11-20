package com.inmortales.eventos;

import java.util.List;
import java.util.stream.Collectors;

import com.inmortales.modelo.Pareja;

// Única clase del sistema que escribe en pantalla.
// Solo da formato: no decide nada del torneo ni del combate.
// Usa líneas hechas con caracteres simples (=, -, +, |) para que se vea
// bien en la consola de Eclipse en cualquier sistema.
public class HeraldoImperial implements ObservadorEventos {

    private static final int ANCHO = 76;

    private static final String LINEA_DOBLE = "=".repeat(ANCHO);
    private static final String LINEA_SIMPLE = "-".repeat(ANCHO);

    private int combatesAnunciados = 0;
    private int asaltoActual = 0;

    @Override
    public void actualizar(Evento evento) {

        switch (evento.getTipo()) {
            case INICIO_TORNEO -> anunciarInicioTorneo(evento);
            case INICIO_COMBATE -> anunciarInicioCombate(evento);
            case ATAQUE_BASICO -> anunciarAccion(evento, "ATAQUE");
            case USO_TECNICA -> anunciarAccion(evento, "TÉCNICA");
            case EFECTO_TEMPORAL -> anunciarAccion(evento, "VENENO");
            case COMBATIENTE_ELIMINADO -> anunciarEliminacion(evento);
            case FIN_COMBATE -> anunciarFinCombate(evento);
            case CAMPEON_PROCLAMADO -> anunciarCampeona(evento);
        }
    }

    // ------------------------------------------------------------
    // Acontecimientos del torneo
    // ------------------------------------------------------------

    private void anunciarInicioTorneo(Evento evento) {
        System.out.println();
        System.out.println(LINEA_DOBLE);
        System.out.println(centrar("EL TORNEO DE LOS INMORTALES"));
        System.out.println(centrar("La elección de los Dos Santos Supremos"));
        System.out.println(LINEA_DOBLE);
        System.out.println(centrar(evento.getDescripcion()));
    }

    private void anunciarCampeona(Evento evento) {
        System.out.println();
        System.out.println(LINEA_DOBLE);
        System.out.println(centrar("*   *   *"));
        System.out.println(centrar(evento.getDescripcion()));
        System.out.println(centrar("*   *   *"));
        System.out.println(LINEA_DOBLE);
    }

    // ------------------------------------------------------------
    // Acontecimientos del combate
    // ------------------------------------------------------------

    private void anunciarInicioCombate(Evento evento) {

        combatesAnunciados++;
        asaltoActual = 0;

        System.out.println();
        System.out.println("+" + LINEA_SIMPLE.substring(2) + "+");
        System.out.println(enCaja("COMBATE " + combatesAnunciados
            + "  -  RONDA " + evento.getNumeroRonda()));
        System.out.println(enCaja(""));
        System.out.println(enCaja(describirPareja(evento, evento.getIdPareja1())));
        System.out.println(enCaja("            contra"));
        System.out.println(enCaja(describirPareja(evento, evento.getIdPareja2())));
        System.out.println("+" + LINEA_SIMPLE.substring(2) + "+");
    }

    private void anunciarAccion(Evento evento, String etiqueta) {
        anunciarAsaltoSiCambio(evento);
        System.out.printf("   %-9s %s%n", "[" + etiqueta + "]", evento.getDescripcion());
    }

    private void anunciarEliminacion(Evento evento) {
        anunciarAsaltoSiCambio(evento);
        System.out.println("   >>> " + evento.getDescripcion().toUpperCase() + " <<<");
    }

    private void anunciarFinCombate(Evento evento) {

        System.out.println();
        System.out.println("   " + "-".repeat(ANCHO - 6));
        System.out.println("   RESULTADO: gana la PAREJA " + evento.getIdParejaGanadora()
            + " en el asalto " + evento.getNumeroAsalto());
        System.out.println(lineaResultado(evento, evento.getIdPareja1(), evento.getVidaPareja1()));
        System.out.println(lineaResultado(evento, evento.getIdPareja2(), evento.getVidaPareja2()));
        System.out.println("   " + "-".repeat(ANCHO - 6));
    }

    // Imprime "Asalto N" una sola vez, antes de la primera acción del asalto.
    private void anunciarAsaltoSiCambio(Evento evento) {

        if (evento.getNumeroAsalto() != asaltoActual) {
            asaltoActual = evento.getNumeroAsalto();
            System.out.println();
            System.out.println("  -- Asalto " + asaltoActual + " --");
        }
    }

    // ------------------------------------------------------------
    // Tabla de posiciones (la pide el main al Cronista)
    // ------------------------------------------------------------

    public void presentarPosiciones(List<ResultadoPareja> posiciones) {

        System.out.println();
        System.out.println(LINEA_DOBLE);
        System.out.println(centrar("TABLA DE POSICIONES"));
        System.out.println(LINEA_DOBLE);
        System.out.printf(" %-7s %-8s %-7s %-6s  %s%n",
            "Puesto", "Pareja", "Ronda", "Vida", "Integrantes (vida final)");
        System.out.println(LINEA_SIMPLE);

        int puesto = 1;

        for (ResultadoPareja resultado : posiciones) {
            System.out.printf(" %-7s %-8s %-7d %-6d  %s%n",
                puesto + "°",
                "N° " + resultado.getIdPareja(),
                resultado.getUltimaRonda(),
                resultado.getVidaFinal(),
                detalleIntegrantes(resultado));
            puesto++;
        }

        System.out.println(LINEA_DOBLE);
    }

    public void proclamarCampeona(Pareja campeona) {
        System.out.println();
        System.out.println(centrar("LA PAREJA CAMPEONA ES LA N° " + campeona.getId()));
    }

    // ------------------------------------------------------------
    // Ayudas de formato
    // ------------------------------------------------------------

    // "Pareja 1:  Kratos y Loki"
    private String describirPareja(Evento evento, Integer idPareja) {

        String nombres = evento.getVidasIntegrantes().stream()
            .filter(v -> idPareja != null && v.idPareja() == idPareja)
            .map(VidaIntegrante::nombre)
            .collect(Collectors.joining(" y "));

        return "Pareja " + idPareja + (nombres.isEmpty() ? "" : ":  " + nombres);
    }

    // "   Pareja 1   55 de vida   (Kratos 0 + Loki 55)"
    private String lineaResultado(Evento evento, Integer idPareja, Integer vida) {

        String detalle = evento.getVidasIntegrantes().stream()
            .filter(v -> idPareja != null && v.idPareja() == idPareja)
            .map(v -> v.nombre() + " " + v.vida())
            .collect(Collectors.joining(" + "));

        return String.format("     Pareja %-3d %4d de vida   %s",
            idPareja, vida, detalle.isEmpty() ? "" : "(" + detalle + ")");
    }

    private String detalleIntegrantes(ResultadoPareja resultado) {
        return resultado.getIntegrantes().stream()
            .map(v -> v.nombre() + " " + v.vida())
            .collect(Collectors.joining(" + "));
    }

    private String centrar(String texto) {
        int espacios = Math.max(0, (ANCHO - texto.length()) / 2);
        return " ".repeat(espacios) + texto;
    }

    private String enCaja(String texto) {
        int ancho = ANCHO - 6;
        String recortado = texto.length() > ancho ? texto.substring(0, ancho) : texto;
        return "|  " + String.format("%-" + ancho + "s", recortado) + "  |";
    }
}
