package com.mycompany.proyectofinal.AccesoDatos.DAOs;
import com.mycompany.proyectofinal.ModelosPOJOs.Sala;

import com.mycompany.proyectofinal.ModelosPOJOs.*;
import com.mycompany.proyectofinal.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

/*
  ObraDAO — acceso a BD para las obras.
  Apuntes rápidos:
  - Normaliza rutas (convierte "\" a "/") para evitar rollos entre Windows / JAR.
  - Acepta fechas null (no lanza NullPointer).
  - Usa CallableStatement y REF CURSOR para procedimientos PL/SQL.
  - Si tus procedures tienen distinto orden/params, ajusta aquí.
*/

public class ObraDAO {
    private Connection conn;
    

   
    
    public ObraDAO() {
        try {
            this.conn = DatabaseConnection.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -----------------------
    // Helpers (pequeñas utilidades)
    // -----------------------

    // Normaliza la ruta para que siempre use "/" y sin espacios al inicio/fin.
    private String normalizeRuta(String ruta) {
        if (ruta == null) return null;
        return ruta.trim().replace("\\", "/");
    }

    // Pone la fecha en el CallableStatement o NULL si es null (evita errores).
    private void safeSetDate(CallableStatement stmt, int index, java.sql.Date date) throws SQLException {
        if (date == null) {
            stmt.setNull(index, Types.DATE);
        } else {
            stmt.setDate(index, date);
        }
    }

    // -----------------------
    // Leer todas las obras (para poblar la tabla)
    // -----------------------
    public List<ObraCompleta> obtenerObrasCompletas() throws SQLException {
        List<ObraCompleta> obras = new ArrayList<>();
        String sql = "{ call OBTENER_OBRAS_COMPLETAS(?) }";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    ObraCompleta obra = new ObraCompleta();

                    // campos habituales
                    obra.setIdObra(rs.getInt("ID_OBRA"));
                    obra.setTitulo(rs.getString("TITULO"));
                    obra.setDescripcion(rs.getString("DESCRIPCION"));
                    obra.setFechaCreacion(rs.getDate("FECHA_CREACION"));
                    obra.setFechaIngreso(rs.getDate("FECHA_INGRESO"));

                    // rutas normalizadas (importante)
                    obra.setRutaImagen(normalizeRuta(rs.getString("RUTA_IMAGEN")));
                    obra.setRutaAudio(normalizeRuta(rs.getString("RUTA_AUDIO")));

                    // joins adicionales (tipo, sala, autor, etc.)
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

    // -----------------------
    // Insertar obra nueva
    // -----------------------
    public int insertarObra(Obra obra) throws SQLException {
        // NOTE: el procedimiento debe coincidir en parámetros y orden.
        String sql = "{ call INSERTAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        int idGenerado = -1;

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getDescripcion());

            // fechas (pueden ser null)
            java.sql.Date fechaCreacion = obra.getFechaCreacion();
            java.sql.Date fechaIngreso = obra.getFechaIngreso();
            safeSetDate(stmt, 3, fechaCreacion);
            safeSetDate(stmt, 4, fechaIngreso);

            // rutas — siempre normalizadas
            stmt.setString(5, normalizeRuta(obra.getRutaImagen()));
            stmt.setString(6, normalizeRuta(obra.getRutaAudio()));

            // ids de referencia
            stmt.setInt(7, obra.getTipoObraId());
            stmt.setInt(8, obra.getSalaId());

            // out param: id generado
            stmt.registerOutParameter(9, Types.INTEGER);

            stmt.execute();
            idGenerado = stmt.getInt(9);
        }
        return idGenerado;
    }

    // -----------------------
    // Actualizar obra existente
    // -----------------------
    public void actualizarObra(Obra obra) throws SQLException {
        // NOTE: revisar que ACTUALIZAR_OBRA reciba los mismos parámetros en BD
        String sql = "{ call ACTUALIZAR_OBRA(?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, obra.getId());
            stmt.setString(2, obra.getTitulo());
            stmt.setString(3, obra.getDescripcion());

            // fechas (manejamos null)
            java.sql.Date fechaCreacion = obra.getFechaCreacion();
            java.sql.Date fechaIngreso = obra.getFechaIngreso();
            safeSetDate(stmt, 4, fechaCreacion);
            safeSetDate(stmt, 5, fechaIngreso);

            // rutas normalizadas
            stmt.setString(6, normalizeRuta(obra.getRutaImagen()));
            stmt.setString(7, normalizeRuta(obra.getRutaAudio()));

            // ids
            stmt.setInt(8, obra.getTipoObraId());
            stmt.setInt(9, obra.getSalaId());

            stmt.execute();
        }
    }

    // -----------------------
    // Eliminar obra por id
    // -----------------------
    public void eliminarObra(int idObra) throws SQLException {
        String sql = "{ call ELIMINAR_OBRA(?) }";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idObra);
            stmt.execute();
        }
    }

    // -----------------------
    // Obtener rutas de imágenes (desde BD, si tienes SP)
    // -----------------------
    // Devuelve rutas ya normalizadas ("/")
    public List<String> obtenerImagenes() throws SQLException {
        List<String> imagenes = new ArrayList<>();
        String sql = "{ call OBTENER_IMAGENES(?) }";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    imagenes.add(normalizeRuta(rs.getString("RUTA_IMAGEN")));
                }
            }
        }
        return imagenes;
    }

    // -----------------------
    // Obtener rutas de audios (desde BD, si tienes SP)
    // -----------------------
    public List<String> obtenerAudios() throws SQLException {
        List<String> audios = new ArrayList<>();
        String sql = "{ call OBTENER_AUDIOS(?) }";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    audios.add(normalizeRuta(rs.getString("RUTA_AUDIO")));
                }
            }
        }
        return audios;
    }
    
   
public List<Obra> obtenerTodasObrasSimple() throws SQLException {
    List<Obra> obras = new ArrayList<>();
    String sql = "{ ? = call MARCE.OBTENER_TODAS_OBRAS_SIMPLE() }";

    try (Connection conn = DatabaseConnection.connect();
         CallableStatement cstmt = conn.prepareCall(sql)) {

        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.execute();

        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            while (rs.next()) {
                Obra obra = new Obra();
                obra.setId(rs.getInt("id_obra"));
                obra.setTitulo(rs.getString("titulo"));
                obra.setRutaImagen(rs.getString("ruta_imagen"));

                // 👉 construimos Sala y la asociamos a la Obra
                Sala sala = new Sala();
                sala.setIdSala(rs.getInt("id_sala"));
                sala.setNombreSala(rs.getString("nombre_sala"));
                obra.setSala(sala);


                obras.add(obra);
            }
        }
    }
    return obras;
}

 public List<Obra> obtenerObrasPorSala(int idSala) throws SQLException {
        List<Obra> obras = new ArrayList<>();
        String sql = "{ ? = call MARCE.OBTENER_OBRAS_POR_SALA(?) }";

        try (Connection conn = DatabaseConnection.connect();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.registerOutParameter(1, Types.REF_CURSOR);
            cstmt.setInt(2, idSala);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    Obra obra = new Obra();
                    obra.setId(rs.getInt("ID_OBRA"));
                    obra.setTitulo(rs.getString("TITULO"));
                    obra.setRutaImagen(rs.getString("RUTA_IMAGEN"));
                    obras.add(obra);
                }
            }
        }
        return obras;
    }

}
