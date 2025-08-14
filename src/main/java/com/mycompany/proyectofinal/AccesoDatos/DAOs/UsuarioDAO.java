/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

/**
 *
 * @author admar
 */
import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class UsuarioDAO {
    
    public void insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "{call MARCE.INSERTAR_USUARIO(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, usuario.getNombreUsuario());
            cstmt.setString(2, usuario.getContrasena()); // Ya debe estar encriptada
            cstmt.setString(3, usuario.getCorreo());
            cstmt.setString(4, usuario.getTelefono());
            cstmt.setInt(5, usuario.getIdRol());
            cstmt.registerOutParameter(6, Types.INTEGER);
            
            cstmt.execute();
            usuario.setIdUsuario(cstmt.getInt(6));
        }
    }
    
    public void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "{call MARCE.ACTUALIZAR_USUARIO(?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, usuario.getIdUsuario());
            cstmt.setString(2, usuario.getNombreUsuario());
            cstmt.setString(3, usuario.getCorreo());
            cstmt.setString(4, usuario.getTelefono());
            cstmt.setInt(5, usuario.getIdRol());
            
            cstmt.execute();
        }
    }
    
    public void actualizarContrasena(String Correo, String nuevaContrasenaHash) throws SQLException {
        String sql = "{call MARCE.ACTUALIZAR_CONTRASENA_USUARIO(?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1,Correo );
            cstmt.setString(2, nuevaContrasenaHash);
            
            cstmt.execute();
        }
    }
    
    public void eliminarUsuario(int idUsuario) throws SQLException {
        String sql = "{call MARCE.ELIMINAR_USUARIO(?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idUsuario);
            cstmt.execute();
        }
    }
    
    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) throws SQLException {
        String sql = "{ ? = call MARCE.OBTENER_USUARIO_POR_NOMBRE(?) }";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, Types.REF_CURSOR);
            cstmt.setString(2, nombreUsuario);
            cstmt.execute();
            
            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contrasena"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("id_rol"),
                        true // Indicar que viene de BD
                    );
                }
            }
        }
        return null;
    }
}
