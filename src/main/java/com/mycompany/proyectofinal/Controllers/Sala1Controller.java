package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sala1Controller implements Initializable {
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void volverLogin() throws IOException {
        App.setRoot("Login");
    }
    
    @FXML
    private void manejarSalir() {
        System.exit(0);
    }
    
    @FXML
    private void irSalaPrincipal() throws IOException {
        App.setRoot("SalaSimple1");
    }
    
    @FXML
    private void irSala1() throws IOException {
        App.setRoot("Sala1");
    }
    
    @FXML
    private void irSala2() throws IOException {
        // Implementar lógica para Sala 2
        System.out.println("Navegando a Sala 2");
    }
    
    @FXML
    private void irSala3() throws IOException {
        // Implementar lógica para Sala 3
        System.out.println("Navegando a Sala 3");
    }
    
    @FXML
    private void reproducirSonido() {
        try {
            // Detener cualquier reproducción anterior
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            
            // Obtener la URL del recurso usando el classloader
            URL resource = getClass().getResource("/SONIDOSANIMALES/3cuernos.m4a");
            
            if (resource == null) {
                throw new RuntimeException("Archivo de audio no encontrado");
            }
            
            String audioPath = resource.toExternalForm();
            Media media = new Media(audioPath);
            mediaPlayer = new MediaPlayer(media);
            
            mediaPlayer.play();
            
        } catch (Exception e) {
            System.err.println("Error al reproducir sonido: " + e.getMessage());
            e.printStackTrace();
        }
    }
}