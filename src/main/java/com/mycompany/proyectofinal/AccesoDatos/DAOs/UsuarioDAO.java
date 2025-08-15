package com.mycompany.proyectofinal.AccesoDatos.DAOs;

import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import com.mycompany.proyectofinal.util.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public void insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "{call MARCE.INSERTAR_USUARIO(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Hashear la contraseña antes de enviar
            String hashedPassword = usuario.getContrasena();
            
            cstmt.setString(1, usuario.getNombreUsuario());
            cstmt.setString(2, hashedPassword);
            cstmt.setString(3, usuario.getCorreo());
            cstmt.setString(4, usuario.getTelefono());
            cstmt.setInt(5, usuario.getIdRol());
            cstmt.registerOutParameter(6, Types.INTEGER);
            
            cstmt.execute();
            usuario.setIdUsuario(cstmt.getInt(6));
        }
    }
    
    public List<Usuario> obtenerTodosUsuarios() throws SQLException {
    List<Usuario> usuarios = new ArrayList<>();
    // Cambiar a sintaxis de procedimiento
    String sql = "{ call MARCE.OBTENER_USUARIOS_CON_ROLES(?) }";
    
    try (Connection conn = DatabaseConnection.connect();
         CallableStatement cstmt = conn.prepareCall(sql)) {
        
        cstmt.registerOutParameter(1, Types.REF_CURSOR);
        cstmt.execute();
        
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("ID_USUARIO"),
                    rs.getString("NOMBRE_USUARIO"),
                    rs.getString("CONTRASENA"),
                    rs.getString("CORREO"),
                    rs.getString("TELEFONO"),
                    rs.getString("NOMBRE_ROL")
                ));
            }
        }
    }
    return usuarios;
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
                        true
                    );
                }
            }
        }
        return null;
    }
   
    public Usuario obtenerUsuarioPorId(int id) throws SQLException {
        String sql = "{ ? = call MARCE.OBTENER_USUARIO_POR_ID(?) }";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, Types.REF_CURSOR);
            cstmt.setInt(2, id);
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
                        true
                    );
                }
            }
        }
        return null;
    }
    
    public Usuario obtenerUsuarioPorCorreo(String correo) throws SQLException {
        String sql = "{ ? = call MARCE.OBTENER_USUARIO_POR_CORREO(?) }";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, Types.REF_CURSOR);
            cstmt.setString(2, correo);
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
                        true
                    );
                }
            }
        }
        return null;
    }
}