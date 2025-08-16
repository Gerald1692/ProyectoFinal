/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.AccesoDatos.DAOs;

import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

public class ObraDAO {
    
    public ObraDAO() {
        // Constructor vacío
    }
    
    // Método para obtener todas las obras
    public List<Obra> obtenerTodasObras() throws SQLException {
    List<Obra> obras = new ArrayList<>();
    String sql = "{ call SP_MOSTRAR_OBRAS(?) }";
    
    try (Connection conexion = DatabaseConnection.connect();
         CallableStatement cstmt = conexion.prepareCall(sql)) {
        
        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.execute();
        
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
           while (rs.next()) {
                Obra obra = new Obra(
                    rs.getInt("id_obra"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),  
                    rs.getDate("fecha_creacion").toLocalDate(),
                    rs.getDate("fecha_ingreso").toLocalDate(),
                    rs.getString("ruta_imagen"),
                    rs.getString("ruta_audio"),
                    rs.getInt("id_tipo_obra"),  // Nombre corregido
                    rs.getString("nombre_tipo_obra"),
                    rs.getString("tecnica_tipo_obra"),  // Nombre corregido
                    rs.getInt("id_sala"),  // Nombre corregido
                    rs.getString("nombre_sala"),
                    rs.getString("autores")
                );
                obras.add(obra);
           }
        }
    }
    return obras;
}

    // Método para insertar una nueva obra
    public int insertarObra(Obra obra) throws SQLException {
        int idGenerado = 0;
        String sql = "{ call SP_INSERTAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
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
            idGenerado = cstmt.getInt(9);
        }
        return idGenerado;
    }

    // Método para actualizar una obra existente
    public boolean actualizarObra(Obra obra) throws SQLException {
        String sql = "{ call SP_ACTUALIZAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
            cstmt.setInt(1, obra.getIdObra());
            cstmt.setString(2, obra.getTitulo());
            cstmt.setString(3, obra.getDescripcion());
            cstmt.setDate(4, Date.valueOf(obra.getFechaCreacion()));
            cstmt.setDate(5, Date.valueOf(obra.getFechaIngreso()));
            cstmt.setString(6, obra.getRutaImagen());
            cstmt.setString(7, obra.getRutaAudio());
            cstmt.setInt(8, obra.getIdTipoObra());
            cstmt.setInt(9, obra.getIdSala());
            
            int filasAfectadas = cstmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // Método para eliminar una obra
    public boolean eliminarObra(int idObra) throws SQLException {
        String sql = "{ call SP_ELIMINAR_OBRA(?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
            cstmt.setInt(1, idObra);
            int filasAfectadas = cstmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // Clases internas para los combobox
    public static class TipoObra {
        private final int id;
        private final String nombre;
        private final String tecnica;
        
        public TipoObra(int id, String nombre, String tecnica) {
            this.id = id;
            this.nombre = nombre;
            this.tecnica = tecnica;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getTecnica() { return tecnica; }
        
        @Override
        public String toString() {
            return nombre + " - " + tecnica;
        }
    }
    
    public static class Sala {
        private final int id;
        private final String nombre;
        
        public Sala(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        
        @Override
        public String toString() {
            return nombre;
        }
    }
    
    public static class Autor {
        private final int id;
        private final String nombre;
        private final String apellido;
        
        public Autor(int id, String nombre, String apellido) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        
        @Override
        public String toString() {
            return nombre + " " + apellido;
        }
    }
    
    public static class TipoAutor {
        private final int id;
        private final String tipo;
        
        public TipoAutor(int id, String tipo) {
            this.id = id;
            this.tipo = tipo;
        }
        
        public int getId() { return id; }
        public String getTipo() { return tipo; }
        
        @Override
        public String toString() {
            return tipo;
        }
    }

    // Métodos para los combobox
    public List<TipoObra> obtenerTiposObra() throws SQLException {
        List<TipoObra> tipos = new ArrayList<>();
        String sql = "{ call SP_LISTAR_TIPOS_OBRA(?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();
            
            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    tipos.add(new TipoObra(
                        rs.getInt("id_tipo_obra"),
                        rs.getString("nombre_tipo_obra"),
                        rs.getString("tecnica")
                    ));
                }
            }
        }
        return tipos;
    }

    public List<Sala> obtenerSalas() throws SQLException {
        List<Sala> salas = new ArrayList<>();
        String sql = "{ call SP_LISTAR_SALAS(?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();
            
            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    salas.add(new Sala(
                        rs.getInt("id_sala"),
                        rs.getString("nombre_sala")
                    ));
                }
            }
        }
        return salas;
    }

    public List<Autor> obtenerAutores() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "{ call SP_LISTAR_AUTORES(?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();
            
            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    autores.add(new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                    ));
                }
            }
        }
        return autores;
    }

    public List<TipoAutor> obtenerTiposAutor() throws SQLException {
        List<TipoAutor> tipos = new ArrayList<>();
        String sql = "{ call SP_LISTAR_TIPOS_AUTOR(?) }";
        
        try (Connection conexion = DatabaseConnection.connect();
             CallableStatement cstmt = conexion.prepareCall(sql)) {
            
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.execute();
            
            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    tipos.add(new TipoAutor(
                        rs.getInt("id_tipo_autor"),
                        rs.getString("tipo")
                    ));
                }
            }
        }
        return tipos;
    }
}