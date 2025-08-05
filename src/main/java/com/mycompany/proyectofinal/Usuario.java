/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

/**
 *
 * @author geral
 */
public class Usuario {
    private String nombre;
    private String correo;
    private String contrasena;
    private boolean esAdmin;

    // Constructor
    public Usuario(String nombre, String correo, String contrasena, boolean esAdmin) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.esAdmin = esAdmin;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }
    
}
