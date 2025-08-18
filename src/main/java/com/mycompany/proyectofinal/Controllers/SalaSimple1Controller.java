package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SalaSimple1Controller {

    @FXML
    private GridPane gridPane;

    private int idSala;

    public void cargarObrasDeSala(int idSala) {
        this.idSala = idSala;
        gridPane.getChildren().clear();

        ObraDAO obraDAO = new ObraDAO();
        try {
            List<Obra> obras = obraDAO.obtenerObrasPorSala(idSala);

            int col = 0, row = 0;
            for (Obra obra : obras) {
                VBox box = new VBox(10);
                ImageView img = new ImageView(new Image(obra.getRutaImagen()));
                img.setFitHeight(120);
                img.setPreserveRatio(true);

                Label titulo = new Label(obra.getTitulo());
                box.getChildren().addAll(img, titulo);

                // evento clic -> abrir detalle
                box.setOnMouseClicked(e -> abrirDetalleObra(obra.getId()));

                gridPane.add(box, col, row);
                col++;
                if (col > 3) { col = 0; row++; }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void abrirDetalleObra(int idObra) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/proyectofinal/Vistas/DetalleObra.fxml"));
            Parent root = loader.load();

            Sala1Controller controller = loader.getController();
            controller.cargarObra(idObra);

            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
