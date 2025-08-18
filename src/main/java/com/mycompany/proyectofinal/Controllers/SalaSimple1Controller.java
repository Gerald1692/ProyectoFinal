package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.util.SesionSala;
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
        int idSala = SesionSala.getIdSala(); // ← Recupera el idSala que seteó PrimaryController
        cargarObrasDeSala(idSala);
    }

   

    private void cargarObrasDeSala(int idSala) {
        try {
            ObraDAO dao = new ObraDAO();
            List<Obra> obras = dao.obtenerObrasPorSala(idSala);

            int col = 0, row = 0;
            for (Obra obra : obras) {
                VBox contenedor = new VBox(5);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);

                if (obra.getRutaImagen() != null) {
                    try {
                        imageView.setImage(new Image("file:" + obra.getRutaImagen()));
                    } catch (Exception e) {
                        System.out.println("Error cargando imagen: " + obra.getRutaImagen());
                    }
                }

                Label titulo = new Label(obra.getTitulo());
                contenedor.getChildren().addAll(imageView, titulo);

                gridObras.add(contenedor, col, row);

                col++;
                if (col == 3) { col = 0; row++; }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
