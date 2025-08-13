/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

/**
 *
 * @author admar
 */


import com.mycompany.proyectofinal.ModelosPOJOs.Autor;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    
    public int insertarAutor(Autor autor) throws SQLException {
        String sql = "{call MARCE.INSERTAR_AUTOR(?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, autor.getNombreAutor());
            cstmt.setString(2, autor.getApellidoAutor());
            cstmt.registerOutParameter(3, Types.INTEGER);
            
            cstmt.execute();
            return cstmt.getInt(3);
        }
    }
    
    public void actualizarAutor(Autor autor) throws SQLException {
        String sql = "{call MARCE.ACTUALIZAR_AUTOR(?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, autor.getIdAutor());
            cstmt.setString(2, autor.getNombreAutor());
            cstmt.setString(3, autor.getApellidoAutor());
            
            cstmt.execute();
        }
    }
    
    public void eliminarAutor(int idAutor) throws SQLException {
        String sql = "{call MARCE.ELIMINAR_AUTOR(?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idAutor);
            cstmt.execute();
        }
    }
    
    public Autor obtenerAutorPorId(int idAutor) throws SQLException {
        String sql = "SELECT * FROM MARCE.AUTOR WHERE id_autor = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAutor);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor(idAutor, sql, sql);
                    autor.setIdAutor(rs.getInt("id_autor"));
                    autor.setNombreAutor(rs.getString("nombre"));
                    autor.setApellidoAutor(rs.getString("apellido"));
                    return autor;
                }
            }
        }
        return null;
    }
    
    public List<Autor> listarAutores() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM MARCE.AUTOR";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Autor autor = new Autor(0, sql, sql);
                autor.setIdAutor(rs.getInt("id_autor"));
                autor.setNombreAutor(rs.getString("nombre"));
                autor.setApellidoAutor(rs.getString("apellido"));
                autores.add(autor);
            }
        }
        return autores;
    }
}
