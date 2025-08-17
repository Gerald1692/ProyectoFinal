package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.*;
import com.mycompany.proyectofinal.ModelosPOJOs.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Controlador principal para administrar obras.
 * Apuntes rápidos (humanos):
 * - Carga imágenes desde resources o filesystem; no muestra alerta modal si falla (solo limpia).
 * - Combo de audios permite seleccionar rutas; botón btn_Audios reproduce/pausa el seleccionado.
 * - Si el audio está dentro del JAR lo extrae a un temp file (Media necesita URI de archivo).
 * - Normaliza rutas (convierte "\" a "/") para evitar problemas en Windows/JAR.
 * - Mantén javafx-media en el classpath (pom.xml).
 */
public class AdmiObrasController implements Initializable {

    // --- UI bindings (asegúrate de que los fx:id coincidan en tu FXML) ---
    @FXML private TableView<ObraCompleta> tblObras;
    @FXML private TableColumn<ObraCompleta, Integer> col_id;
    @FXML private TableColumn<ObraCompleta, String> col_titulo;
    @FXML private TableColumn<ObraCompleta, String> col_descripcion;
    @FXML private TableColumn<ObraCompleta, String> col_fechaCreacion;
    @FXML private TableColumn<ObraCompleta, String> col_fechaIngreso;
    @FXML private TableColumn<ObraCompleta, String> col_sala;
    @FXML private TableColumn<ObraCompleta, String> col_tipoObra;
    @FXML private TableColumn<ObraCompleta, String> col_tecnica;
    @FXML private TableColumn<ObraCompleta, String> col_autor;

    @FXML private TextArea txt_id;
    @FXML private TextArea txt_titulo;
    @FXML private TextArea txt_descripcion;
    @FXML private TextArea txt_fechaC;
    @FXML private TextArea txt_fechaI;

    @FXML private ComboBox<String> cmb_imagen;
    @FXML private ComboBox<String> cmb_audio;
    @FXML private ComboBox<Sala> cmb_sala;
    @FXML private ComboBox<TipoObra> cmb_tipoObra;
    @FXML private ComboBox<Autor> cmb_autor;

    @FXML private ImageView imgV_ImagenObra;

    @FXML private Button btn_crear;
    @FXML private Button btn_actualizar;
    @FXML private Button btn_eliminar;
    @FXML private Button btn_limpiar;
    @FXML private Button btn_buscar;

    // botón para reproducir/pausar audio (usa el fx:id que ya tienes)
    @FXML private Button btn_Audios;

    @FXML private AnchorPane col_idObra;

    // --- DAOs y listas ---
    private ObraDAO obraDAO;
    private SalaDAO salaDAO;
    private TipoObraDAO tipoObraDAO;
    private AutorDAO autorDAO;
    private ObraAutorDAO obraAutorDAO;
    private ObservableList<ObraCompleta> obrasList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // --- Media player (audio) ---
    private MediaPlayer mediaPlayer;
    private final List<File> tempAudioFiles = new ArrayList<>(); // archivos temporales extraídos del JAR

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obraDAO = new ObraDAO();
        salaDAO = new SalaDAO();
        tipoObraDAO = new TipoObraDAO();
        autorDAO = new AutorDAO();
        obraAutorDAO = new ObraAutorDAO();

        obrasList = FXCollections.observableArrayList();

        configurarTabla();
        cargarCombos();
        cargarObras();
        configurarEventos();
    }

    private void configurarTabla() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("idObra"));
        col_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        col_fechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        col_fechaIngreso.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        col_sala.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
        col_tipoObra.setCellValueFactory(new PropertyValueFactory<>("nombreTipoObra"));
        col_tecnica.setCellValueFactory(new PropertyValueFactory<>("tecnica"));
        col_autor.setCellValueFactory(new PropertyValueFactory<>("nombreAutor"));

        tblObras.setItems(obrasList);

        // al seleccionar una fila, actualizar campos y combos
        tblObras.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) llenarCampos(newSel);
        });
    }

    // -------------------------
    // UTIL: listar recursos dentro de resources/<recursoPath>
    // devuelve rutas como "RECURSOS/AdminImagenes/archivo.jpg"
    // funciona en IDE y en JAR
    // -------------------------
    private List<String> listarRecursos(String recursoPath) {
        List<String> archivos = new ArrayList<>();
        try {
            URL dirURL = getClass().getClassLoader().getResource(recursoPath);
            if (dirURL != null) {
                String protocol = dirURL.getProtocol();
                if ("file".equals(protocol)) {
                    Path folder = Paths.get(dirURL.toURI());
                    try (java.nio.file.DirectoryStream<Path> ds = Files.newDirectoryStream(folder)) {
                        for (Path p : ds) if (Files.isRegularFile(p)) archivos.add(recursoPath + "/" + p.getFileName().toString());
                    }
                    return archivos;
                } else if ("jar".equals(protocol)) {
                    String path = dirURL.getPath();
                    String jarPath = path.substring(path.indexOf("file:") + 5, path.indexOf("!"));
                    jarPath = URLDecoder.decode(jarPath, "UTF-8");
                    try (JarFile jar = new JarFile(jarPath)) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.startsWith(recursoPath + "/") && !entry.isDirectory()) archivos.add(name);
                        }
                    }
                    return archivos;
                }
            }
            // fallback dev
            File devDir = new File("src/main/resources/" + recursoPath);
            if (devDir.exists() && devDir.isDirectory()) {
                File[] files = devDir.listFiles();
                if (files != null) for (File f : files) if (f.isFile()) archivos.add(recursoPath + "/" + f.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return archivos;
    }

    // -------------------------
    // Cargar datos en combos
    // - salas, tipos y autores: objetos (muestran toString)
    // - imágenes: preferir carpeta resources, sino BD
    // - audios: desde BD (o resources si quieres)
    // -------------------------
    private void cargarCombos() {
        try {
            cmb_sala.setItems(FXCollections.observableArrayList(salaDAO.listarSalas()));
            cmb_tipoObra.setItems(FXCollections.observableArrayList(tipoObraDAO.listarTiposObra()));
            cmb_autor.setItems(FXCollections.observableArrayList(autorDAO.listarAutores()));

            List<String> imgs = listarRecursos("RECURSOS/AdminImagenes");
            if (!imgs.isEmpty()) cmb_imagen.setItems(FXCollections.observableArrayList(imgs));
            else cmb_imagen.setItems(FXCollections.observableArrayList(obraDAO.obtenerImagenes()));

            // audios: intento BD (puedes cambiar por listarRecursos similar a imágenes)
            cmb_audio.setItems(FXCollections.observableArrayList(obraDAO.obtenerAudios()));
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar combos", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // -------------------------
    // Cargar imagen robustamente desde varias fuentes
    // - classpath (resources)
    // - filesystem absoluto/relativo (user.dir)
    // - src/main/resources (modo dev)
    // No muestra alert modal en fallo; solo limpia ImageView.
    // -------------------------
    private void cargarImagenDesdeRuta(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            imgV_ImagenObra.setImage(null);
            return;
        }
        String rutaNorm = ruta.trim().replace("\\", "/");
        try {
            // try classloader resource
            URL res = getClass().getClassLoader().getResource(rutaNorm);
            if (res != null) {
                Image img = new Image(res.toExternalForm(), false);
                if (!img.isError()) { imgV_ImagenObra.setImage(img); return; }
            }

            // try filesystem
            File f = new File(rutaNorm);
            if (!f.exists()) f = new File(System.getProperty("user.dir"), rutaNorm);
            if (!f.exists()) f = new File("src/main/resources", rutaNorm);
            if (f.exists()) {
                Image img = new Image(f.toURI().toString(), false);
                if (!img.isError()) { imgV_ImagenObra.setImage(img); return; }
            }

            // try getResource with leading slash
            res = getClass().getResource(rutaNorm.startsWith("/") ? rutaNorm : ("/" + rutaNorm));
            if (res != null) {
                Image img = new Image(res.toExternalForm(), false);
                if (!img.isError()) { imgV_ImagenObra.setImage(img); return; }
            }

            // not found
            imgV_ImagenObra.setImage(null);
            System.err.println("No se pudo localizar ni cargar la imagen: " + rutaNorm);
        } catch (Exception ex) {
            ex.printStackTrace();
            imgV_ImagenObra.setImage(null);
        }
    }

    private void cargarObras() {
        try {
            obrasList.setAll(obraDAO.obtenerObrasCompletas());
            tblObras.refresh();
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar obras", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // -------------------------
    // Eventos UI
    // -------------------------
    private void configurarEventos() {
        btn_crear.setOnAction(e -> crearObra());
        btn_actualizar.setOnAction(e -> actualizarObra());
        btn_eliminar.setOnAction(e -> eliminarObra());
        btn_limpiar.setOnAction(e -> limpiarCampos());

        // cuando eliges una imagen en el combo, la mostramos de inmediato (silencioso)
        cmb_imagen.valueProperty().addListener((obs, oldVal, newVal) -> cargarImagenDesdeRuta(newVal));

        // botón de audios: reproduce/pausa el audio seleccionado
        btn_Audios.setOnAction(e -> {
            String sel = cmb_audio.getValue();
            if (sel == null || sel.trim().isEmpty()) {
                mostrarAlerta("Audio requerido", "Seleccione una ruta de audio en el combo", Alert.AlertType.WARNING);
                return;
            }
            try {
                // si ya hay reproductor y está reproduciendo -> pausa
                if (mediaPlayer != null) {
                    MediaPlayer.Status st = mediaPlayer.getStatus();
                    if (st == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                        btn_Audios.setText("▶"); // pequeño feedback
                        return;
                    } else if (st == MediaPlayer.Status.PAUSED || st == MediaPlayer.Status.STOPPED || st == MediaPlayer.Status.READY) {
                        mediaPlayer.play();
                        btn_Audios.setText("⏸");
                        return;
                    }
                }
                // si no hay mediaPlayer o no listo -> crear y reproducir
                String uri = obtenerUriAudioParaMedia(sel);
                if (uri == null) {
                    mostrarAlerta("Audio no encontrado", "No fue posible localizar el archivo de audio seleccionado.", Alert.AlertType.ERROR);
                    return;
                }
                prepararYReproducirMedia(uri);
                btn_Audios.setText("⏸");
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error audio", "No se pudo reproducir el audio: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    // -------------------------
    // AUDIO helpers
    // - obtiene URI usable por Media (file:///...)
    // - si el recurso está dentro del JAR, extrae a temp file
    // -------------------------
    private String obtenerUriAudioParaMedia(String ruta) {
        if (ruta == null) return null;
        String rutaNorm = ruta.trim().replace("\\", "/");
        try {
            // classpath
            URL res = getClass().getClassLoader().getResource(rutaNorm);
            if (res != null) {
                String protocol = res.getProtocol();
                if ("file".equals(protocol)) return res.toExternalForm();
                // probable jar -> extraer
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(rutaNorm)) {
                    if (is == null) return null;
                    String ext = "";
                    int dot = rutaNorm.lastIndexOf('.');
                    if (dot > 0) ext = rutaNorm.substring(dot);
                    Path tmp = Files.createTempFile("audio_", ext);
                    Files.copy(is, tmp, StandardCopyOption.REPLACE_EXISTING);
                    File tmpFile = tmp.toFile();
                    tmpFile.deleteOnExit();
                    tempAudioFiles.add(tmpFile);
                    return tmpFile.toURI().toString();
                }
            }

            // filesystem
            File f = new File(rutaNorm);
            if (!f.exists()) f = new File(System.getProperty("user.dir"), rutaNorm);
            if (!f.exists()) f = new File("src/main/resources", rutaNorm);
            if (f.exists()) return f.toURI().toString();

            // última chance: getResource con leading slash y file protocol
            res = getClass().getResource(rutaNorm.startsWith("/") ? rutaNorm : ("/" + rutaNorm));
            if (res != null && "file".equals(res.getProtocol())) return res.toExternalForm();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void prepararYReproducirMedia(String uri) {
        // limpiar anterior
        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); mediaPlayer.dispose(); } catch (Exception ignored) {}
            mediaPlayer = null;
        }
        Media media = new Media(uri);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnError(() -> {
            System.err.println("Media error: " + mediaPlayer.getError());
            mostrarAlerta("Error de reproducción", "No se pudo reproducir el audio: " + mediaPlayer.getError(), Alert.AlertType.ERROR);
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            btn_Audios.setText("▶");
            try { mediaPlayer.stop(); } catch (Exception ignored) {}
        });

        mediaPlayer.play();
    }

    // -------------------------
    // Llenar campos al seleccionar fila
    // - selecciona la imagen en el combo y la carga
    // - selecciona el audio en el combo (no lo reproduce automáticamente)
    // - selecciona sala/tipo/autor (objeto)
    // -------------------------
    private void llenarCampos(ObraCompleta obra) {
        txt_id.setText(String.valueOf(obra.getIdObra()));
        txt_titulo.setText(obra.getTitulo());
        txt_descripcion.setText(obra.getDescripcion());

        if (obra.getFechaCreacion() != null) txt_fechaC.setText(obra.getFechaCreacion().toString()); else txt_fechaC.clear();
        if (obra.getFechaIngreso() != null) txt_fechaI.setText(obra.getFechaIngreso().toString()); else txt_fechaI.clear();

        // imagen: seleccionar por ruta normalizada y forzar carga
        if (obra.getRutaImagen() != null) {
            String rutaObra = obra.getRutaImagen().trim().replace("\\", "/");
            cmb_imagen.getItems().stream()
                .filter(img -> img != null && img.trim().replace("\\", "/").equals(rutaObra))
                .findFirst().ifPresent(img -> cmb_imagen.getSelectionModel().select(img));
            cargarImagenDesdeRuta(rutaObra);
        } else {
            cmb_imagen.getSelectionModel().clearSelection();
            imgV_ImagenObra.setImage(null);
        }

        // audio: seleccionar en combo, no reproducir
        if (obra.getRutaAudio() != null) {
            String rutaAud = obra.getRutaAudio().trim().replace("\\", "/");
            cmb_audio.getItems().stream()
                .filter(a -> a != null && a.trim().replace("\\", "/").equals(rutaAud))
                .findFirst().ifPresent(a -> cmb_audio.getSelectionModel().select(a));
            // detener reproductor si estaba en uso
            if (mediaPlayer != null) { try { mediaPlayer.stop(); mediaPlayer.dispose(); } catch (Exception ignored) {} mediaPlayer = null; btn_Audios.setText("▶"); }
        } else {
            cmb_audio.getSelectionModel().clearSelection();
        }

        // sala
        if (obra.getNombreSala() != null) {
            String nombreSala = obra.getNombreSala();
            cmb_sala.getItems().stream()
                .filter(s -> s.getNombreSala() != null && s.getNombreSala().equals(nombreSala))
                .findFirst().ifPresent(s -> cmb_sala.getSelectionModel().select(s));
        } else cmb_sala.getSelectionModel().clearSelection();

        // tipo obra
        if (obra.getNombreTipoObra() != null) {
            String nombreTipo = obra.getNombreTipoObra();
            cmb_tipoObra.getItems().stream()
                .filter(t -> t.getNombreTipoObra() != null && t.getNombreTipoObra().equals(nombreTipo))
                .findFirst().ifPresent(t -> cmb_tipoObra.getSelectionModel().select(t));
        } else cmb_tipoObra.getSelectionModel().clearSelection();

        // autor (por "Nombre Apellido")
        if (obra.getNombreAutor() != null) {
            String nombreAutor = obra.getNombreAutor();
            cmb_autor.getItems().stream()
                .filter(a -> (a.getNombre() + " " + a.getApellido()).equals(nombreAutor))
                .findFirst().ifPresent(a -> cmb_autor.getSelectionModel().select(a));
        } else cmb_autor.getSelectionModel().clearSelection();
    }

    // -------------------------
    // Helpers para parseo de fechas
    // -------------------------
    private java.sql.Date parseDateOrNull(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;
        try {
            LocalDate ld = LocalDate.parse(texto.trim(), dateFormatter);
            return java.sql.Date.valueOf(ld);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    // -------------------------
    // CRUD: crear / actualizar / eliminar
    // -------------------------
    private void crearObra() {
        try {
            Obra nueva = new Obra();
            nueva.setTitulo(txt_titulo.getText());
            nueva.setDescripcion(txt_descripcion.getText());
            nueva.setFechaCreacion(parseDateOrNull(txt_fechaC.getText()));
            nueva.setFechaIngreso(parseDateOrNull(txt_fechaI.getText()));

            String rutaImg = cmb_imagen.getValue();
            if (rutaImg != null) rutaImg = rutaImg.trim().replace("\\", "/");
            nueva.setRutaImagen(rutaImg);

            String rutaAud = cmb_audio.getValue();
            if (rutaAud != null) rutaAud = rutaAud.trim().replace("\\", "/");
            nueva.setRutaAudio(rutaAud);

            Sala salaSeleccionada = cmb_sala.getSelectionModel().getSelectedItem();
            TipoObra tipoSeleccionado = cmb_tipoObra.getSelectionModel().getSelectedItem();
            if (salaSeleccionada == null || tipoSeleccionado == null) { mostrarAlerta("Campos requeridos", "Seleccione sala y tipo de obra", Alert.AlertType.WARNING); return; }

            nueva.setSalaId(salaSeleccionada.getIdSala());
            nueva.setTipoObraId(tipoSeleccionado.getIdTipoObra());

            int idGenerado = obraDAO.insertarObra(nueva);

            Autor autorSeleccionado = cmb_autor.getSelectionModel().getSelectedItem();
            if (autorSeleccionado != null) obraAutorDAO.asociarAutorObra(idGenerado, autorSeleccionado.getId(), 1);

            mostrarAlerta("Éxito", "Obra creada con ID: " + idGenerado, Alert.AlertType.INFORMATION);
            cargarObras();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void actualizarObra() {
        if (txt_id.getText().isEmpty()) { mostrarAlerta("Selección requerida", "Seleccione una obra", Alert.AlertType.WARNING); return; }
        try {
            Obra obra = new Obra();
            obra.setId(Integer.parseInt(txt_id.getText()));
            obra.setTitulo(txt_titulo.getText());
            obra.setDescripcion(txt_descripcion.getText());
            obra.setFechaCreacion(parseDateOrNull(txt_fechaC.getText()));
            obra.setFechaIngreso(parseDateOrNull(txt_fechaI.getText()));

            String rutaImg = cmb_imagen.getValue();
            if (rutaImg != null) rutaImg = rutaImg.trim().replace("\\", "/");
            obra.setRutaImagen(rutaImg);

            String rutaAud = cmb_audio.getValue();
            if (rutaAud != null) rutaAud = rutaAud.trim().replace("\\", "/");
            obra.setRutaAudio(rutaAud);

            Sala salaSeleccionada = cmb_sala.getSelectionModel().getSelectedItem();
            TipoObra tipoSeleccionado = cmb_tipoObra.getSelectionModel().getSelectedItem();
            if (salaSeleccionada == null || tipoSeleccionado == null) { mostrarAlerta("Campos requeridos", "Seleccione sala y tipo de obra", Alert.AlertType.WARNING); return; }

            obra.setSalaId(salaSeleccionada.getIdSala());
            obra.setTipoObraId(tipoSeleccionado.getIdTipoObra());

            obraDAO.actualizarObra(obra);

            obraAutorDAO.desasociarTodosAutoresObra(obra.getId());
            Autor autorSeleccionado = cmb_autor.getSelectionModel().getSelectedItem();
            if (autorSeleccionado != null) obraAutorDAO.asociarAutorObra(obra.getId(), autorSeleccionado.getId(), 1);

            mostrarAlerta("Éxito", "Obra actualizada correctamente", Alert.AlertType.INFORMATION);
            cargarObras();
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException nfe) {
            mostrarAlerta("ID inválido", "ID de obra no es un número válido", Alert.AlertType.ERROR);
        }
    }

    private void eliminarObra() {
        if (txt_id.getText().isEmpty()) { mostrarAlerta("Selección requerida", "Seleccione una obra", Alert.AlertType.WARNING); return; }
        try {
            obraDAO.eliminarObra(Integer.parseInt(txt_id.getText()));
            mostrarAlerta("Éxito", "Obra eliminada correctamente", Alert.AlertType.INFORMATION);
            cargarObras();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException nfe) {
            mostrarAlerta("ID inválido", "ID de obra no es un número válido", Alert.AlertType.ERROR);
        }
    }

    // -------------------------
    // limpiar campos UI
    // -------------------------
    private void limpiarCampos() {
        txt_id.clear();
        txt_titulo.clear();
        txt_descripcion.clear();
        txt_fechaC.clear();
        txt_fechaI.clear();
        cmb_imagen.getSelectionModel().clearSelection();
        cmb_audio.getSelectionModel().clearSelection();
        cmb_autor.getSelectionModel().clearSelection();
        cmb_tipoObra.getSelectionModel().clearSelection();
        cmb_sala.getSelectionModel().clearSelection();
        imgV_ImagenObra.setImage(null);
        tblObras.getSelectionModel().clearSelection();

        // detener audio si está sonando y limpiar mediaPlayer
        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); mediaPlayer.dispose(); } catch (Exception ignored) {}
            mediaPlayer = null;
            btn_Audios.setText("▶");
        }
    }

    // -------------------------
    // util UI: show alert
    // -------------------------
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
