package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.ConexionBD;
import com.mycompany.proyectofinal.Obra;
import java.sql.*;
import javafx.collections.*;

public class ObraModelo {
    
    public ObservableList<Obra> obtenerTodasObras() {
        ObservableList<Obra> obras = FXCollections.observableArrayList();
        String sql = "SELECT o.id_obra, o.titulo, o.descripcion, "
                   + "s.nombre AS sala_nombre, o.ruta_imagen, "
                   + "o.artista, o.tecnica, o.anio, o.ruta_audio "
                   + "FROM Obra o "
                   + "JOIN Sala s ON o.id_sala = s.id_sala";
        
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                obras.add(new Obra(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener obras: " + e.getMessage());
            e.printStackTrace();
        }
        return obras;
    }
    
    public ObservableList<Obra> buscarObras(String criterio) {
        ObservableList<Obra> obras = FXCollections.observableArrayList();
        String sql = "SELECT o.id_obra, o.titulo, o.descripcion, "
                   + "s.nombre AS sala_nombre, o.ruta_imagen, "
                   + "o.artista, o.tecnica, o.anio, o.ruta_audio "
                   + "FROM Obra o "
                   + "JOIN Sala s ON o.id_sala = s.id_sala "
                   + "WHERE o.titulo LIKE ? OR o.descripcion LIKE ? "
                   + "OR o.artista LIKE ? OR s.nombre LIKE ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);
            stmt.setString(4, likeCriterio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    obras.add(new Obra(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar obras: " + e.getMessage());
            e.printStackTrace();
        }
        return obras;
    }
    
    public boolean crearObra(Obra obra, int idSala) {
        String sql = "INSERT INTO Obra (titulo, descripcion, id_sala, "
                   + "ruta_imagen, artista, tecnica, anio, ruta_audio) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getDescripcion());
            stmt.setInt(3, idSala);
            stmt.setString(4, obra.getRutaImagen());
            stmt.setString(5, obra.getArtista());
            stmt.setString(6, obra.getTecnica());
            stmt.setInt(7, obra.getAnio());
            stmt.setString(8, obra.getRutaAudio());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear obra: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarObra(Obra obra, int idSala) {
        String sql = "UPDATE Obra SET titulo = ?, descripcion = ?, id_sala = ?, "
                   + "ruta_imagen = ?, artista = ?, tecnica = ?, anio = ?, "
                   + "ruta_audio = ? WHERE id_obra = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getDescripcion());
            stmt.setInt(3, idSala);
            stmt.setString(4, obra.getRutaImagen());
            stmt.setString(5, obra.getArtista());
            stmt.setString(6, obra.getTecnica());
            stmt.setInt(7, obra.getAnio());
            stmt.setString(8, obra.getRutaAudio());
            stmt.setInt(9, obra.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar obra: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarObra(int idObra) {
        String sql = "DELETE FROM Obra WHERE id_obra = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idObra);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar obra: " + e.getMessage());
            return false;
        }
    }
    
    public ObservableList<String> obtenerNombresSalas() {
        ObservableList<String> salas = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM Sala";
        
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                salas.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener salas: " + e.getMessage());
        }
        return salas;
    }
    
    public int obtenerIdSala(String nombreSala) {
        String sql = "SELECT id_sala FROM Sala WHERE nombre = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreSala);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_sala");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID sala: " + e.getMessage());
        }
        return -1;
    }
}