package com.mycompany.proyectofinal;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;

    @FXML
    private void iniciarSesion() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void REGISTRARSE() throws IOException {
        App.setRoot("REGISTRARSE");
    }

    // AÑADE ESTE MÉTODO QUE FALTA
    @FXML
    private void recuperarContrasena() throws IOException {
        System.out.println("Recuperar contraseña");
        // Aquí puedes agregar la lógica para recuperar contraseña
        // Por ejemplo: App.setRoot("RecuperarContrasena");
    }
}