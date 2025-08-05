package com.mycompany.proyectofinal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class REGISTRARSEController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }    

    @FXML
    private void registrarse() {
        // Lógica para registrar al usuario
        System.out.println("Usuario registrado con éxito");
        // Aquí iría la lógica de base de datos
    }

    @FXML
    private void volverLogin() throws IOException {
        // Volver a la vista de login
        App.setRoot("Login");
    }
}