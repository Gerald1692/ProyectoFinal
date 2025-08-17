package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.*;
import com.mycompany.proyectofinal.ModelosPOJOs.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
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
import javafx.util.Duration;

/**
 * AdmiObrasController completo — apuntes rápidos en comentarios.
 *
 * NOTA: esta versión carga LOS AUDIOS desde la carpeta de recursos "SONIDOS/"
 *       (primero intenta classpath/JAR, luego src/main/resources en dev).
 *       No usa la BD para audios.
 */
public class AdmiObrasController implements Initializable {

    // --- UI (asegúrate de que los fx:id en tu FXML coincidan) ---
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
    @FXML private ComboBox<String> cmb_audio;          // <-- audios mostrados como rutas tipo "SONIDOS/archivo.mp3"
    @FXML private ComboBox<Sala> cmb_sala;
    @FXML private ComboBox<TipoObra> cmb_tipoObra;
    @FXML private ComboBox<Autor> cmb_autor;

    @FXML private ImageView imgV_ImagenObra;

    @FXML private Button btn_crear;
    @FXML private Button btn_actualizar;
    @FXML private Button btn_eliminar;
    @FXML private Button btn_limpiar;
    @FXML private Button btn_buscar;
    @FXML private Button btn_Audios; // botón para reproducir audio

    @FXML private AnchorPane col_idObra;

    // --- DAOs y listas ---
    private ObraDAO obraDAO;
    private SalaDAO salaDAO;
    private TipoObraDAO tipoObraDAO;
    private AutorDAO autorDAO;
    private ObraAutorDAO obraAutorDAO;
    private ObservableList<ObraCompleta> obrasList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // MediaPlayer para reproducir audio (reutilizable)
    private MediaPlayer mediaPlayer;

    // Carpeta de recursos donde están los audios (ajústala si usas otro path)
    private static final String RECURSO_SONIDOS = "SONIDOS";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // inicializar DAOs
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

    // --------------------------
    // Configurar la tabla
    // --------------------------
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

