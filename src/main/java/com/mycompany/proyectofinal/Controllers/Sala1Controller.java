package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

public class Sala1Controller {

    @FXML
    private ImageView imagenObra;
    @FXML
    private Label tituloLabel, salaLabel, tipoLabel, tecnicaLabel, fechaLabel;
    @FXML
    private TextArea descripcionArea;

    public void cargarObra(int idObra) {
        ObraDAO obraDAO = new ObraDAO();
        try {
            Obra obra = obraDAO.obtenerObraPorId(idObra);

            if (obra != null) {
                imagenObra.setImage(new Image(obra.getRutaImagen()));
                tituloLabel.setText(obra.getTitulo());
                salaLabel.setText("Sala: " + obra.getNombreSala());
                tipoLabel.setText("Tipo: " + obra.getNombreTipoObra());
                tecnicaLabel.setText("Técnica: " + obra.getTecnica());
                descripcionArea.setText(obra.getDescripcion());
                fechaLabel.setText("Ingreso: " + obra.getFechaIngreso());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
