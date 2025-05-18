package com.example.kindersoft.modelo.entidades;

public class Usuario {
    private String id;
    private String username;
    private String password;
    private String nombres;
    private Integer edad;
    private String rol;
    private Integer puntos_totales;

    public Usuario() {
    }

    public Usuario(String id, String username, String password, String nombres, Integer edad, String rol, Integer puntosTotales) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombres = nombres;
        this.edad = edad;
        this.rol = rol;
        this.puntos_totales = puntosTotales;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getPuntos_totales() {
        return puntos_totales;
    }

    public void setPuntos_totales(Integer puntos_totales) {
        this.puntos_totales = puntos_totales;
    }

    @Override
    public String toString() {
        return
                "UserName: " + username +
                         "\nRol: " +rol;
    }

    public String toStringCompleto(){
        return
                "id='" + id + '\'' +
                        ", username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", nombres='" + nombres + '\'' +
                        ", edad=" + edad +
                        ", rol='" + rol + '\'' +
                        ", puntosTotales=" + puntos_totales +
                        '}';
    }
}
