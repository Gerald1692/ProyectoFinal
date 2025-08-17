package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.*;
import com.mycompany.proyectofinal.ModelosPOJOs.*;
import java.io.File;
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

public class AdmiObrasController implements Initializable {

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

    @FXML private AnchorPane col_idObra;

    private ObraDAO obraDAO;
    private SalaDAO salaDAO;
    private TipoObraDAO tipoObraDAO;
    private AutorDAO autorDAO;
    private ObraAutorDAO obraAutorDAO;
    private ObservableList<ObraCompleta> obrasList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        // cuando seleccionas fila, llenamos campos (y seleccionamos en combos)
        tblObras.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    llenarCampos(newSelection);
                }
            }
        );
    }

    /**
     * Lista archivos dentro de resources/<recursoPath>.
     * Devuelve rutas relativas tipo "RECURSOS/AdminImagenes/archivo.jpg".
     * Funciona en IDE y en JAR.
     */
    private List<String> listarRecursos(String recursoPath) {
        List<String> archivos = new ArrayList<>();
        try {
            // Primero intentar por classloader
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
                    String jarPath = path.substring(path.indexOf("file:" ) + 5, path.indexOf("!"));
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

            // Fallback dev: carpeta src/main/resources/recursoPath
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

    private void cargarCombos() {
        try {
            // objetos completos para salas/tipos/autores (muestran nombre gracias a toString de POJOs)
            cmb_sala.setItems(FXCollections.observableArrayList(salaDAO.listarSalas()));
            cmb_tipoObra.setItems(FXCollections.observableArrayList(tipoObraDAO.listarTiposObra()));
            cmb_autor.setItems(FXCollections.observableArrayList(autorDAO.listarAutores()));

            // imágenes: preferimos cargar desde resources (carpeta proyectos)
            List<String> imgs = listarRecursos("RECURSOS/AdminImagenes");
            if (!imgs.isEmpty()) {
                cmb_imagen.setItems(FXCollections.observableArrayList(imgs));
            } else {
                // fallback a BD (si tienes SP OBTENER_IMAGENES)
                cmb_imagen.setItems(FXCollections.observableArrayList(obraDAO.obtenerImagenes()));
            }

            // audios: desde BD por ahora (puedes cambiar a resources si quieres)
            cmb_audio.setItems(FXCollections.observableArrayList(obraDAO.obtenerAudios()));
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar combos", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Carga imagen desde múltiples orígenes:
     * - resources (classpath)
     * - filesystem absoluto o relativo a user.dir
     * - src/main/resources (modo dev)
     *
     * No lanza alert modal; setea ImageView o lo limpia.
     */
    private void cargarImagenDesdeRuta(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            imgV_ImagenObra.setImage(null);
            return;
        }

        String rutaNorm = ruta.trim().replace("\\", "/");

        try {
            // 1) classpath usando classloader (ruta relativa dentro de resources)
            URL res = getClass().getClassLoader().getResource(rutaNorm);
            if (res != null) {
                Image img = new Image(res.toExternalForm(), false);
                if (!img.isError()) {
                    imgV_ImagenObra.setImage(img);
                    // System.out.println("Imagen desde classpath: " + res.toExternalForm());
                    return;
                }
            }

            // 2) filesystem: absoluto o relativo a user.dir, o src/main/resources
            File f = new File(rutaNorm);
            if (!f.exists()) f = new File(System.getProperty("user.dir"), rutaNorm);
            if (!f.exists()) f = new File("src/main/resources", rutaNorm);
            if (f.exists()) {
                Image img = new Image(f.toURI().toString(), false);
                if (!img.isError()) {
                    imgV_ImagenObra.setImage(img);
                    // System.out.println("Imagen desde archivo: " + f.getAbsolutePath());
                    return;
                }
            }

            // 3) intentar con leading slash en getResource
            res = getClass().getResource(rutaNorm.startsWith("/") ? rutaNorm : ("/" + rutaNorm));
            if (res != null) {
                Image img = new Image(res.toExternalForm(), false);
                if (!img.isError()) {
                    imgV_ImagenObra.setImage(img);
                    return;
                }
            }

            // no encontrada -> limpiar
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

    private void configurarEventos() {
        btn_crear.setOnAction(e -> crearObra());
        btn_actualizar.setOnAction(e -> actualizarObra());
        btn_eliminar.setOnAction(e -> eliminarObra());
        btn_limpiar.setOnAction(e -> limpiarCampos());
        btn_buscar.setOnAction(e -> {
            // si tienes función de buscar por id, puedes llamarla aquí
        });

        // cuando el usuario selecciona una ruta en el combo, cargar imagen silenciosamente
        cmb_imagen.valueProperty().addListener((obs, oldVal, newVal) -> {
            cargarImagenDesdeRuta(newVal);
        });
    }

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
            // Forzar carga silenciosa
            cargarImagenDesdeRuta(rutaObra);
        } else {
            cmb_imagen.getSelectionModel().clearSelection();
            imgV_ImagenObra.setImage(null);
        }

        // Seleccionar audio (similar)
        if (obra.getRutaAudio() != null) {
            String rutaAud = obra.getRutaAudio().trim().replace("\\", "/");
            cmb_audio.getItems().stream()
                .filter(a -> a != null && a.trim().replace("\\", "/").equals(rutaAud))
                .findFirst()
                .ifPresent(a -> cmb_audio.getSelectionModel().select(a));
        } else {
            cmb_audio.getSelectionModel().clearSelection();
        }

        // Sala: buscar por nombre y seleccionar el objeto Sala
        if (obra.getNombreSala() != null) {
            String nombreSala = obra.getNombreSala();
            cmb_sala.getItems().stream()
                .filter(s -> s.getNombreSala() != null && s.getNombreSala().equals(nombreSala))
                .findFirst()
                .ifPresent(s -> cmb_sala.getSelectionModel().select(s));
        } else {
            cmb_sala.getSelectionModel().clearSelection();
        }

        // TipoObra: buscar por nombre
        if (obra.getNombreTipoObra() != null) {
            String nombreTipo = obra.getNombreTipoObra();
            cmb_tipoObra.getItems().stream()
                .filter(t -> t.getNombreTipoObra() != null && t.getNombreTipoObra().equals(nombreTipo))
                .findFirst()
                .ifPresent(t -> cmb_tipoObra.getSelectionModel().select(t));
        } else {
            cmb_tipoObra.getSelectionModel().clearSelection();
        }

        // Autor: comparar "Nombre Apellido"
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

    private java.sql.Date parseDateOrNull(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;
        try {
            LocalDate ld = LocalDate.parse(texto.trim(), dateFormatter);
            return java.sql.Date.valueOf(ld);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private void crearObra() {
        try {
            Obra nueva = new Obra();
            nueva.setTitulo(txt_titulo.getText());
            nueva.setDescripcion(txt_descripcion.getText());

            java.sql.Date fechaC = parseDateOrNull(txt_fechaC.getText());
            java.sql.Date fechaI = parseDateOrNull(txt_fechaI.getText());
            nueva.setFechaCreacion(fechaC);
            nueva.setFechaIngreso(fechaI);

            // normalizar ruta seleccionada antes de guardar
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
        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato de fecha inválido", "Use el formato YYYY-MM-DD", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

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

            // rutas normalizadas
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
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
