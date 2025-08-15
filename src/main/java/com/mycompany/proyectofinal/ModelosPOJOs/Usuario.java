package com.mycompany.proyectofinal.ModelosPOJOs;

import org.mindrot.jbcrypt.BCrypt;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasena; // Almacena el hash BCrypt
    private String correo;
    private String telefono;
    private int idRol;
    private String rolNombre; // Nuevo campo para el nombre del rol

    
    
    // Constructor para registro
    public Usuario(int idUsuario, String nombreUsuario, String contrasenaPlana, 
                  String correo, String telefono, String rolNombre) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
        this.correo = correo;
        this.telefono = telefono;
        this.rolNombre = rolNombre;
    }
    
     public Usuario( String nombreUsuario,  
                  String correo, String telefono, String rolNombre) {
        
        this.nombreUsuario = nombreUsuario;
       
        this.correo = correo;
        this.telefono = telefono;
        this.rolNombre = rolNombre;
    }
    
    public Usuario() {
    }
    // Constructor para datos de BD
    public Usuario(int idUsuario, String nombreUsuario, String contrasenaHash, 
                  String correo, String telefono, int idRol, boolean fromDB) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasenaHash;
        this.correo = correo;
        this.telefono = telefono;
        this.idRol = idRol;
    }

    // Getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasena = contrasenaHash;
    }
    
    public void setContrasenaPlana(String contrasenaPlana) {
        this.contrasena = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
    
    public boolean verificarContrasena(String contrasenaPlana) {
        return BCrypt.checkpw(contrasenaPlana, this.contrasena);
    }
    
    
    
}