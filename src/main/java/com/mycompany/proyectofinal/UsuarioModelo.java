package com.mycompany.proyectofinal;
import com.mycompany.proyectofinal.ConexionBD;
import com.mycompany.proyectofinal.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioModelo {
    
    public boolean existeUsuario(String correo) {
        correo = correo.toLowerCase().trim();
        if (!validarEmail(correo)) return false;
        
        String sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(correo) = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos: " + e.getMessage(), e);
        }
    }
    
    public boolean actualizarContrasena(String correo, String nuevaContrasena) {
        correo = correo.toLowerCase().trim();
        if (!validarEmail(correo) || nuevaContrasena == null || nuevaContrasena.length() < 8) {
            return false;
        }
        
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Eliminada encriptación
            stmt.setString(1, nuevaContrasena);
            stmt.setString(2, correo);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contraseña", e);
        }
    }
    
    public Usuario validarUsuario(String correo, String contrasena) {
        if (!validarEmail(correo)) return null;
        
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Comparación directa sin encriptación
                    String contrasenaBD = rs.getString("contrasena");
                    if (contrasena.equals(contrasenaBD)) {
                        return new Usuario(
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            contrasenaBD,
                            rs.getBoolean("es_admin")
                        );
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de autenticación", e);
        }
    }
    
    public boolean crearUsuario(Usuario usuario) {
        if (usuario == null || 
            usuario.getNombre() == null || usuario.getNombre().trim().isEmpty() ||
            !validarEmail(usuario.getCorreo()) || 
            usuario.getContrasena() == null || usuario.getContrasena().length() < 8) {
            return false;
        }
        
        if (existeUsuario(usuario.getCorreo())) {
            return false;
        }
        
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, es_admin) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            // Guardar contraseña sin encriptar
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setBoolean(4, usuario.isEsAdmin());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario", e);
        }
    }
    
    private boolean validarEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}