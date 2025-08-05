package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Obra;
import com.mycompany.proyectofinal.Modelos.ObraModelo;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CatalogoController  {
    @FXML private ListView<Obra> listObras;
    @FXML private ImageView imgPreview;
    @FXML private TextField txtBusqueda;
    
    private final ObraModelo obraModelo = new ObraModelo();

    public void initialize() {
        cargarObras();
        
        // Listener para mostrar previsualización
        listObras.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> mostrarPreview(newValue)
        );
    }

    private void cargarObras() {
        listObras.setItems(obraModelo.obtenerTodasObras());
    }
    
    private void mostrarPreview(Obra obra) {
        if (obra != null) {
            imgPreview.setImage(obra.getImagen());
        }
    }

    @FXML
    private void buscarObras() {
        String criterio = txtBusqueda.getText();
        listObras.setItems(obraModelo.buscarObras(criterio));
    }

    @FXML
    private void verDetalle() {
        Obra seleccionada = listObras.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            try {
                // Pasar obra seleccionada a detalles
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DetallesObra.fxml"));
                Parent root = loader.load();
                
                DetallesObraController controller = loader.getController();
                controller.setObra(seleccionada);
                
                App.primaryStage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void salir() {
        try {
            App.cambiarVentana("Login", "Inicio de Sesión");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}