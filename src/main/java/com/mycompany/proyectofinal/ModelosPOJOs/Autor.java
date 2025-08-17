package com.mycompany.proyectofinal.ModelosPOJOs;

public class Autor {
    private int id;  // Auto-incrementado
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String biografia;

    // Constructores
    public Autor() {}

    public Autor(String nombre, String apellido, String nacionalidad, String biografia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.biografia = biografia;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    
    
 @Override
    public String toString() {
        return nombre + ""+ apellido; // 👈 Esto hace que se muestre bonito en el ComboBox
    }

}