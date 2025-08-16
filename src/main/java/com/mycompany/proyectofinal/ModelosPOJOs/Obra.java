package com.mycompany.proyectofinal.ModelosPOJOs;

import java.time.LocalDate;

public class Obra {
    private int idObra;
    private String titulo;
    private String descripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaIngreso;
    private String rutaImagen;
    private String rutaAudio;
    private int idTipoObra;
    private String nombreTipoObra;
    private String tecnicaTipoObra;
    private int idSala;
    private String nombreSala;
    private String autores;

    // Constructores
    public Obra() {
    }

    // Constructor básico
    public Obra(int idObra, String titulo, String descripcion, 
               LocalDate fechaCreacion, LocalDate fechaIngreso, 
               String rutaImagen, String rutaAudio, 
               int idTipoObra, int idSala) {
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

    // Constructor completo
    public Obra(int idObra, String titulo, String descripcion, 
               LocalDate fechaCreacion, LocalDate fechaIngreso, 
               String rutaImagen, String rutaAudio,
               int idTipoObra, String nombreTipoObra, String tecnicaTipoObra,
               int idSala, String nombreSala, String autores) {
        this.idObra = idObra;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaIngreso = fechaIngreso;
        this.rutaImagen = rutaImagen;
        this.rutaAudio = rutaAudio;
        this.idTipoObra = idTipoObra;
        this.nombreTipoObra = nombreTipoObra;
        this.tecnicaTipoObra = tecnicaTipoObra;
        this.idSala = idSala;
        this.nombreSala = nombreSala;
        this.autores = autores;
    }

    // Getters y setters (todos los campos)
    public int getIdObra() {
        return idObra;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public void setRutaAudio(String rutaAudio) {
        this.rutaAudio = rutaAudio;
    }

    public int getIdTipoObra() {
        return idTipoObra;
    }

    public void setIdTipoObra(int idTipoObra) {
        this.idTipoObra = idTipoObra;
    }

    public String getNombreTipoObra() {
        return nombreTipoObra;
    }

    public void setNombreTipoObra(String nombreTipoObra) {
        this.nombreTipoObra = nombreTipoObra;
    }

    public String getTecnicaTipoObra() {
        return tecnicaTipoObra;
    }

    public void setTecnicaTipoObra(String tecnicaTipoObra) {
        this.tecnicaTipoObra = tecnicaTipoObra;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    @Override
    public String toString() {
        return titulo + " (" + idObra + ")";
    }
}