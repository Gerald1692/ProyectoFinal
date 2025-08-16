package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sala3Controller implements Initializable {
    private MediaPlayer mediaPlayer;

    @FXML
    private void irSalaPrincipal() throws IOException {
        App.setRoot("SalaSimple3");
    }
    
    @FXML
    private void volverLogin() throws IOException {
        App.setRoot("Login");
    }
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}