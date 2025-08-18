package com.mycompany.proyectofinal.ModelosPOJOs;

import org.mindrot.jbcrypt.BCrypt;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
<<<<<<< Updated upstream
    private String contrasena; // Almacena el hash BCrypt
=======
    private String contrasena; // Almacenará el hash BCrypt
>>>>>>> Stashed changes
    private String correo;
    private String telefono;
    private int idRol;
    private String rolNombre; // Nuevo campo para el nombre del rol

<<<<<<< Updated upstream
    
    
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
=======
    // Constructor para registro de nuevos usuarios
    public Usuario(int idUsuario, String nombreUsuario, String contrasenaPlana, 
                   String correo, String telefono, int idRol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.setContrasenaSegura(contrasenaPlana); // Encripta la contraseña
>>>>>>> Stashed changes
        this.correo = correo;
        this.telefono = telefono;
        this.idRol = idRol;
    }

<<<<<<< Updated upstream
    // Getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
=======
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
>>>>>>> Stashed changes
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

<<<<<<< Updated upstream
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

=======
>>>>>>> Stashed changes
    public String getCorreo() {
        return correo;
    }

<<<<<<< Updated upstream
=======
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

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    
    public String getRolNombre() {
        return rolNombre;
    }
=======
>>>>>>> Stashed changes

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
    
<<<<<<< Updated upstream
    public boolean verificarContrasena(String contrasenaPlana) {
        return BCrypt.checkpw(contrasenaPlana, this.contrasena);
    }
    
    
    
=======

>>>>>>> Stashed changes
}