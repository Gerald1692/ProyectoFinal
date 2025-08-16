/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

import com.mycompany.proyectofinal.ModelosPOJOs.*;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ObraDAO {
    private Connection conexion;

    public ObraDAO() {
        try {
            this.conexion = DatabaseConnection.connect();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    // Método para crear una nueva obra
    public boolean crearObra(Obra obra) {
        String sql = "{call MARCE.SP_OBRA_CREAR(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, obra.getTitulo());
            stmt.setString(3, obra.getDescripcion());
            stmt.setDate(4, Date.valueOf(obra.getFechaCreacion()));
            stmt.setDate(5, Date.valueOf(obra.getFechaIngreso()));
            stmt.setString(6, obra.getRutaImagen());
            stmt.setString(7, obra.getRutaAudio());
            stmt.setInt(8, obra.getIdTipoObra());
            stmt.setInt(9, obra.getIdSala());
            
            stmt.execute();
            obra.setIdObra(stmt.getInt(1));
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear obra: " + e.getMessage());
            return false;
        }
    }

    // Método para obtener todas las obras
    public List<Obra> obtenerTodasObras() {
        List<Obra> obras = new ArrayList<>();
        String sql = "{call MARCE.SP_OBRAS_LISTAR(?)}";
        
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Obra obra = new Obra(
                        rs.getInt("id_obra"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_creacion").toLocalDate(),
                        rs.getDate("fecha_ingreso").toLocalDate(),
                        rs.getString("ruta_imagen"),
                        rs.getString("ruta_audio"),
                        rs.getInt("id_tipo_obra"),
                        rs.getInt("id_sala")
                    );
                    obras.add(obra);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar obras: " + e.getMessage());
        }
        return obras;
    }

    // Método para buscar una obra por ID
    public Obra buscarObraPorId(int idObra) {
        String sql = "{call MARCE.SP_OBRA_BUSCAR_ID(?, ?)}";
        Obra obra = null;
        
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setInt(1, idObra);
            stmt.registerOutParameter(2, Types.REF_CURSOR);
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (rs.next()) {
                    obra = new Obra(
                        rs.getInt("id_obra"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_creacion").toLocalDate(),
                        rs.getDate("fecha_ingreso").toLocalDate(),
                        rs.getString("ruta_imagen"),
                        rs.getString("ruta_audio"),
                        rs.getInt("id_tipo_obra"),
                        rs.getInt("id_sala")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar obra: " + e.getMessage());
        }
        return obra;
    }

    // Método para actualizar una obra
    public boolean actualizarObra(Obra obra) {
        String sql = "{call MARCE.SP_OBRA_ACTUALIZAR(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setInt(1, obra.getIdObra());
            stmt.setString(2, obra.getTitulo());
            stmt.setString(3, obra.getDescripcion());
            stmt.setDate(4, Date.valueOf(obra.getFechaCreacion()));
            stmt.setDate(5, Date.valueOf(obra.getFechaIngreso()));
            stmt.setString(6, obra.getRutaImagen());
            stmt.setString(7, obra.getRutaAudio());
            stmt.setInt(8, obra.getIdTipoObra());
            stmt.setInt(9, obra.getIdSala());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar obra: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar una obra
    public boolean eliminarObra(int idObra) {
        String sql = "{call MARCE.SP_OBRA_ELIMINAR(?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setInt(1, idObra);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar obra: " + e.getMessage());
            return false;
        }
    }

    // Métodos adicionales para relaciones y combos
    
    public boolean agregarAutorAObra(ObraAutor obraAutor) {
        String sql = "{call MARCE.SP_OBRA_AUTOR_AGREGAR(?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setInt(1, obraAutor.getIdObra());
            stmt.setInt(2, obraAutor.getIdAutor());
            stmt.setInt(3, obraAutor.getIdTipoAutor());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar autor a obra: " + e.getMessage());
            return false;
        }
    }

    public List<Autor> obtenerAutoresDeObra(int idObra) {
        List<Autor> autores = new ArrayList<>();
        String sql = "{call MARCE.SP_OBRA_AUTORES_LISTAR(?, ?)}";
        
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setInt(1, idObra);
            stmt.registerOutParameter(2, Types.REF_CURSOR);
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    Autor autor = new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("nombre_autor"),
                        rs.getString("apellido_autor")
                    );
                    autores.add(autor);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener autores de obra: " + e.getMessage());
        }
        return autores;
    }

    public List<TipoObra> obtenerTodosTiposObra() {
        List<TipoObra> tipos = new ArrayList<>();
        String sql = "{call MARCE.SP_TIPOS_OBRA_LISTAR(?)}";
        
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    TipoObra tipo = new TipoObra(
                        rs.getInt("id_tipo_obra"),
                        rs.getString("nombre_tipo_obra"),
                        rs.getString("tecnica_tipo_obra")
                    );
                    tipos.add(tipo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de obra: " + e.getMessage());
        }
        return tipos;
    }

    public List<Sala> obtenerTodasSalas() {
        List<Sala> salas = new ArrayList<>();
        String sql = "{call MARCE.SP_SALAS_LISTAR(?)}";
        
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Sala sala = new Sala(
                        rs.getInt("id_sala"),
                        rs.getString("nombre_sala"),
                        rs.getString("tematica"),
                        rs.getInt("numero_puerta")
                    );
                    salas.add(sala);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar salas: " + e.getMessage());
        }
        return salas;
    }

    // Método para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}