        // cuando seleccionas fila, llenamos campos (y seleccionamos en combos)
        tblObras.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    llenarCampos(newSelection);
                }
            }
        );
    }

    // --------------------------
    // Listar recursos dentro de resources/<ruta>
    // - devuelve rutas tipo "SONIDOS/archivo.mp3"
    // - funciona en IDE y en JAR
    // --------------------------
    private List<String> listarRecursos(String recursoPath) {
        List<String> archivos = new ArrayList<>();
        try {
            // intento 1: classloader
            URL dirURL = getClass().getClassLoader().getResource(recursoPath);
            if (dirURL != null) {
                String protocol = dirURL.getProtocol();
                if ("file".equals(protocol)) {
                    Path folder = Paths.get(dirURL.toURI());
                    try (DirectoryStream<Path> ds = Files.newDirectoryStream(folder)) {
                        for (Path p : ds) {
                            if (Files.isRegularFile(p)) archivos.add(recursoPath + "/" + p.getFileName().toString());
                        }
                    }
                    return archivos;
                } else if ("jar".equals(protocol)) {
                    // dentro de JAR: recorrer entradas
                    String path = dirURL.getPath();
                    String jarPath = path.substring(path.indexOf("file:") + 5, path.indexOf("!"));
                    jarPath = URLDecoder.decode(jarPath, "UTF-8");
                    try (JarFile jar = new JarFile(jarPath)) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.startsWith(recursoPath + "/") && !entry.isDirectory()) {
                                archivos.add(name);
                            }
                        }
                    }
                    return archivos;
                }
            }

            // fallback dev: carpeta src/main/resources/<recursoPath>
            File devDir = new File("src/main/resources/" + recursoPath);
            if (devDir.exists() && devDir.isDirectory()) {
                File[] files = devDir.listFiles();
                if (files != null) {
                    for (File f : files) if (f.isFile()) archivos.add(recursoPath + "/" + f.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return archivos;
    }

    // --------------------------
    // Cargar combos (salas, tipos, autores, imágenes, audios)
    // Audios: los cargamos SIEMPRE desde RECURSO_SONIDOS (no BD).
    // --------------------------
    private void cargarCombos() {
        try {
            // objetos completos para salas/tipos/autores
            cmb_sala.setItems(FXCollections.observableArrayList(salaDAO.listarSalas()));
            cmb_tipoObra.setItems(FXCollections.observableArrayList(tipoObraDAO.listarTiposObra()));
            cmb_autor.setItems(FXCollections.observableArrayList(autorDAO.listarAutores()));

            // imágenes (igual que antes, intenta recursos o BD)
            List<String> imgs = listarRecursos("RECURSOS/AdminImagenes");
            if (!imgs.isEmpty()) {
                cmb_imagen.setItems(FXCollections.observableArrayList(imgs));
            } else {
                // fallback: desde BD (si tienes)
                cmb_imagen.setItems(FXCollections.observableArrayList(obraDAO.obtenerImagenes()));
            }

            // ===========================
            // AUDIOS: fuerza recursos SONIDOS/
            // ===========================
            List<String> audios = listarRecursos(RECURSO_SONIDOS);
            if (!audios.isEmpty()) {
                cmb_audio.setItems(FXCollections.observableArrayList(audios));
            } else {
                // Si por alguna razón no hay archivos en resources/SONIDOS,
                // puedes opcionalmente usar BD como backup; pero la petición
                // fue "asegúrate que se carguen de las rutas SONIDOS/ no la BD",
                // así que mostramos vacío (o podrías llenar con obraDAO.obtenerAudios()).
                cmb_audio.setItems(FXCollections.observableArrayList());
                System.out.println("No se encontraron audios en resources/" + RECURSO_SONIDOS);
            }

        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar combos", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // --------------------------
    // Obtener URI utilizable por Media a partir de una ruta tipo:
    // - dentro del JAR: "SONIDOS/file.mp3" -> extrae a temp y devuelve file:// URI
    // - filesystem (absoluto o relativo): devuelve file:// URI
    // - classpath directo: devuelve toExternalForm()
    // --------------------------
    private String obtenerUriAudioParaMedia(String ruta) throws Exception {
        if (ruta == null || ruta.trim().isEmpty()) return null;
        String rutaNorm = ruta.trim().replace("\\", "/");

        // 1) intentar classloader directo
        URL res = getClass().getClassLoader().getResource(rutaNorm);
        if (res != null) {
            // Si está dentro del JAR, URL puede ser "jar:file:/...!/..."; Media no acepta jar: URIs -> extraer si es jar
            String protocol = res.getProtocol();
            if ("file".equals(protocol)) {
                return res.toURI().toString();
            } else if ("jar".equals(protocol)) {
                // extraer entrada a temp file
                JarURLConnection jarCon = (JarURLConnection) res.openConnection();
                try (InputStream is = jarCon.getInputStream()) {
                    String tmpName = Paths.get(rutaNorm).getFileName().toString();
                    File tmp = File.createTempFile("audio_", "_" + tmpName);
                    tmp.deleteOnExit();
                    try (FileOutputStream fos = new FileOutputStream(tmp)) {
                        byte[] buf = new byte[8192];
                        int r;
                        while ((r = is.read(buf)) != -1) fos.write(buf, 0, r);
                    }
                    return tmp.toURI().toString();
                }
            } else {
                // fallback: usar external form si existe
                return res.toExternalForm();
            }
        }

        // 2) probar como archivo en filesystem (absoluto o relativo)
        File f = new File(rutaNorm);
        if (!f.exists()) f = new File(System.getProperty("user.dir"), rutaNorm);
        if (!f.exists()) f = new File("src/main/resources", rutaNorm); // modo dev
        if (f.exists()) {
            return f.toURI().toString();
        }

        // 3) intentar getResource con leading slash
        res = getClass().getResource(rutaNorm.startsWith("/") ? rutaNorm : ("/" + rutaNorm));
        if (res != null) {
            if ("file".equals(res.getProtocol())) return res.toURI().toString();
            // si está en jar, extraemos:
            if ("jar".equals(res.getProtocol())) {
                try (InputStream is = res.openStream()) {
                    String tmpName = Paths.get(rutaNorm).getFileName().toString();
                    File tmp = File.createTempFile("audio_", "_" + tmpName);
                    tmp.deleteOnExit();
                    try (FileOutputStream fos = new FileOutputStream(tmp)) {
                        byte[] buf = new byte[8192];
                        int r;
                        while ((r = is.read(buf)) != -1) fos.write(buf, 0, r);
                    }
                    return tmp.toURI().toString();
                }
            }
            return res.toExternalForm();
        }

        throw new IllegalArgumentException("No se encontró la ruta de audio: " + rutaNorm);
    }

    // --------------------------
    // Cargar imagen (igual lógica robusta usada antes)
    // --------------------------
    private void cargarImagenDesdeRuta(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            imgV_ImagenObra.setImage(null);
            return;
        }

        String rutaNorm = ruta.trim().replace("\\", "/");

        try {
            URL res = getClass().getClassLoader().getResource(rutaNorm);
            if (res != null) {
                Image img = new Image(res.toExternalForm(), false);
                if (!img.isError()) {
                    imgV_ImagenObra.setImage(img);
                    return;
                }
            }

            File f = new File(rutaNorm);
            if (!f.exists()) f = new File(System.getProperty("user.dir"), rutaNorm);
            if (!f.exists()) f = new File("src/main/resources", rutaNorm);
            if (f.exists()) {
                Image img = new Image(f.toURI().toString(), false);
                if (!img.isError()) {
                    imgV_ImagenObra.setImage(img);
                    return;
                }
            }

            URL res2 = getClass().getResource(rutaNorm.startsWith("/") ? rutaNorm : ("/" + rutaNorm));
            if (res2 != null) {
                Image img = new Image(res2.toExternalForm(), false);
                if (!img.isError()) {
                    imgV_ImagenObra.setImage(img);
                    return;
                }
            }

            imgV_ImagenObra.setImage(null);
            System.err.println("No se pudo localizar ni cargar la imagen: " + rutaNorm);
        } catch (Exception ex) {
            ex.printStackTrace();
            imgV_ImagenObra.setImage(null);
        }
    }

    // --------------------------
    // Cargar obras en la tabla
    // --------------------------
    private void cargarObras() {
        try {
            obrasList.setAll(obraDAO.obtenerObrasCompletas());
            tblObras.refresh();
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar obras", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // --------------------------
    // Eventos UI
    // --------------------------
    private void configurarEventos() {
        btn_crear.setOnAction(e -> crearObra());
        btn_actualizar.setOnAction(e -> actualizarObra());
        btn_eliminar.setOnAction(e -> eliminarObra());
        btn_limpiar.setOnAction(e -> limpiarCampos());
        btn_buscar.setOnAction(e -> { /* implementar si tienes búsqueda */ });

        // cuando el usuario selecciona una ruta en el combo, no auto-reproducimos
        // solo cargamos el thumbnail/imagen (si quieres) o dejamos para botón.
        cmb_imagen.valueProperty().addListener((obs, oldVal, newVal) -> {
            cargarImagenDesdeRuta(newVal);
        });

        // btn_Audios reproduce/para audio seleccionado
        btn_Audios.setOnAction(e -> {
            String rutaSeleccionada = cmb_audio.getValue();
            if (rutaSeleccionada == null || rutaSeleccionada.trim().isEmpty()) {
                mostrarAlerta("Audio", "Seleccione un audio en el combo", Alert.AlertType.INFORMATION);
                return;
            }
            reproducirAudioSeleccionado(rutaSeleccionada);
        });
    }

    // --------------------------
    // Reproducir audio seleccionado (manejo de MediaPlayer)
    // --------------------------
    private void reproducirAudioSeleccionado(String ruta) {
        try {
            // parar si ya había algo reproduciéndose
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
            }

            String uri = obtenerUriAudioParaMedia(ruta);
            Media media = new Media(uri);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setStartTime(Duration.ZERO);
            mediaPlayer.setOnError(() -> {
                Throwable err = mediaPlayer.getError();
                err.printStackTrace();
                mostrarAlerta("Error de reproducción", "No se pudo reproducir audio: " + (err != null ? err.getMessage() : "unknown"), Alert.AlertType.ERROR);
            });
            mediaPlayer.play();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            mostrarAlerta("Audio no encontrado", iae.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "Fallo al reproducir audio: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // --------------------------
    // Llenar campos cuando seleccionas una fila
    // - selecciona la ruta de imagen/audio en los combos
    // - carga la imagen silenciosamente
    // --------------------------
    private void llenarCampos(ObraCompleta obra) {
        txt_id.setText(String.valueOf(obra.getIdObra()));
        txt_titulo.setText(obra.getTitulo());
        txt_descripcion.setText(obra.getDescripcion());

        if (obra.getFechaCreacion() != null) {
            txt_fechaC.setText(obra.getFechaCreacion().toString());
        } else {
            txt_fechaC.clear();
        }
        if (obra.getFechaIngreso() != null) {
            txt_fechaI.setText(obra.getFechaIngreso().toString());
        } else {
            txt_fechaI.clear();
        }

        // Seleccionar imagen en combo comparando rutas normalizadas
        if (obra.getRutaImagen() != null) {
            String rutaObra = obra.getRutaImagen().trim().replace("\\", "/");
            cmb_imagen.getItems().stream()
                .filter(img -> img != null && img.trim().replace("\\", "/").equals(rutaObra))
                .findFirst()
                .ifPresent(img -> cmb_imagen.getSelectionModel().select(img));
            cargarImagenDesdeRuta(rutaObra);
        } else {
            cmb_imagen.getSelectionModel().clearSelection();
            imgV_ImagenObra.setImage(null);
        }

        // Seleccionar audio: buscamos la ruta en el combo de audios
        if (obra.getRutaAudio() != null) {
            String rutaAud = obra.getRutaAudio().trim().replace("\\", "/");
            cmb_audio.getItems().stream()
                .filter(a -> a != null && a.trim().replace("\\", "/").equals(rutaAud))
                .findFirst()
                .ifPresent(a -> cmb_audio.getSelectionModel().select(a));
            // NO reproducimos automáticamente; el usuario usa btn_Audios
        } else {
            cmb_audio.getSelectionModel().clearSelection();
        }

        // Sala
        if (obra.getNombreSala() != null) {
            String nombreSala = obra.getNombreSala();
            cmb_sala.getItems().stream()
                .filter(s -> s.getNombreSala() != null && s.getNombreSala().equals(nombreSala))
                .findFirst()
                .ifPresent(s -> cmb_sala.getSelectionModel().select(s));
        } else {
            cmb_sala.getSelectionModel().clearSelection();
        }

        // TipoObra
        if (obra.getNombreTipoObra() != null) {
            String nombreTipo = obra.getNombreTipoObra();
            cmb_tipoObra.getItems().stream()
                .filter(t -> t.getNombreTipoObra() != null && t.getNombreTipoObra().equals(nombreTipo))
                .findFirst()
                .ifPresent(t -> cmb_tipoObra.getSelectionModel().select(t));
        } else {
            cmb_tipoObra.getSelectionModel().clearSelection();
        }

        // Autor
        if (obra.getNombreAutor() != null) {
            String nombreAutor = obra.getNombreAutor();
            cmb_autor.getItems().stream()
                .filter(a -> (a.getNombre() + " " + a.getApellido()).equals(nombreAutor))
                .findFirst()
                .ifPresent(a -> cmb_autor.getSelectionModel().select(a));
        } else {
            cmb_autor.getSelectionModel().clearSelection();
        }
    }

    // --------------------------
    // parseo seguro de fecha (devuelve null si no es válido)
    // --------------------------
    private java.sql.Date parseDateOrNull(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;
        try {
            LocalDate ld = LocalDate.parse(texto.trim(), dateFormatter);
            return java.sql.Date.valueOf(ld);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    // --------------------------
    // Crear obra
    // --------------------------
    private void crearObra() {
        try {
            Obra nueva = new Obra();
            nueva.setTitulo(txt_titulo.getText());
            nueva.setDescripcion(txt_descripcion.getText());

            java.sql.Date fechaC = parseDateOrNull(txt_fechaC.getText());
            java.sql.Date fechaI = parseDateOrNull(txt_fechaI.getText());
            nueva.setFechaCreacion(fechaC);
            nueva.setFechaIngreso(fechaI);

            String rutaImg = cmb_imagen.getValue();
            if (rutaImg != null) rutaImg = rutaImg.trim().replace("\\", "/");
            nueva.setRutaImagen(rutaImg);

            String rutaAud = cmb_audio.getValue();
            if (rutaAud != null) rutaAud = rutaAud.trim().replace("\\", "/");
            nueva.setRutaAudio(rutaAud);

            Sala salaSeleccionada = cmb_sala.getSelectionModel().getSelectedItem();
            TipoObra tipoSeleccionado = cmb_tipoObra.getSelectionModel().getSelectedItem();
            if (salaSeleccionada == null || tipoSeleccionado == null) {
                mostrarAlerta("Campos requeridos", "Seleccione sala y tipo de obra", Alert.AlertType.WARNING);
                return;
            }

            nueva.setSalaId(salaSeleccionada.getIdSala());
            nueva.setTipoObraId(tipoSeleccionado.getIdTipoObra());

            int idGenerado = obraDAO.insertarObra(nueva);

            Autor autorSeleccionado = cmb_autor.getSelectionModel().getSelectedItem();
            if (autorSeleccionado != null) {
                obraAutorDAO.asociarAutorObra(idGenerado, autorSeleccionado.getId(), 1);
            }

            mostrarAlerta("Éxito", "Obra creada con ID: " + idGenerado, Alert.AlertType.INFORMATION);
            cargarObras();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // --------------------------
    // Actualizar obra
    // --------------------------
    private void actualizarObra() {
        if (txt_id.getText().isEmpty()) {
            mostrarAlerta("Selección requerida", "Seleccione una obra", Alert.AlertType.WARNING);
            return;
        }
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
            if (salaSeleccionada == null || tipoSeleccionado == null) {
                mostrarAlerta("Campos requeridos", "Seleccione sala y tipo de obra", Alert.AlertType.WARNING);
                return;
            }

            obra.setSalaId(salaSeleccionada.getIdSala());
            obra.setTipoObraId(tipoSeleccionado.getIdTipoObra());

            obraDAO.actualizarObra(obra);

            // actualizar asociación autor
            obraAutorDAO.desasociarTodosAutoresObra(obra.getId());
            Autor autorSeleccionado = cmb_autor.getSelectionModel().getSelectedItem();
            if (autorSeleccionado != null) {
                obraAutorDAO.asociarAutorObra(obra.getId(), autorSeleccionado.getId(), 1);
            }

            mostrarAlerta("Éxito", "Obra actualizada correctamente", Alert.AlertType.INFORMATION);
            cargarObras();
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException nfe) {
            mostrarAlerta("ID inválido", "ID de obra no es un número válido", Alert.AlertType.ERROR);
        }
    }

    // --------------------------
    // Eliminar obra
    // --------------------------
    private void eliminarObra() {
        if (txt_id.getText().isEmpty()) {
            mostrarAlerta("Selección requerida", "Seleccione una obra", Alert.AlertType.WARNING);
            return;
        }
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

    // --------------------------
    // Limpiar campos
    // --------------------------
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

        // parar audio si estaba sonando
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    // --------------------------
    // 
    // --------------------------
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
