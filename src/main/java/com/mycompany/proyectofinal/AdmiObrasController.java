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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author admar
 */
public class AdmiObrasController implements Initializable {

    @FXML
    private TableView<?> TObras;
    @FXML
    private TableColumn<?, ?> ColIdObra;
    @FXML
    private TableColumn<?, ?> ColTitulo;
    @FXML
    private TableColumn<?, ?> ColDescripcion;
    @FXML
    private TableColumn<?, ?> ColFechaCreacion;
    @FXML
    private TableColumn<?, ?> ColFechaIngreso;
    @FXML
    private TextArea txtIdObra;
    @FXML
    private TextArea txtTitulo;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextArea txtFechaCreacion;
    @FXML
    private TextArea txtFechaIngreso;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnMostrar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
