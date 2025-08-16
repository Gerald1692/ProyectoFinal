/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
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

    @FXML private AnchorPane col_idObra;
    @FXML private TableView<Obra> tblObras;
    @FXML private TableColumn<Obra, Integer> col_id;
    @FXML private TableColumn<Obra, String> col_titulo;
    @FXML private TableColumn<Obra, String> col_descripcion;
    @FXML private TableColumn<Obra, LocalDate> col_fechaCreacion;
    @FXML private TableColumn<Obra, LocalDate> col_fechaIngreso;
    @FXML private TableColumn<Obra, String> col_autor;
    @FXML private TableColumn<Obra, String> col_sala;
    @FXML private TableColumn<Obra, String> col_tipoObra;
    @FXML private TableColumn<Obra, String> col_tecnica;
    
    @FXML private TextArea txt_id;
    @FXML private TextArea txt_titulo;
    @FXML private TextArea txt_descripcion;
    @FXML private TextArea txt_fechaC;
    @FXML private TextArea txt_fechaI;
    
    @FXML private ComboBox<String> cmb_imagen;
    @FXML private ComboBox<String> cmb_audio;
    @FXML private ComboBox<String> cmb_autor;
    @FXML private ComboBox<String> cmb_tipoObra;
    @FXML private ComboBox<String> cmb_sala;
    
    @FXML private ImageView imgV_ImagenObra;
    
    @FXML private Button btn_crear;
    @FXML private Button btn_eliminar;
    @FXML private Button btn_buscar;
    @FXML private Button btn_actualizar;
    @FXML private Button btn_limpiar;

    private ObraDAO obraDAO;
    private ObservableList<Obra> obrasList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obraDAO = new ObraDAO();
        configurarTabla();
        cargarDatos();
        cargarCombos();
        configurarEventos();
    }
    
    private void configurarTabla() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("idObra"));
        col_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        col_fechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        col_fechaIngreso.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        col_autor.setCellValueFactory(new PropertyValueFactory<>("autores"));
        col_sala.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
        col_tipoObra.setCellValueFactory(new PropertyValueFactory<>("nombreTipoObra"));
        col_tecnica.setCellValueFactory(new PropertyValueFactory<>("tecnicaTipoObra"));
        
        // Selección de fila
        tblObras.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> mostrarDetallesObra(newValue));
    }
    
    private void cargarDatos() {
        try {
            obrasList = FXCollections.observableArrayList(obraDAO.obtenerTodasObras());
            tblObras.setItems(obrasList);
        } catch (SQLException ex) {
            mostrarAlerta("Error", "Error al cargar obras: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void cargarCombos() {
        try {
            // Cargar combos con datos de la base de datos
            cargarCombo(cmb_tipoObra, obraDAO.obtenerTiposObra());
            cargarCombo(cmb_sala, obraDAO.obtenerSalas());
            cargarCombo(cmb_autor, obraDAO.obtenerAutores());
            
            // Combos de imágenes y audios
            cmb_imagen.getItems().addAll("imagen1.png", "imagen2.jpg");
            cmb_audio.getItems().addAll("audio1.mp3", "audio2.wav");
        } catch (SQLException ex) {
            mostrarAlerta("Error", "Error al cargar combos: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private <T> void cargarCombo(ComboBox<String> combo, List<T> items) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (T item : items) {
            options.add(item.toString());
        }
        combo.setItems(options);
    }
    
    private void configurarEventos() {
        btn_crear.setOnAction(event -> crearObra());
        btn_actualizar.setOnAction(event -> actualizarObra());
        btn_eliminar.setOnAction(event -> eliminarObra());
        btn_limpiar.setOnAction(event -> limpiarCampos());
        btn_buscar.setOnAction(event -> buscarObra());
        
        cmb_imagen.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> cargarImagen(newValue));
    }
    
    private void mostrarDetallesObra(Obra obra) {
        if (obra != null) {
            txt_id.setText(String.valueOf(obra.getIdObra()));
            txt_titulo.setText(obra.getTitulo());
            txt_descripcion.setText(obra.getDescripcion());
            txt_fechaC.setText(obra.getFechaCreacion().toString());
            txt_fechaI.setText(obra.getFechaIngreso().toString());
            
            // Seleccionar valores en combos
            cmb_tipoObra.getSelectionModel().select(obra.getNombreTipoObra());
            cmb_sala.getSelectionModel().select(obra.getNombreSala());
            
            // Cargar imagen
            if (obra.getRutaImagen() != null && !obra.getRutaImagen().isEmpty()) {
                cargarImagen(obra.getRutaImagen());
            }
        }
    }
    
    private void cargarImagen(String rutaImagen) {
        try {
            Image image = new Image(getClass().getResourceAsStream(rutaImagen));
            imgV_ImagenObra.setImage(image);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la imagen", Alert.AlertType.ERROR);
        }
    }
    
    private void crearObra() {
        try {
            Obra obra = new Obra();
            obra.setTitulo(txt_titulo.getText());
            obra.setDescripcion(txt_descripcion.getText());
            obra.setFechaCreacion(LocalDate.parse(txt_fechaC.getText()));
            obra.setFechaIngreso(LocalDate.parse(txt_fechaI.getText()));
            obra.setRutaImagen(cmb_imagen.getSelectionModel().getSelectedItem());
            obra.setRutaAudio(cmb_audio.getSelectionModel().getSelectedItem());
            
            // Obtener IDs de los combos (necesitarías implementar esta lógica)
            // obra.setIdTipoObra(...);
            // obra.setIdSala(...);
            
            int idGenerado = obraDAO.insertarObra(obra);
            mostrarAlerta("Éxito", "Obra creada con ID: " + idGenerado, Alert.AlertType.INFORMATION);
            cargarDatos();
            limpiarCampos();
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarAlerta("Error", "No se pudo crear la obra: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void actualizarObra() {
        try {
            if (txt_id.getText().isEmpty()) {
                mostrarAlerta("Advertencia", "Seleccione una obra para actualizar", Alert.AlertType.WARNING);
                return;
            }
            
            Obra obra = new Obra();
            obra.setIdObra(Integer.parseInt(txt_id.getText()));
            obra.setTitulo(txt_titulo.getText());
            obra.setDescripcion(txt_descripcion.getText());
            obra.setFechaCreacion(LocalDate.parse(txt_fechaC.getText()));
            obra.setFechaIngreso(LocalDate.parse(txt_fechaI.getText()));
            obra.setRutaImagen(cmb_imagen.getSelectionModel().getSelectedItem());
            obra.setRutaAudio(cmb_audio.getSelectionModel().getSelectedItem());
            
            // Obtener IDs de los combos
            // obra.setIdTipoObra(...);
            // obra.setIdSala(...);
            
            boolean actualizado = obraDAO.actualizarObra(obra);
            if (actualizado) {
                mostrarAlerta("Éxito", "Obra actualizada correctamente", Alert.AlertType.INFORMATION);
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la obra", Alert.AlertType.ERROR);
            }
        } catch (SQLException | NumberFormatException ex) {
            mostrarAlerta("Error", "No se pudo actualizar la obra: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void eliminarObra() {
        try {
            if (txt_id.getText().isEmpty()) {
                mostrarAlerta("Advertencia", "Seleccione una obra para eliminar", Alert.AlertType.WARNING);
                return;
            }
            
            int idObra = Integer.parseInt(txt_id.getText());
            boolean eliminado = obraDAO.eliminarObra(idObra);
            
            if (eliminado) {
                mostrarAlerta("Éxito", "Obra eliminada correctamente", Alert.AlertType.INFORMATION);
                cargarDatos();
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la obra", Alert.AlertType.ERROR);
            }
        } catch (SQLException | NumberFormatException ex) {
            mostrarAlerta("Error", "No se pudo eliminar la obra: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void buscarObra() {
        // Implementar lógica de búsqueda según criterios
        mostrarAlerta("Información", "Función de búsqueda no implementada aún", Alert.AlertType.INFORMATION);
    }
    
    private void limpiarCampos() {
        txt_id.clear();
        txt_titulo.clear();
        txt_descripcion.clear();
        txt_fechaC.clear();
        txt_fechaI.clear();
        cmb_imagen.getSelectionModel().clearSelection();
        cmb_audio.getSelectionModel().clearSelection();
        cmb_tipoObra.getSelectionModel().clearSelection();
        cmb_sala.getSelectionModel().clearSelection();
        cmb_autor.getSelectionModel().clearSelection();
        imgV_ImagenObra.setImage(null);
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}