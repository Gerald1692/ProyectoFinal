package com.mycompany.proyectofinal.Controllers;


import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.Modelos.UsuarioModelo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;


public class Login_InicioController implements Initializable {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnLogin;
    @FXML private Button btnRegistro;
    @FXML private Button btnRecuperar;

    private final UsuarioModelo usuarioModelo = new UsuarioModelo();

    @FXML
private void iniciarSesion() {
    String correo = txtCorreo.getText().trim();
    String contrasena = txtContrasena.getText().trim();
    
    if (correo.isEmpty() || contrasena.isEmpty()) {
        mostrarError("Por favor complete todos los campos");
        return;
    }
    
    try {
        Usuario usuario = usuarioModelo.validarUsuario(correo, contrasena);
        if (usuario != null) {
            txtCorreo.clear();
            txtContrasena.clear();
            
            // Usar el método unificado para cambiar ventanas
            if (usuario.isEsAdmin()) {
                App.cambiarVentana("Administracion", "Panel de Administración");
            } else {
                App.cambiarVentana("Catalogo", "Catálogo de Obras");
            }
        } else {
            mostrarError("Credenciales incorrectas");
        }
    } catch (Exception e) {
        e.printStackTrace();
        mostrarError("Error: " + e.getMessage());
    }
}

    @FXML
    private void abrirRegistro() {
        try {
            // Limpiar campos antes de cambiar de ventana
            txtCorreo.clear();
            txtContrasena.clear();
            App.cambiarVentana("Registro", "Registro de Usuario");
        } catch (IOException e) {
            mostrarError("Error al cargar registro: " + e.getMessage());
        }
    }

    @FXML
    private void recuperarContrasena() {
        try {
            // Limpiar campos antes de cambiar de ventana
            txtCorreo.clear();
            txtContrasena.clear();
            App.cambiarVentana("Recuperacion", "Recuperar Contraseña");
        } catch (IOException e) {
            mostrarError("Error al cargar recuperación: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar acciones para la tecla Enter
        txtContrasena.setOnAction((var event) -> {
            iniciarSesion();
        });
        btnLogin.setDefaultButton(true);
    }
}