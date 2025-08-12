package com.mycompany.proyectofinal.ModelosPOJOs;

import org.mindrot.jbcrypt.BCrypt;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasena; // Almacenará el hash BCrypt
    private String correo;
    private String telefono;
    private int idRol;

    // Constructor para registro de nuevos usuarios
    public Usuario(int idUsuario, String nombreUsuario, String contrasenaPlana, 
                   String correo, String telefono, int idRol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.setContrasenaSegura(contrasenaPlana); // Encripta la contraseña
        this.correo = correo;
        this.telefono = telefono;
        this.idRol = idRol;
    }

    // Constructor para recuperación desde BD (ya tiene el hash)
    public Usuario(int idUsuario, String nombreUsuario, String contrasenaHash, 
                   String correo, String telefono, int idRol, boolean isFromDB) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasenaHash;
        this.correo = correo;
        this.telefono = telefono;
        this.idRol = idRol;
    }

    // Hashea la contraseña al registrarse/actualizar
    public void setContrasenaSegura(String contrasenaPlana) {
        this.contrasena = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt(12));
    }

    // Verifica la contraseña sin exponer el hash
    public boolean verificarContrasena(String contrasenaPlana) {
        return BCrypt.checkpw(contrasenaPlana, this.contrasena);
    }

    // Getter modificado por seguridad
    public String getContrasena() {
        return "[PROTEGIDO]";
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    

}