package com.inmortales.eventos;

import java.util.Objects;

// Vida de un integrante de una pareja en el momento en que terminó
// un combate. La guarda el evento FIN_COMBATE para poder mostrar
// cómo se llegó a la vida total de cada pareja.
public record VidaIntegrante(
        int idPareja,
        int idCombatiente,
        String nombre,
        int vida) {

    public VidaIntegrante {
        Objects.requireNonNull(nombre);

        if (vida < 0) {
            throw new IllegalArgumentException(
                "La vida no puede ser negativa."
            );
        }
    }
}
