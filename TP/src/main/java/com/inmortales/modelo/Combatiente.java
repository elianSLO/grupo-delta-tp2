package com.inmortales.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.inmortales.modelo.equipamiento.Equipamiento;

public abstract class Combatiente {

    private static final int VIDA_BASE = 100;
    private static final int ENERGIA_BASE = 40;
    private static final int USOS_MAXIMOS_TECNICA = 5;
    private static final int EQUIPAMIENTO_MAXIMO = 3;

    private final int id;
    private final String nombre;

    private int vida;
    private int energia;
    private boolean defensaActiva;
    
    private int danioVenenoPorTurno;
    private int turnosVeneno;

    private final List<Tecnica> tecnicas;
    private final Map<Tecnica, Integer> usosTecnicas;
    private final List<Equipamiento> equipamientos;

    public Combatiente(int id, String nombre) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "El identificador debe ser positivo."
            );
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                "El nombre no puede estar vacío."
            );
        }

        this.id = id;
        this.nombre = nombre;

        this.vida = VIDA_BASE;
        this.energia = ENERGIA_BASE;

        this.tecnicas = new ArrayList<>();
        this.usosTecnicas = new LinkedHashMap<>();
        this.equipamientos = new ArrayList<>();
    }

    // Características propias de cada tipo de cultivador

    protected abstract double multiplicadorDanio();

    protected abstract double multiplicadorDanioRecibido();

    protected abstract double multiplicadorCostoTecnicas();

    // Identidad

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Vida y energía

    public int getVida() {
        return vida;
    }

    public int getEnergia() {
        return energia;
    }

    public int getVidaMaxima() {
        int total = VIDA_BASE;

        for (Equipamiento equipo : equipamientos) {
            total += equipo.modificarVidaMaxima();
        }

        return total;
    }

    public int getEnergiaMaxima() {
        int total = ENERGIA_BASE;

        for (Equipamiento equipo : equipamientos) {
            total += equipo.modificarEnergiaMaxima();
        }

        return total;
    }

    public boolean estaEnPie() {
        return vida > 0;
    }

    public void recibirDanio(int danio) {

        if (danio < 0) {
            throw new IllegalArgumentException(
                "El daño no puede ser negativo."
            );
        }

        int danioFinal = calcularDanioRecibido(danio);
        vida = Math.max(0, vida - danioFinal);
    }

    public void recuperarVida(int cantidad) {

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                "La recuperación no puede ser negativa."
            );
        }

        vida = Math.min(getVidaMaxima(), vida + cantidad);
    }

    public void recuperarEnergia(int cantidad) {

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                "La recuperación no puede ser negativa."
            );
        }

        energia = Math.min(getEnergiaMaxima(), energia + cantidad);
    }

    public boolean consumirEnergia(int cantidad) {

        if (cantidad < 0 || energia < cantidad) {
            return false;
        }

        energia -= cantidad;
        return true;
    }
    
    public void perderEnergia(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(
                "La cantidad no puede ser negativa."
            );
        }

        energia = Math.max(0, energia - cantidad);
    }

    public void reiniciarParaCombate() {
    	vida = getVidaMaxima();
        energia = getEnergiaMaxima();

        // Los efectos duran solamente un combate
        reiniciarEfectosTemporales();
    }
    
    public void activarDefensa() {
        defensaActiva = true;
    }

    public boolean tieneDefensaActiva() {
        return defensaActiva;
    }

    public void reiniciarEfectosTemporales() {
        defensaActiva = false;
        danioVenenoPorTurno = 0;
        turnosVeneno = 0;
    }

    // Cálculo del daño

    public int calcularDanio(int danioBase) {

        if (danioBase < 0) {
            throw new IllegalArgumentException(
                "El daño base no puede ser negativo."
            );
        }

        int danio = (int) Math.round(
            danioBase * multiplicadorDanio()
        );

        for (Equipamiento equipo : equipamientos) {
            danio += equipo.modificarDanio();
        }

        return Math.max(0, danio);
    }

    private int calcularDanioRecibido(int danio) {

        double danioFinal = danio * multiplicadorDanioRecibido();

        if (defensaActiva) {
            danioFinal *= 0.5;
        }

        int defensaEquipamiento = equipamientos.stream()
            .mapToInt(Equipamiento::modificarDefensa)
            .sum();

        danioFinal -= defensaEquipamiento;

        return Math.max(0, (int) Math.round(danioFinal));
    }

    public int calcularCostoEnergia(int costoBase) {

        if (costoBase < 0) {
            throw new IllegalArgumentException(
                "El costo no puede ser negativo."
            );
        }

        return (int) Math.ceil(
            costoBase * multiplicadorCostoTecnicas()
        );
    }

    // Técnicas

    public void aprenderTecnica(Tecnica tecnica) {

        Objects.requireNonNull(tecnica, "La técnica no puede ser nula.");

        if (!usosTecnicas.containsKey(tecnica)) {
            tecnicas.add(tecnica);
            usosTecnicas.put(tecnica, 0);
        }
    }

    public List<Tecnica> getTecnicas() {
        return List.copyOf(tecnicas);
    }

    public int getUsosRestantes(Tecnica tecnica) {

        Integer usos = usosTecnicas.get(tecnica);

        if (usos == null) {
            return 0;
        }

        return USOS_MAXIMOS_TECNICA - usos;
    }

    public boolean puedeUsarTecnica(Tecnica tecnica) {

        Objects.requireNonNull(tecnica, "La técnica no puede ser nula.");
    	
        if (!usosTecnicas.containsKey(tecnica)) {
            return false;
        }

        int costo = calcularCostoEnergia(
            tecnica.getCostoEnergia()
        );

        return getUsosRestantes(tecnica) > 0
            && energia >= costo;
    }

    public boolean registrarUsoTecnica(Tecnica tecnica) {

        if (!puedeUsarTecnica(tecnica)) {
            return false;
        }

        int costo = calcularCostoEnergia(
            tecnica.getCostoEnergia()
        );

        consumirEnergia(costo);

        usosTecnicas.put(
            tecnica,
            usosTecnicas.get(tecnica) + 1
        );

        return true;
    }

    // Equipamiento

    public boolean equipar(Equipamiento equipo) {

        if (equipo == null) {
            return false;
        }

        if (equipamientos.size() >= EQUIPAMIENTO_MAXIMO) {
            return false;
        }

        boolean categoriaRepetida = equipamientos.stream()
            .anyMatch(e -> e.getCategoria() == equipo.getCategoria());

        if (categoriaRepetida) {
            return false;
        }

        equipamientos.add(equipo);
        return true;
    }

    public boolean quitarEquipamiento(Equipamiento equipo) {

        if (!equipamientos.remove(equipo)) {
            return false;
        }

        vida = Math.min(vida, getVidaMaxima());
        energia = Math.min(energia, getEnergiaMaxima());

        return true;
    }

    public List<Equipamiento> getEquipamientos() {
        return List.copyOf(equipamientos);
    }
    
    //Poison
    
    public void aplicarVeneno(int danio, int turnos) {
        if (danio < 0 || turnos < 0) {
            throw new IllegalArgumentException();
        }

        danioVenenoPorTurno = danio;
        turnosVeneno = turnos;
    }

    // Aplica los efectos temporales al comienzo del turno del combatiente.
    // El veneno es daño interno: no lo reducen la armadura, la defensa
    // activa ni el multiplicador del Monje.
    // Devuelve el daño que recibió (0 si no estaba envenenado).
    public int aplicarEfectosInicioTurno() {
        if (turnosVeneno <= 0 || !estaEnPie()) {
            return 0;
        }

        int vidaAnterior = vida;
        perderVida(danioVenenoPorTurno);
        turnosVeneno--;

        return vidaAnterior - vida;
    }

    public int getTurnosVeneno() {
        return turnosVeneno;
    }

    // Resta vida directamente, sin pasar por las defensas.
    public void perderVida(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(
                "La cantidad no puede ser negativa."
            );
        }

        vida = Math.max(0, vida - cantidad);
    }

    // Igualdad por identificador

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Combatiente otro)) {
            return false;
        }

        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}