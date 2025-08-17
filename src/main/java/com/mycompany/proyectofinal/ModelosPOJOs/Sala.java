/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.ModelosPOJOs;

/**
 *
 * @author admar
 */
public class Sala {
    private int idSala;
    private String nombreSala;
    private String tematica;
    private int numeroPuerta;

    public Sala() {}
    
    public Sala(int idSala, String nombreSala, String tematica, int numeroPuerta) {
        this.idSala = idSala;
        this.nombreSala = nombreSala;
        this.tematica = tematica;
        this.numeroPuerta = numeroPuerta;
    }

    public int getIdSala() {
        return idSala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public String getTematica() {
        return tematica;
    }

    public int getNumeroPuerta() {
        return numeroPuerta;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public void setNumeroPuerta(int numeroPuerta) {
        this.numeroPuerta = numeroPuerta;
    }
     @Override
    public String toString() {
        return nombreSala; // 👈 Esto hace que se muestre bonito en el ComboBox
    }
    
}
