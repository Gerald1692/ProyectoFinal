package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Controllers.SesionSala;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class SalaSimple1Controller {

    @FXML private GridPane gridObras;
    @FXML private ScrollPane scrollPane;
    @FXML private Button btn_Salir;

    @FXML
    public void initialize() {
        cargarObras();
    }

    private void cargarObras() {
        try {
            ObraDAO dao = new ObraDAO();
            List<Obra> obras = dao.obtenerObrasPorSala(1); // Cambia el ID según tu sala

            int column = 0;
            int row = 0;
            
            for (Obra obra : obras) {
                VBox obraBox = crearObraBox(obra);
                gridObras.add(obraBox, column, row);
                
                column++;
                if (column == 3) { // 3 columnas por fila
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox crearObraBox(Obra obra) {
        VBox box = new VBox();
        box.getStyleClass().add("section");
        box.setPadding(new Insets(15));
        box.setSpacing(10);
        box.setOnMouseClicked(e -> abrirDetalleObra(obra.getId()));
        
        // Imagen
        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("section-image");
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        cargarImagen(imageView, obra.getRutaImagen());
        
        // Título
        Text titulo = new Text(obra.getTitulo());
        titulo.getStyleClass().add("section-text");
        titulo.setWrappingWidth(180);
        
        box.getChildren().addAll(imageView, titulo);
        return box;
    }

    private void cargarImagen(ImageView imageView, String rutaImg) {
        if (rutaImg == null || rutaImg.isBlank()) {
            cargarPlaceholder(imageView);
            return;
        }
        
        try {
            rutaImg = rutaImg.replace("\\", "/");
            
            // Intentar cargar como recurso interno
            URL imgUrl = getClass().getResource("/" + rutaImg);
            if (imgUrl != null) {
                imageView.setImage(new Image(imgUrl.toExternalForm()));
                return;
            }
            
            // Intentar cargar como archivo externo
            File imgFile = new File(rutaImg);
            if (imgFile.exists()) {
                imageView.setImage(new Image(imgFile.toURI().toString()));
            } else {
                cargarPlaceholder(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cargarPlaceholder(imageView);
        }
    }

    private void cargarPlaceholder(ImageView imageView) {
        try {
            URL placeholderUrl = getClass().getResource("/imagenes/placeholder.png");
            if (placeholderUrl != null) {
                imageView.setImage(new Image(placeholderUrl.toExternalForm()));
            }
        } catch (Exception e) {
            System.err.println("Error cargando placeholder");
        }
    }

    private void abrirDetalleObra(int idObra) {
        SesionSala.setIdObra(idObra);
        try {
            App.setRoot("Sala1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volverAInicio() {
        try {
            App.setRoot("primary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}