    // Sala1Controller.java
package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SalaSimple1Controller implements Initializable {

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
        App.setRoot("primary");
    }
      // AÑADE ESTOS MÉTODOS FALTANTES
    @FXML
    private void irATyrannosaurus() throws IOException {
        App.setRoot("Sala1", "Tyrannosaurus");
    }

    @FXML
    private void irATriceratops() throws IOException {
        App.setRoot("Sala1", "Triceratops");
    }

    @FXML
    private void irAVelociraptor() throws IOException {
        App.setRoot("Sala1", "Velociraptor");
    }
    // Nuevos métodos para navegar a salas específicas
    @FXML
    private void IRASALA1() throws IOException {
        App.setRoot("Sala1");
    }
  
   
    @FXML
    private void atras() throws IOException {
        App.setRoot("primary");
    }

}