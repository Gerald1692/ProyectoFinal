/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

/**
 *
 * @author admar
 */


import com.mycompany.proyectofinal.ModelosPOJOs.Sala;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
 private Connection conn;

    public SalaDAO() {
        try {
            this.conn = DatabaseConnection.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    public int insertarSala(Sala sala) throws SQLException {
        String sql = "{call MARCE.INSERTAR_SALA(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, sala.getNombreSala());
            cstmt.setString(2, sala.getTematica());
            cstmt.setInt(3, sala.getNumeroPuerta());
            cstmt.registerOutParameter(4, Types.INTEGER);
            
            cstmt.execute();
            return cstmt.getInt(4);
        }
    }
    public void actualizarSala(Sala sala) throws SQLException {
        String sql = "{call MARCE.ACTUALIZAR_SALA(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, sala.getIdSala());
            cstmt.setString(2, sala.getNombreSala());
            cstmt.setString(3, sala.getTematica());
            cstmt.setInt(4, sala.getNumeroPuerta());
            
            cstmt.execute();
        }
    }
    
    public void eliminarSala(int idSala) throws SQLException {
        String sql = "{call MARCE.ELIMINAR_SALA(?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, idSala);
            cstmt.execute();
        }
    }

    
    public List<Sala> listarSalas() throws SQLException {
    List<Sala> salas = new ArrayList<>();
    String sql = "SELECT * FROM MARCE.SALA";
    try (Connection conn = DatabaseConnection.connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            Sala sala = new Sala(); // Objeto vacío
            sala.setIdSala(rs.getInt("id_sala"));
            sala.setNombreSala(rs.getString("nombre_sala"));
            sala.setTematica(rs.getString("tematica"));
            sala.setNumeroPuerta(rs.getInt("numero_puerta"));
            salas.add(sala);
        }
    }
    return salas;
}
}
