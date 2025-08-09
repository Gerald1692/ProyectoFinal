/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.ModelosPOJOs;
import java.time.LocalDate;
/**
 *
 * @author admar
 */
public class Obra {
    private int idObra;
    private String titulo;
    private String descripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaIngreso;
    private String rutaImagen;
    private String rutaAudio;
    private int idTipoObra;
    private int idSala;

    public Obra(int idObra, String titulo, String descripcion, LocalDate fechaCreacion, LocalDate fechaIngreso, String rutaImagen, String rutaAudio, int idTipoObra, int idSala) {
        this.idObra = idObra;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaIngreso = fechaIngreso;
        this.rutaImagen = rutaImagen;
        this.rutaAudio = rutaAudio;
        this.idTipoObra = idTipoObra;
        this.idSala = idSala;
    }

    public int getIdObra() {
        return idObra;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public int getIdTipoObra() {
        return idTipoObra;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public void setRutaAudio(String rutaAudio) {
        this.rutaAudio = rutaAudio;
    }

    public void setIdTipoObra(int idTipoObra) {
        this.idTipoObra = idTipoObra;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }
    
    
    
}
