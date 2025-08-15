package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    
    // Usuarios válidos (simulados)
    private static final String[][] USUARIOS_VALIDOS = {
        {"admin", "admin123"},  // usuario, contraseña, rol
        {"cliente", "cliente123"}
    };
    
    @FXML
    private void iniciarSesion() throws IOException {
       App.setRoot("primary");
    }

    private String validarCredenciales(String usuario, String contrasena) {
        for (String[] credencial : USUARIOS_VALIDOS) {
            if (credencial[0].equals(usuario) && credencial[1].equals(contrasena)) {
                return credencial[2];
            }
        }
        return null;
    }

    private void redirigirSegunRol(int idRol) throws IOException {
        switch (idRol) {
            case 1: // Administrador
                App.setRoot("Administrador");
                break;
            case 4: // Cliente
                App.setRoot("primary");
                break;
            default:
                mostrarAlerta("Error", "Rol no reconocido: " + idRol);
                break;
        }
    }

    @FXML
    private void REGISTRARSE() throws IOException {
          App.setRoot("primary");
    }

    @FXML
    private void RECUPERARContra() throws IOException {
          App.setRoot("primary");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}