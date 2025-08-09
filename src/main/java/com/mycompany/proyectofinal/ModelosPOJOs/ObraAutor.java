/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.ModelosPOJOs;

/**
 *
 * @author admar
 */
public class ObraAutor {
    private int idObra;
    private int idAutor;
    private int idTipoAutor;

    public ObraAutor(int idObra, int idAutor, int idTipoAutor) {
        this.idObra = idObra;
        this.idAutor = idAutor;
        this.idTipoAutor = idTipoAutor;
    }

    public int getIdObra() {
        return idObra;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public int getIdTipoAutor() {
        return idTipoAutor;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public void setIdTipoAutor(int idTipoAutor) {
        this.idTipoAutor = idTipoAutor;
    }
    
    
    
}
