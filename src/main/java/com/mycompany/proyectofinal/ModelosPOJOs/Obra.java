package com.mycompany.proyectofinal.ModelosPOJOs;

import java.sql.Date;

public class Obra {
    private int id;  // Auto-incrementado
    private String titulo;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaIngreso;
    private String rutaImagen;
    private String rutaAudio;
    private int autorId;
    private int tipoObraId;
    private int salaId;
    
    public Obra() {}

    public Obra(String titulo, String descripcion, Date fechaCreacion, 
                Date fechaIngreso, String rutaImagen, String rutaAudio, 
                int autorId, int tipoObraId, int salaId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaIngreso = fechaIngreso;
        this.rutaImagen = rutaImagen;
        this.rutaAudio = rutaAudio;
        this.autorId = autorId;
        this.tipoObraId = tipoObraId;
        this.salaId = salaId;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
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
    
    public int getAutorId() { return autorId; }
    public void setAutorId(int autorId) { this.autorId = autorId; }
    
    public int getTipoObraId() { return tipoObraId; }
    public void setTipoObraId(int tipoObraId) { this.tipoObraId = tipoObraId; }
    
    public int getSalaId() { return salaId; }
    public void setSalaId(int salaId) { this.salaId = salaId; }

    @Override
    public String toString() {
        // útil si usas Obra en ComboBox; por ahora no se usa, así que devolvemos título
        return titulo != null ? titulo : super.toString()+ rutaImagen + rutaAudio;
    }
}

    
    
    
  
      
      
    
    
      
   
