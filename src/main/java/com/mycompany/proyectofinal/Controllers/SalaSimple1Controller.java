package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.Controllers.SesionSala;
import java.io.IOException;
import java.io.InputStream; // Importación añadida
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SalaSimple1Controller {

    @FXML
    private GridPane gridObras;

    private ObraDAO obraDAO = new ObraDAO();

    public void initialize() {
        int idSala = SesionSala.getIdSala();
        cargarObrasDeSala(idSala);
    }

    private void cargarObrasDeSala(int idSala) {
        try {
            ObraDAO dao = new ObraDAO();
            List<Obra> obras = dao.obtenerObrasPorSala(idSala);

            int col = 0, row = 0;
            for (Obra obra : obras) {
                VBox contenedor = new VBox(5);
                contenedor.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #ddd; -fx-border-radius: 5;");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                if (obra.getRutaImagen() != null && !obra.getRutaImagen().isEmpty()) {
                    try {
                        // Cargar imagen desde recursos
                        String rutaNormalizada = obra.getRutaImagen().replace("RECURSOS/", "");
                        InputStream is = getClass().getResourceAsStream("/" + rutaNormalizada);
                        if (is != null) {
                            imageView.setImage(new Image(is));
                        } else {
                            // Cargar placeholder si no se encuentra
                            InputStream placeholder = getClass().getResourceAsStream("/imagenes/placeholder.png");
                            if (placeholder != null) {
                                imageView.setImage(new Image(placeholder));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error cargando imagen: " + obra.getRutaImagen());
                        e.printStackTrace();
                    }
                }

                Label titulo = new Label(obra.getTitulo());
                titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                
                // Botón para ver detalles
                Button btnDetalles = new Button("Ver detalles");
                btnDetalles.setStyle("-fx-background-color: #4a86e8; -fx-text-fill: white;");
                btnDetalles.setOnAction(e -> mostrarDetalleObra(obra.getId()));
                
                contenedor.getChildren().addAll(imageView, titulo, btnDetalles);
                gridObras.add(contenedor, col, row);

                col++;
                if (col == 3) { 
                    col = 0; 
                    row++; 
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDetalleObra(int idObra) {
        try {
            // Guardar ID de obra en sesión
            SesionSala.setIdObra(idObra);
            
            // Navegar a vista de detalle
            App.setRoot("Sala1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void volverAInicio() {
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}