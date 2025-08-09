/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.ModelosPOJOs;

/**
 *
 * @author admar
 */
public class Usuario {
    private int idUsusario;
    private String nombreUsuario;
    private byte [] contrasena;
    private byte [] salto;
    private String correo;
    private String telefono;
    private int idRol;

    public Usuario(int idUsusario, String nombreUsuario, byte[] contrasenaUsuario, byte[] salto, String correo, String telefono, int idRol) {
        this.idUsusario = idUsusario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasenaUsuario;
        this.salto = salto;
        this.correo = correo;
        this.telefono = telefono;
        this.idRol = idRol;
    }

    public int getIdUsusario() {
        return idUsusario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public byte[] getContrasena() {
        return contrasena;
    }

    public byte[] getSalto() {
        return salto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdUsusario(int idUsusario) {
        this.idUsusario = idUsusario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(byte[] contrasena) {
        this.contrasena = contrasena;
    }

    public void setSalto(byte[] salto) {
        this.salto = salto;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    

    
}
