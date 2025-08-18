package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.ObraDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Obra;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Controllers.SesionSala;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    @FXML
    public void initialize() {
        // Cargar obra desde sesión
        int idObra = SesionSala.getIdObra();
        if(idObra > 0) {
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

            // Cargar texto
            tituloLabel.setText(obra.getTitulo() != null ? obra.getTitulo() : "");
            subtituloLabel.setText(obra.getNombreTipoObra() != null ? obra.getNombreTipoObra() : "");
            descripcionArea.setText(obra.getDescripcion() != null ? obra.getDescripcion() : "");
            infoSalaLabel.setText(obra.getNombreSala() != null ? obra.getNombreSala() : "Sala " + obra.getSalaId());
            estadoLabel.setText("En exhibición");

            // Cargar imagen - SOLUCIÓN MEJORADA
            String rutaImg = obra.getRutaImagen();
            if (rutaImg != null && !rutaImg.isBlank()) {
                try {
                    // Normalizar ruta
                    rutaImg = rutaImg.replace("\\", "/");
                    
                    // Intentar cargar como recurso
                    URL imgUrl = getClass().getResource("/" + rutaImg);
                    if (imgUrl != null) {
                        Image img = new Image(imgUrl.toExternalForm());
                        imagenObra.setImage(img);
                    } 
                    // Intentar cargar como archivo externo
                    else {
                        File imgFile = new File(rutaImg);
                        if (imgFile.exists()) {
                            Image img = new Image(imgFile.toURI().toString());
                            imagenObra.setImage(img);
                        } else {
                            throw new Exception("Imagen no encontrada: " + rutaImg);
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error cargando imagen: " + ex.getMessage());
                    cargarPlaceholder();
                }
            } else {
                cargarPlaceholder();
            }

            // Configurar audio
            rutaAudioActual = obra.getRutaAudio();
            playButton.setDisable(rutaAudioActual == null || rutaAudioActual.isBlank());
            stopAndDisposePlayer();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarPlaceholder() {
        try {
            URL placeholderUrl = getClass().getResource("/imagenes/placeholder.png");
            if (placeholderUrl != null) {
                Image placeholder = new Image(placeholderUrl.toExternalForm());
                imagenObra.setImage(placeholder);
            }
        } catch (Exception e) {
            System.err.println("Error cargando placeholder");
        }
    }

    @FXML
    private void reproducirSonido() {
        if (rutaAudioActual == null || rutaAudioActual.isBlank()) return;
        try {
            if (mediaPlayer == null) {
                String rutaAudio = rutaAudioActual.replace("\\", "/");
                String mediaSource;
                
                // Intentar cargar como recurso interno
                URL audioUrl = getClass().getResource("/" + rutaAudio);
                if (audioUrl != null) {
                    mediaSource = audioUrl.toExternalForm();
                } 
                // Intentar cargar como archivo externo
                else {
                    File audioFile = new File(rutaAudio);
                    if (audioFile.exists()) {
                        mediaSource = audioFile.toURI().toString();
                    } else {
                        System.err.println("Archivo de audio no encontrado: " + rutaAudio);
                        playButton.setDisable(true);
                        return;
                    }
                }
                
                Media media = new Media(mediaSource);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                    playButton.setText("Reproducir Sonido");
                });
            }
            if ("Reproducir Sonido".equals(playButton.getText()) || 
                (mediaPlayer != null && mediaPlayer.getStatus().toString().equals("PAUSED"))) {
                mediaPlayer.play();
                playButton.setText("Pausar Sonido");
            } else if (mediaPlayer != null) {
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
    
    @FXML
    private void volverAInicio() {
        stopAndDisposePlayer();
        try {
            App.setRoot("primary"); // vuelve al inicio de sesión
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