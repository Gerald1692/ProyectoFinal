package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.Modelos.UsuarioModelo; // Elimina import de Encriptacion
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class RegistroController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtConfirmarContrasena;
    
    private final UsuarioModelo usuarioModelo = new UsuarioModelo();

    @FXML
    private void registrarUsuario() {
        if (!txtContrasena.getText().equals(txtConfirmarContrasena.getText())) {
            mostrarAlerta("Error", "Las contraseñas no coinciden");
            return;
        }
        
        if (txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty() || 
            txtContrasena.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }
        
        // Elimina la encriptación: usa la contraseña directamente
        Usuario nuevoUsuario = new Usuario(
            txtNombre.getText(),
            txtCorreo.getText(),
            txtContrasena.getText(), // Contraseña sin encriptar
            false
        );
        
        if(usuarioModelo.crearUsuario(nuevoUsuario)) {
            mostrarAlerta("Éxito", "Usuario registrado correctamente");
            try {
                App.cambiarVentana("Login", "Inicio de Sesión");
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo redirigir: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Error", "No se pudo registrar el usuario");
        }
    }
    
    @FXML
    private void volverAlLogin() {
        try {
            App.cambiarVentana("Login", "Inicio de Sesión");
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver al login: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(
            titulo.equals("Éxito") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR
        );
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}