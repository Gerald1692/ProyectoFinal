/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

/**
 *
 * @author admar
 */

import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ObraAutorDAO {
 private Connection conn;

    public ObraAutorDAO() {
        try {
            this.conn = DatabaseConnection.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void asociarAutorObra(int idObra, int idAutor, int idTipoAutor) throws SQLException {
        String sql = "{call MARCE.ASOCIAR_AUTOR_OBRA(?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idObra);
            cstmt.setInt(2, idAutor);
            cstmt.setInt(3, idTipoAutor);
            
            cstmt.execute();
        }
    }
    
    public void desasociarAutorObra(int idObra, int idAutor) throws SQLException {
        String sql = "{call MARCE.DESASOCIAR_AUTOR_OBRA(?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idObra);
            cstmt.setInt(2, idAutor);
            
            cstmt.execute();
        }
    }
    
    public void desasociarTodosAutoresObra(int idObra) throws SQLException {
        String sql = "{call MARCE.DESASOCIAR_TODOS_AUTORES_OBRA(?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idObra);
            cstmt.execute();
        }
    }
}
