package com.example.kindersoft.modelo.entidades;

public class Trofeo {
    private String id;
    private String descripcion;
    private String nombre;

    public Trofeo() {}

    public Trofeo(String id, String descripcion, String nombre) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Trofeo{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
