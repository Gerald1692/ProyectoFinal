package com.mycompany.proyectofinal.ModelosPOJOs;

import java.sql.Date;

public class Obra {
    private int id;               // Auto-incrementado
    private String titulo;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaIngreso;
    private String rutaImagen;
    private String rutaAudio;
    private int autorId;
    private int tipoObraId;
    private int salaId;

    // Campos adicionales solo para consultas (no inserts/updates directos)
    private String nombreTipoObra;  // Ej: Escultura, Pintura
    private String tecnica;         // Ej: Óleo, Mármol
    private String nombreSala;      // Ej: Sala de Dinosaurios
    private Sala sala;
    // --- Constructores ---
    public Obra() {
    }

    public Obra(int id, String titulo, String rutaImagen) {
        this.id = id;
        this.titulo = titulo;
        this.rutaImagen = rutaImagen;
    }

    public Obra(Sala sala) {
        this.sala = sala;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
    
    public Obra(int id, String titulo, String descripcion, Date fechaCreacion, Date fechaIngreso,
                String rutaImagen, String rutaAudio, int autorId, int tipoObraId, int salaId,
                String nombreTipoObra, String tecnica, String nombreSala) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaIngreso = fechaIngreso;
        this.rutaImagen = rutaImagen;
        this.rutaAudio = rutaAudio;
        this.autorId = autorId;
        this.tipoObraId = tipoObraId;
        this.salaId = salaId;
        this.nombreTipoObra = nombreTipoObra;
        this.tecnica = tecnica;
        this.nombreSala = nombreSala;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
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

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public int getTipoObraId() {
        return tipoObraId;
    }

    public void setTipoObraId(int tipoObraId) {
        this.tipoObraId = tipoObraId;
    }

    public int getSalaId() {
        return salaId;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public String getNombreTipoObra() {
        return nombreTipoObra;
    }

    public void setNombreTipoObra(String nombreTipoObra) {
        this.nombreTipoObra = nombreTipoObra;
    }

    public String getTecnica() {
        return tecnica;
    }

    public void setTecnica(String tecnica) {
        this.tecnica = tecnica;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }
}
