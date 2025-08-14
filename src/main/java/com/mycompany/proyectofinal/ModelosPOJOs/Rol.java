package com.mycompany.proyectofinal.ModelosPOJOs;

public class Rol {
    private int idRol;
    private String nombreRol;

    public Rol(int idRol, String nombre) {
        this.idRol = idRol;
        this.nombreRol = nombre;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombreRol;
    }

    public void setNombre(String nombre) {
        this.nombreRol = nombre;
    }
}