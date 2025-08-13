/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

/**
 *
 * @author admar
 */


import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ObraDAO {
    
    public int insertarObra(Obra obra) throws SQLException {
        String sql = "{call MARCE.INSERTAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, obra.getTitulo());
            cstmt.setString(2, obra.getDescripcion());
            cstmt.setDate(3, Date.valueOf(obra.getFechaCreacion()));
            cstmt.setDate(4, Date.valueOf(obra.getFechaIngreso()));
            cstmt.setString(5, obra.getRutaImagen());
            cstmt.setString(6, obra.getRutaAudio());
            cstmt.setInt(7, obra.getIdTipoObra());
            cstmt.setInt(8, obra.getIdSala());
            cstmt.registerOutParameter(9, Types.INTEGER);
            
            cstmt.execute();
            return cstmt.getInt(9);
        }
    }
    
    public void actualizarObra(Obra obra) throws SQLException {
        String sql = "{call MARCE.ACTUALIZAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, obra.getIdObra());
            cstmt.setString(2, obra.getTitulo());
            cstmt.setString(3, obra.getDescripcion());
            cstmt.setDate(4, Date.valueOf(obra.getFechaCreacion()));
            cstmt.setDate(5, Date.valueOf(obra.getFechaIngreso()));
            cstmt.setString(6, obra.getRutaImagen());
            cstmt.setString(7, obra.getRutaAudio());
            cstmt.setInt(8, obra.getIdTipoObra());
            cstmt.setInt(9, obra.getIdSala());
            
            cstmt.execute();
        }
    }
    
    public void eliminarObra(int idObra) throws SQLException {
        String sql = "{call MARCE.ELIMINAR_OBRA(?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idObra);
            cstmt.execute();
        }
    }
    
    public Obra obtenerObraPorId(int idObra) throws SQLException {
        String sql = "SELECT * FROM MARCE.OBRA WHERE id_obra = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idObra);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Obra obra = new Obra(idObra, sql, sql, LocalDate.MIN, LocalDate.MIN, sql, sql, idObra, idObra);
                    obra.setIdObra(rs.getInt("id_obra"));
                    obra.setTitulo(rs.getString("titulo"));
                    obra.setDescripcion(rs.getString("descripcion"));
                    obra.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
                    obra.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                    obra.setRutaImagen(rs.getString("ruta_imagen"));
                    obra.setRutaAudio(rs.getString("ruta_audio"));
                    obra.setIdTipoObra(rs.getInt("id_tipo_obra"));
                    obra.setIdSala(rs.getInt("id_sala"));
                    return obra;
                }
            }
        }
        return null;
    }
    
    public List<Obra> listarObras() throws SQLException {
        List<Obra> obras = new ArrayList<>();
        String sql = "SELECT * FROM MARCE.OBRA";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Obra obra = new Obra(0, sql, sql, LocalDate.MIN, LocalDate.MIN, sql, sql, 0, 0);
                obra.setIdObra(rs.getInt("id_obra"));
                obra.setTitulo(rs.getString("titulo"));
                obra.setDescripcion(rs.getString("descripcion"));
                obra.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
                obra.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                obra.setRutaImagen(rs.getString("ruta_imagen"));
                obra.setRutaAudio(rs.getString("ruta_audio"));
                obra.setIdTipoObra(rs.getInt("id_tipo_obra"));
                obra.setIdSala(rs.getInt("id_sala"));
                obras.add(obra);
            }
        }
        return obras;
    }
}
