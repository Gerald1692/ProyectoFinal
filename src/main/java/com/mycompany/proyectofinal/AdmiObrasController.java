package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.*;
import com.mycompany.proyectofinal.ModelosPOJOs.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
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

    private ObraDAO obraDAO;
    private SalaDAO salaDAO;
    private TipoObraDAO tipoObraDAO;
    private AutorDAO autorDAO;
    private ObraAutorDAO obraAutorDAO;
    private ObservableList<ObraCompleta> obrasList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private AnchorPane col_idObra;
    @FXML private TableColumn<ObraCompleta, String> col_autor;

    @FXML
    private Button btn_buscar;

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

        tblObras.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    llenarCampos(newSelection);
                }
            }
        );
    }

 private void cargarCombos() {
    try {
        cmb_sala.setItems(FXCollections.observableArrayList(salaDAO.listarSalas()));
        cmb_tipoObra.setItems(FXCollections.observableArrayList(tipoObraDAO.listarTiposObra()));
        cmb_autor.setItems(FXCollections.observableArrayList(autorDAO.listarAutores()));
    } catch (SQLException ex) {
        mostrarAlerta("Error al cargar combos", ex.getMessage(), Alert.AlertType.ERROR);
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

        cmb_imagen.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                try {
                    imgV_ImagenObra.setImage(new Image(newVal));
                } catch (Exception e) {
                    mostrarAlerta("Error de imagen", "No se pudo cargar la imagen", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void llenarCampos(ObraCompleta obra) {
    txt_id.setText(String.valueOf(obra.getIdObra()));
    txt_titulo.setText(obra.getTitulo());
    txt_descripcion.setText(obra.getDescripcion());

    if (obra.getFechaCreacion() != null) {
        txt_fechaC.setText(obra.getFechaCreacion().toString());
    }
    if (obra.getFechaIngreso() != null) {
        txt_fechaI.setText(obra.getFechaIngreso().toString());
    }

    cmb_imagen.setValue(obra.getRutaImagen());
    cmb_audio.setValue(obra.getRutaAudio());

    // Seleccionar Sala
    cmb_sala.getItems().stream()
        .filter(s -> s.getNombreSala().equals(obra.getNombreSala()))
        .findFirst()
        .ifPresent(s -> cmb_sala.getSelectionModel().select(s));

    // Seleccionar Tipo de Obra
    cmb_tipoObra.getItems().stream()
        .filter(t -> t.getNombreTipoObra().equals(obra.getNombreTipoObra()))
        .findFirst()
        .ifPresent(t -> cmb_tipoObra.getSelectionModel().select(t));

    // Seleccionar Autor
    if (obra.getNombreAutor() != null) {
        cmb_autor.getItems().stream()
            .filter(a -> (a.getNombre() + " " + a.getApellido()).equals(obra.getNombreAutor()))
            .findFirst()
            .ifPresent(a -> cmb_autor.getSelectionModel().select(a));
    }
}


   

    

    private void crearObra() {
        try {
            Obra nueva = new Obra();
            nueva.setTitulo(txt_titulo.getText());
            nueva.setDescripcion(txt_descripcion.getText());
            nueva.setFechaCreacion(java.sql.Date.valueOf(LocalDate.parse(txt_fechaC.getText(), dateFormatter)));
            nueva.setFechaIngreso(java.sql.Date.valueOf(LocalDate.parse(txt_fechaI.getText(), dateFormatter)));
            nueva.setRutaImagen(cmb_imagen.getValue());
            nueva.setRutaAudio(cmb_audio.getValue());

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
            obra.setFechaCreacion(java.sql.Date.valueOf(LocalDate.parse(txt_fechaC.getText(), dateFormatter)));
            obra.setFechaIngreso(java.sql.Date.valueOf(LocalDate.parse(txt_fechaI.getText(), dateFormatter)));
            obra.setRutaImagen(cmb_imagen.getValue());
            obra.setRutaAudio(cmb_audio.getValue());

            Sala salaSeleccionada = cmb_sala.getSelectionModel().getSelectedItem();
            TipoObra tipoSeleccionado = cmb_tipoObra.getSelectionModel().getSelectedItem();
            if (salaSeleccionada == null || tipoSeleccionado == null) {
                mostrarAlerta("Campos requeridos", "Seleccione sala y tipo de obra", Alert.AlertType.WARNING);
                return;
            }

            obra.setSalaId(salaSeleccionada.getIdSala());
            obra.setTipoObraId(tipoSeleccionado.getIdTipoObra());

            obraDAO.actualizarObra(obra);
            obraAutorDAO.desasociarTodosAutoresObra(obra.getId());
            Autor autorSeleccionado = cmb_autor.getSelectionModel().getSelectedItem();
            if (autorSeleccionado != null) {
                obraAutorDAO.asociarAutorObra(obra.getId(), autorSeleccionado.getId(), 1);
            }

            mostrarAlerta("Éxito", "Obra actualizada correctamente", Alert.AlertType.INFORMATION);
            cargarObras();
        } catch (SQLException e) {
            mostrarAlerta("Error en BD", e.getMessage(), Alert.AlertType.ERROR);
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
