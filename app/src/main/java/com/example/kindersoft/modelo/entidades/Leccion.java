package com.example.kindersoft.modelo.entidades;

public class Leccion {
    private String id;
    private String nombre;
    private String descripcion;
    private Integer puntos;

    public Leccion() {}

    public Leccion(String id, String nombre, String descripcion, Integer puntos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.puntos = puntos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return
                nombre;
    }
}
