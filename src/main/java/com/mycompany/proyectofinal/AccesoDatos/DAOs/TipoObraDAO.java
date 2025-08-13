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
        String sql = "SELECT * FROM MARCE.TIPO_OBRA";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                TipoObra tipo = new TipoObra(0, sql);
                tipo.setIdTipoObra(rs.getInt("id_tipo_obra"));
                tipo.setNombreTipoObra(rs.getString("nombre"));
                tipos.add(tipo);
            }
        }
        return tipos;
    }
}
