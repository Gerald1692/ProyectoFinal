package com.mycompany.proyectofinal.ModelosPOJOs;

import java.sql.Date;

public class ObraCompleta {
    private int idObra;
    private String titulo;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaIngreso;
    private String rutaImagen;
    private String rutaAudio;
    private String nombreTipoObra;
    private String tecnica;
    private String nombreSala;
    private String nombreAutor;
    private String tipoAutor;

    // Constructor
    public ObraCompleta() {}

    // Getters y Setters
    public int getIdObra() { return idObra; }
    public void setIdObra(int idObra) { this.idObra = idObra; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    
    public String getRutaAudio() { return rutaAudio; }
    public void setRutaAudio(String rutaAudio) { this.rutaAudio = rutaAudio; }
    
    public String getNombreTipoObra() { return nombreTipoObra; }
    public void setNombreTipoObra(String nombreTipoObra) { this.nombreTipoObra = nombreTipoObra; }
    
    public String getTecnica() { return tecnica; }
    public void setTecnica(String tecnica) { this.tecnica = tecnica; }
    
    public String getNombreSala() { return nombreSala; }
    public void setNombreSala(String nombreSala) { this.nombreSala = nombreSala; }
    
    public String getNombreAutor() { return nombreAutor; }
    public void setNombreAutor(String nombreAutor) { this.nombreAutor = nombreAutor; }
    
    public String getTipoAutor() { return tipoAutor; }
    public void setTipoAutor(String tipoAutor) { this.tipoAutor = tipoAutor; }
}