package com.mycompany.proyectofinal.AccesoDatos.DAOs;

import com.mycompany.proyectofinal.ModelosPOJOs.*;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

public class ObraDAO {
 private Connection conn;

    public ObraDAO() {
        try {
            this.conn = DatabaseConnection.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtener todas las obras completas (para la tabla)
    public List<ObraCompleta> obtenerObrasCompletas() throws SQLException {
        List<ObraCompleta> obras = new ArrayList<>();
        String sql = "{ call OBTENER_OBRAS_COMPLETAS(?) }";
        
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    ObraCompleta obra = new ObraCompleta();
                    obra.setIdObra(rs.getInt("ID_OBRA"));
                    obra.setTitulo(rs.getString("TITULO"));
                    obra.setDescripcion(rs.getString("DESCRIPCION"));
                    obra.setFechaCreacion(rs.getDate("FECHA_CREACION"));
                    obra.setFechaIngreso(rs.getDate("FECHA_INGRESO"));
                    obra.setRutaImagen(rs.getString("RUTA_IMAGEN"));
                    obra.setRutaAudio(rs.getString("RUTA_AUDIO"));
                    obra.setNombreTipoObra(rs.getString("NOMBRE_TIPO_OBRA"));
                    obra.setTecnica(rs.getString("TECNICA"));
                    obra.setNombreSala(rs.getString("NOMBRE_SALA"));
                    obra.setNombreAutor(rs.getString("NOMBRE_AUTOR"));
                    obra.setTipoAutor(rs.getString("TIPO_AUTOR"));
                    
                    obras.add(obra);
                }
            }
        }
        return obras;
    }

    // Insertar una nueva obra
   public int insertarObra(Obra obra) throws SQLException {
    String sql = "{ call INSERTAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    int idGenerado = -1;
    
    try (CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setString(1, obra.getTitulo());
        stmt.setString(2, obra.getDescripcion());
        stmt.setDate(3, new java.sql.Date(obra.getFechaCreacion().getTime()));
        stmt.setDate(4, new java.sql.Date(obra.getFechaIngreso().getTime()));
        stmt.setString(5, obra.getRutaImagen());
        stmt.setString(6, obra.getRutaAudio());
        stmt.setInt(7, obra.getTipoObraId());
        stmt.setInt(8, obra.getSalaId());
        stmt.registerOutParameter(9, Types.INTEGER);
        
        stmt.execute();
        idGenerado = stmt.getInt(9);
    }
    return idGenerado;
}

public void actualizarObra(Obra obra) throws SQLException {
    String sql = "{ call ACTUALIZAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    
    try (CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, obra.getId());
        stmt.setString(2, obra.getTitulo());
        stmt.setString(3, obra.getDescripcion());
        stmt.setDate(4, new java.sql.Date(obra.getFechaCreacion().getTime()));
        stmt.setDate(5, new java.sql.Date(obra.getFechaIngreso().getTime()));
        stmt.setString(6, obra.getRutaImagen());
        stmt.setString(7, obra.getRutaAudio());
        stmt.setInt(8, obra.getTipoObraId());
        stmt.setInt(9, obra.getSalaId());
        
        stmt.execute();
    }
}

public void eliminarObra(int idObra) throws SQLException {
    String sql = "{ call ELIMINAR_OBRA(?) }";
    
    try (CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, idObra);
        stmt.execute();
    }
}

}