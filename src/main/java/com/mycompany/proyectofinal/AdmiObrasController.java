/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author admar
 */
public class AdmiObrasController implements Initializable {

    @FXML
    private AnchorPane col_idObra;
    @FXML
    private TableColumn<?, ?> col_tituloObra;
    @FXML
    private TableColumn<?, ?> col_descripcionObra;
    @FXML
    private TableColumn<?, ?> col_fechaCreacioObra;
    @FXML
    private TableColumn<?, ?> col_fechaIngresoObra;
    @FXML
    private TableColumn<?, ?> col_autorObra;
    @FXML
    private TextArea txt_id;
    @FXML
    private TextArea txt_autor;
    @FXML
    private TextArea txt_fechaI;
    @FXML
    private TextArea txt_fechaC;
    @FXML
    private TextArea txt_descripcion;
    @FXML
    private TextArea txt_tilulo;
    @FXML
    private ComboBox<?> cmb_imagen;
    @FXML
    private ComboBox<?> cmb_audio;
    @FXML
    private ImageView imgV_ImagenObra;
    @FXML
    private Button btn_crear;
    @FXML
    private Button btn_eliminar;
    @FXML
    private Button btn_buscar;
    @FXML
    private Button btn_actualizar;
    @FXML
    private Button btn_limpiar;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
