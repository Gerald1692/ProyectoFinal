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
   private String TecnicaTipoObra;

   public TipoObra() {}
   
    public TipoObra(int idTipoObra, String nombreTipoObra, String TecnicaTipoObra) {
        this.idTipoObra = idTipoObra;
        this.nombreTipoObra = nombreTipoObra;
        this.TecnicaTipoObra = TecnicaTipoObra;
    }

    public int getIdTipoObra() {
        return idTipoObra;
    }

    public String getNombreTipoObra() {
        return nombreTipoObra;
    }

    public String getTecnicaTipoObra() {
        return TecnicaTipoObra;
    }

    public void setIdTipoObra(int idTipoObra) {
        this.idTipoObra = idTipoObra;
    }

    public void setNombreTipoObra(String nombreTipoObra) {
        this.nombreTipoObra = nombreTipoObra;
    }

    public void setTecnicaTipoObra(String TecnicaTipoObra) {
        this.TecnicaTipoObra = TecnicaTipoObra;
    }

    @Override
    public String toString() {
        return nombreTipoObra; // 
    }

    public String setTecnica(String string) {
        return TecnicaTipoObra;
    }
   
}
