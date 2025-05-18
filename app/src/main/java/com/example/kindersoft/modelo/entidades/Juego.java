package com.example.kindersoft.modelo.entidades;

public class Juego {
    private String id;
    private String nombre;
    private String tipo;
    private Integer puntaje_maximos;
    private String id_leccion;

    public Juego() {
    }

    public Juego(String id, String nombre, String tipo, Integer puntaje_maximos, String id_leccion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.puntaje_maximos = puntaje_maximos;
        this.id_leccion = id_leccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_leccion() {
        return id_leccion;
    }

    public void setId_leccion(String id_leccion) {
        this.id_leccion = id_leccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPuntaje_maximos() {
        return puntaje_maximos;
    }

    public void setPuntaje_maximos(Integer puntaje_maximos) {
        this.puntaje_maximos = puntaje_maximos;
    }

    @Override
    public String toString() {
        return
                "Nombre= " + nombre +
                "\nTipo= " + tipo +
                "\nPuntaje_maximo= " + puntaje_maximos +
                "\nId_leccion= " + id_leccion;
    }
}
