// Sala1Controller.java
package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SalaSimple3Controller implements Initializable {

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
private void irSalaSimple3() throws IOException {
    App.setRoot("SalaSimple3");
}
    @FXML
    private void irSalaPrincipal() throws IOException {
        App.setRoot("primary");
    }
    
    // Nuevos métodos para navegar a salas específicas
    @FXML
    private void irSala1() throws IOException {
        App.setRoot("Sala1");
    }
    
    @FXML
    private void irSala2() throws IOException {
        App.setRoot("Sala2");
    }
     @FXML
    private void volverALaVistaPrincipal() throws IOException {
        App.setRoot("primary");
    }
    
    @FXML
    private void irSala3() throws IOException {
        App.setRoot("Sala3");
    }
}