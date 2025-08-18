package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.App;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.SQLException;

public class Sala1Controller {

    @FXML private ImageView imagenObra;
    @FXML private Label tituloLabel;
    @FXML private Label subtituloLabel;
    @FXML private TextArea descripcionArea;
    @FXML private Button playButton;
    @FXML private Label infoSalaLabel;
    @FXML private Label estadoLabel;

    private MediaPlayer mediaPlayer;
    private String rutaAudioActual;

    /**
     * Se dispara cuando el usuario hace click en una obra dentro del Grid de SalaSimple1.
     * El idObra debe estar seteado en el userData del nodo.
     */
    @FXML
    private void abrirObra(MouseEvent event) {
        Node source = (Node) event.getSource();
        Integer idObra = (Integer) source.getUserData(); // viene del GridPane
        if (idObra != null) {
            cargarObra(idObra);
        }
    }

    public void cargarObra(int idObra) {
        ObraDAO dao = new ObraDAO();
        try {
            Obra obra = dao.obtenerObraPorId(idObra);
            if (obra == null) {
                tituloLabel.setText("Obra no encontrada");
                descripcionArea.setText("");
                playButton.setDisable(true);
                return;
            }

            // Texto
            tituloLabel.setText(obra.getTitulo() != null ? obra.getTitulo() : "");
            subtituloLabel.setText(obra.getNombreTipoObra() != null ? obra.getNombreTipoObra() : "");
            descripcionArea.setText(obra.getDescripcion() != null ? obra.getDescripcion() : "");
            infoSalaLabel.setText(obra.getNombreSala() != null ? obra.getNombreSala() : "Sala " + obra.getSalaId());
            estadoLabel.setText("En exhibición");

            // Imagen segura
            String rutaImg = obra.getRutaImagen();
            Image img = null;
            if (rutaImg != null && !rutaImg.isBlank()) {
                try {
                    String source = rutaImg.startsWith("file:") || rutaImg.startsWith("http") ? rutaImg : "file:" + rutaImg;
                    img = new Image(source, false);
                } catch (Exception ex) { img = null; }
            }
            if (img == null || img.isError()) {
                try (InputStream is = getClass().getResourceAsStream("/imagenes/placeholder.png")) {
                    if (is != null) img = new Image(is);
                } catch (Exception e) { }
            }
            imagenObra.setImage(img);

            // Audio
            rutaAudioActual = obra.getRutaAudio();
            playButton.setDisable(rutaAudioActual == null || rutaAudioActual.isBlank());
            stopAndDisposePlayer();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void reproducirSonido() {
        if (rutaAudioActual == null || rutaAudioActual.isBlank()) return;
        try {
            if (mediaPlayer == null) {
                String src = rutaAudioActual.startsWith("file:") || rutaAudioActual.startsWith("http")
                        ? rutaAudioActual : "file:" + rutaAudioActual;
                Media media = new Media(src);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                    playButton.setText("Reproducir Sonido");
                });
            }
            if ("Reproducir Sonido".equals(playButton.getText()) || mediaPlayer.getStatus().toString().equals("PAUSED")) {
                mediaPlayer.play();
                playButton.setText("Pausar Sonido");
            } else {
                mediaPlayer.pause();
                playButton.setText("Reproducir Sonido");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            playButton.setDisable(true);
        }
    }

    @FXML
    private void volverSala() {
        stopAndDisposePlayer();
        try {
            App.setRoot("SalaSimple1"); // vuelve a la lista de obras
        } catch (Exception e) {
            Stage s = (Stage) tituloLabel.getScene().getWindow();
            s.close();
        }
    }

    private void stopAndDisposePlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                playButton.setText("Reproducir Sonido");
            }
        } catch (Exception ignored) {}
    }
}
