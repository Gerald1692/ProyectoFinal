package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Sala1Controller implements Initializable {
    @FXML
    private BorderPane rootPane; // Referencia al nodo raíz
    
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar manejador para cuando se cierre la ventana
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                detenerSonido();
            });
        });
    }
    
    // Método para detener el sonido
    private void detenerSonido() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Libera recursos
            mediaPlayer = null;
        }
    }

    @FXML
    private void volverLogin() throws IOException {
        detenerSonido(); // Detiene el sonido antes de cambiar de vista
        App.setRoot("Login");
    }
    
    @FXML
    private void manejarSalir() {
        detenerSonido(); // Detiene el sonido antes de salir
        System.exit(0);
    }
    
    @FXML
    private void irSalaPrincipal() throws IOException {
        detenerSonido(); // Detiene el sonido antes de cambiar de vista
        App.setRoot("SalaSimple1");
    }
    
    @FXML
    private void reproducirSonido() {
        try {
            detenerSonido(); // Detiene cualquier sonido previo
            
            URL resource = getClass().getResource("/SONIDOSANIMALES/3cuernos.m4a");
            if (resource == null) {
                throw new RuntimeException("Archivo de audio no encontrado");
            }
            
            String audioPath = resource.toExternalForm();
            Media media = new Media(audioPath);
            mediaPlayer = new MediaPlayer(media);
            
            // Configurar para detener el sonido cuando termine
            mediaPlayer.setOnEndOfMedia(() -> detenerSonido());
            
            mediaPlayer.play();
            
        } catch (Exception e) {
            System.err.println("Error al reproducir sonido: " + e.getMessage());
            e.printStackTrace();
        }
    }
}