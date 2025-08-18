package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class SalaSimple1Controller {

    @FXML
    private GridPane gridObras;

    private ObraDAO obraDAO = new ObraDAO();
    private static int idSala;

    public static void setIdSala(int id) {
        idSala = id;
    }

    public void initialize() {
        try {
            cargarObrasDeSala(idSala);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public void cargarObrasDeSala(int idSala) throws SQLException {
    gridObras.getChildren().clear();

    List<Obra> obras = obraDAO.obtenerObrasPorSala(idSala);
    int col = 0, row = 0;

    for (Obra obra : obras) {
        VBox contenedor = new VBox(5);
        contenedor.setPadding(new Insets(5));

        ImageView imageView = new ImageView();
        String rutaImagen = obra.getRutaImagen();
        
        // Cargar imagen como recurso interno
        if (rutaImagen != null && !rutaImagen.isBlank()) {
            try {
                // Normalizar ruta: quitar prefijo "RECURSOS/" si existe
                String rutaNormalizada = rutaImagen.replace("RECURSOS/", "");
                
                // Cargar imagen desde recursos
                InputStream imgStream = getClass().getResourceAsStream("/" + rutaNormalizada);
                if (imgStream != null) {
                    Image img = new Image(imgStream);
                    imageView.setImage(img);
                } else {
                    System.err.println("No se encontró la imagen: " + rutaNormalizada);
                    // Cargar placeholder si la imagen no existe
                    imgStream = getClass().getResourceAsStream("/imagenes/placeholder.png");
                    if (imgStream != null) {
                        imageView.setImage(new Image(imgStream));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error cargando imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        
        Label titulo = new Label(obra.getTitulo());
        contenedor.getChildren().addAll(imageView, titulo);
        contenedor.setUserData(obra.getId());
        contenedor.setOnMouseClicked(this::abrirObra);
        gridObras.add(contenedor, col, row);

        col++;
        if (col == 3) {
            col = 0;
            row++;
        }
    }
   }
    private void abrirObra(MouseEvent event) {
        Node source = (Node) event.getSource();
        Integer idObra = (Integer) source.getUserData();

        if (idObra != null) {
            mostrarDetalleObra(idObra);
        }
    }

    private void mostrarDetalleObra(int id) {
        try {
            Obra obra = obraDAO.obtenerObraPorId(id);
            gridObras.getChildren().clear();

            VBox detalle = new VBox(10);
            Label titulo = new Label(obra.getTitulo());
            titulo.setStyle("-fx-font-size:18; -fx-font-weight:bold;");

            Label descripcion = new Label(obra.getDescripcion());
            descripcion.setWrapText(true);

            ImageView imageView = new ImageView();
            if (obra.getRutaImagen() != null) {
                imageView.setImage(new Image("file:" + obra.getRutaImagen()));
            }
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);

            detalle.getChildren().addAll(titulo, descripcion, imageView);

            gridObras.add(detalle, 0, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
