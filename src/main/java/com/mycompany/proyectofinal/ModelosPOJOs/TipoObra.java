/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.ModelosPOJOs;

/**
 *
 * @author admar
 */
public class TipoObra {
   private int idTipoObra;
   private String nombreTipoObra;

    public TipoObra(int idTipoObra, String nombreTipoObra) {
        this.idTipoObra = idTipoObra;
        this.nombreTipoObra = nombreTipoObra;
    }

    public int getIdTipoObra() {
        return idTipoObra;
    }

    public String getNombreTipoObra() {
        return nombreTipoObra;
    }

    public void setIdTipoObra(int idTipoObra) {
        this.idTipoObra = idTipoObra;
    }

    public void setNombreTipoObra(String nombreTipoObra) {
        this.nombreTipoObra = nombreTipoObra;
    }
   
   
}
