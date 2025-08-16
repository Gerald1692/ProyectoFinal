package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.UsuarioDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    
    @FXML
    private void iniciarSesion() throws IOException {
       App.setRoot("primary");
    }

    private void redirigirSegunRol(int idRol) throws IOException {
        System.out.println("ID de rol recibido: " + idRol); // Log para diagnóstico
        
        switch (idRol) {
            case 1: // Administrador
                App.setRoot("Administrador");
                break;
            case 4: // Cliente - ¡CAMBIO CLAVE AQUÍ! (de 2 a 4)
                App.setRoot("primary");
                break;
            default:
                mostrarAlerta("Error", "Rol no reconocido: " + idRol);
                break;
        }
    }

    @FXML
    private void REGISTRARSE() throws IOException {
        App.setRoot("REGISTRARSE");
    }

    @FXML
    private void RECUPERARContra() throws IOException {
        App.setRoot("Recuperar");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}