package com.inmortales.modelo;

public interface EfectoTecnica {

    void aplicar(Combatiente ejecutor, Combatiente objetivo);

    TipoEfecto getTipo();
}