/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

/**
 *
 * @author admar
 */


import com.mycompany.proyectofinal.ModelosPOJOs.TipoObra;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TipoObraDAO {
 private Connection conn;

    public TipoObraDAO() {
        try {
            this.conn = DatabaseConnection.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int insertarTipoObra(TipoObra tipoObra) throws SQLException {
        String sql = "{call MARCE.INSERTAR_TIPO_OBRA(?, ?)}";
        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, tipoObra.getNombreTipoObra());
            cstmt.registerOutParameter(2, Types.INTEGER);
            
            cstmt.execute();
            return cstmt.getInt(2);
        }
    }
    
     public List<TipoObra> listarTiposObra() throws SQLException {
        List<TipoObra> tipos = new ArrayList<>();
        String sql = "SELECT ID_TIPO_OBRA, NOMBRE_TIPO_OBRA, TECNICA FROM MARCE.TIPO_OBRA ORDER BY NOMBRE_TIPO_OBRA";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TipoObra tipo = new TipoObra();
                tipo.setIdTipoObra(rs.getInt("ID_TIPO_OBRA"));             // correcto
                tipo.setNombreTipoObra(rs.getString("NOMBRE_TIPO_OBRA")); // correcto
                tipo.setTecnica(rs.getString("TECNICA"));                 // correcto (puede ser null)
                tipos.add(tipo);
            }
        }
        return tipos;
    }
}
