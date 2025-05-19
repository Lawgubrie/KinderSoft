package com.example.kindersoft.modelo.entidades;

public class Reporte {
    private String id;
    private String idUsuario;
    private Integer puntuacion;

    public Reporte() {
    }

    public Reporte(String id, String idUsuario, Integer puntuacion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return
                "IdUsuario= " + idUsuario +
                        " \npuntuacion= " + puntuacion;
    }
}
