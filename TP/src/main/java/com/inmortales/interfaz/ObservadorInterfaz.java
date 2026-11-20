package com.inmortales.interfaz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;

import com.inmortales.eventos.Evento;
import com.inmortales.eventos.ObservadorEventos;
import com.inmortales.eventos.TipoEvento;
import com.inmortales.eventos.VidaIntegrante;

// Tercer interesado en los acontecimientos (además del Heraldo y del
// Cronista): los guarda agrupados por combate y los muestra en la
// ventana con colores. No toca ninguna regla del torneo.
public class ObservadorInterfaz implements ObservadorEventos {

    private final JEditorPane areaEventos;
    private final List<List<Evento>> eventosPorCombate;

    private int indiceCombateActual;

    public ObservadorInterfaz(JEditorPane areaEventos) {
        this.areaEventos = areaEventos;
        this.eventosPorCombate = new ArrayList<>();
        this.indiceCombateActual = -1;
    }

    @Override
    public synchronized void actualizar(Evento evento) {

        // Cada INICIO_COMBATE crea un nuevo grupo de eventos.
        if (evento.getTipo() == TipoEvento.INICIO_COMBATE) {
            eventosPorCombate.add(new ArrayList<>());
            indiceCombateActual = eventosPorCombate.size() - 1;
        }

        // Ignoramos los eventos que no pertenecen a un combate.
        if (indiceCombateActual < 0) {
            return;
        }

        eventosPorCombate.get(indiceCombateActual).add(evento);

        int indiceActual = indiceCombateActual;

        SwingUtilities.invokeLater(() -> {
            synchronized (ObservadorInterfaz.this) {
                // Actualizamos la pantalla solamente si sigue seleccionado
                // el combate al que pertenece el evento.
                if (indiceCombateActual == indiceActual) {
                    mostrarCombate(indiceActual);
                }
            }
        });
    }

    public synchronized void mostrarCombate(int indice) {

        if (indice < 0 || indice >= eventosPorCombate.size()) {
            return;
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:SansSerif; font-size:12px;"
            + " color:#22252B; margin:10px'>");

        int asaltoActual = 0;

        for (Evento evento : eventosPorCombate.get(indice)) {

            boolean esAccion = evento.getTipo() == TipoEvento.ATAQUE_BASICO
                || evento.getTipo() == TipoEvento.USO_TECNICA
                || evento.getTipo() == TipoEvento.EFECTO_TEMPORAL
                || evento.getTipo() == TipoEvento.COMBATIENTE_ELIMINADO;

            if (esAccion && evento.getNumeroAsalto() != asaltoActual) {
                asaltoActual = evento.getNumeroAsalto();
                html.append("<p style='margin-top:12px; margin-bottom:4px;"
                    + " color:" + Estilo.HTML_SUAVE + "'><b>ASALTO ")
                    .append(asaltoActual).append("</b></p>");
            }

            html.append(renglon(evento, indice));
        }

        html.append("</body></html>");

        areaEventos.setText(html.toString());
        areaEventos.setCaretPosition(0);
    }

    // Un acontecimiento convertido en un renglón con color.
    private String renglon(Evento evento, int indice) {

        String texto = escapar(evento.getDescripcion());

        return switch (evento.getTipo()) {

            case INICIO_COMBATE ->
                "<table width='100%' cellpadding='8' cellspacing='0' bgcolor='#1F2A44'>"
                + "<tr><td><font color='white'>"
                + "<span style='font-size:15px'><b>Combate " + (indice + 1)
                + " &nbsp;&middot;&nbsp; Ronda " + evento.getNumeroRonda()
                + "</b></span><br>"
                + nombresPareja(evento, evento.getIdPareja1())
                + " &nbsp;<b>contra</b>&nbsp; "
                + nombresPareja(evento, evento.getIdPareja2())
                + "</font></td></tr></table>";

            case ATAQUE_BASICO -> etiqueta("ATAQUE", Estilo.HTML_ATAQUE, texto);
            case USO_TECNICA -> etiqueta("TÉCNICA", Estilo.HTML_TECNICA, texto);
            case EFECTO_TEMPORAL -> etiqueta("VENENO", Estilo.HTML_VENENO, texto);

            case COMBATIENTE_ELIMINADO ->
                "<p style='margin:2px 0'><font color='" + Estilo.HTML_ELIMINADO
                + "'><b>[ELIMINADO] " + texto + "</b></font></p>";

            case FIN_COMBATE ->
                "<br><table width='100%' cellpadding='8' cellspacing='0' bgcolor='#FFF6D6'>"
                + "<tr><td><b>Gana la pareja " + evento.getIdParejaGanadora()
                + "</b> en el asalto " + evento.getNumeroAsalto() + "<br>"
                + resultadoPareja(evento, evento.getIdPareja1(), evento.getVidaPareja1())
                + "<br>"
                + resultadoPareja(evento, evento.getIdPareja2(), evento.getVidaPareja2())
                + "</td></tr></table>";

            default -> "<p style='margin:2px 0'>" + texto + "</p>";
        };
    }

    private String etiqueta(String nombre, String color, String texto) {
        return "<p style='margin:2px 0'><font color='" + color + "'><b>["
            + nombre + "]</b></font> " + texto + "</p>";
    }

    private String nombresPareja(Evento evento, Integer idPareja) {

        String nombres = integrantes(evento, idPareja).stream()
            .map(v -> escapar(v.nombre()))
            .collect(Collectors.joining(" y "));

        return "Pareja " + idPareja + (nombres.isEmpty() ? "" : " (" + nombres + ")");
    }

    private String resultadoPareja(Evento evento, Integer idPareja, Integer vida) {

        String detalle = integrantes(evento, idPareja).stream()
            .map(v -> escapar(v.nombre()) + " " + v.vida())
            .collect(Collectors.joining(" + "));

        return "Pareja " + idPareja + ": <b>" + vida + "</b> de vida"
            + (detalle.isEmpty() ? "" : " &nbsp;<font color='"
                + Estilo.HTML_SUAVE + "'>(" + detalle + ")</font>");
    }

    private List<VidaIntegrante> integrantes(Evento evento, Integer idPareja) {
        return evento.getVidasIntegrantes().stream()
            .filter(v -> idPareja != null && v.idPareja() == idPareja)
            .toList();
    }

    private String escapar(String texto) {
        return texto.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

    public synchronized int getCantidadCombates() {
        return eventosPorCombate.size();
    }

    public synchronized int getIndiceCombateActual() {
        return indiceCombateActual;
    }
}
