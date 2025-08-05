package com.mycompany.proyectofinal;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;
import java.io.File;

public class Obra {
    private int id;
    private String titulo;
    private String descripcion;
    private String sala;
    private String rutaImagen;
    private String artista;
    private String tecnica;
    private int anio;
    private String rutaAudio; 

    // Constructor básico
    public Obra() {}

    // Constructor desde BD
    public Obra(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id_obra");
        this.titulo = rs.getString("titulo");
        this.descripcion = rs.getString("descripcion");
        this.sala = rs.getString("sala_nombre");
        this.rutaImagen = rs.getString("ruta_imagen");
        this.artista = rs.getString("artista");
        this.tecnica = rs.getString("tecnica");
        this.anio = rs.getInt("anio");
        this.rutaAudio = rs.getString("ruta_audio");
    }

    // Constructor para datos de ejemplo
    public Obra(String titulo, String descripcion, String sala) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.sala = sala;
    }

    // Obtener imagen con manejo robusto de errores
    public Image getImagen() {
        // Intentar cargar imagen principal
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            try {
                File file = new File(rutaImagen);
                if (file.exists()) {
                    return new Image(file.toURI().toString());
                }
            } catch (Exception e) {
                System.err.println("Error cargando imagen: " + e.getMessage());
            }
        }
        
        // Cargar placeholder si falla
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Error cargando placeholder: " + e.getMessage());
            return null;
        }
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getSala() { return sala; }
    public String getRutaImagen() { return rutaImagen; }
    public String getArtista() { return artista; }
    public String getTecnica() { return tecnica; }
    public int getAnio() { return anio; }
    public String getRutaAudio() { return rutaAudio; }
    
    // Setters para posible uso futuro
    public void setRutaAudio(String ruta) { this.rutaAudio = ruta; }
    public void setRutaImagen(String ruta) { this.rutaImagen = ruta; }
}