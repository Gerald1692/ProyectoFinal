package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Modelos.UsuarioModelo; // Elimina import de Encriptacion
import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RecuperacionController {
    @FXML
    private TextField txtCorreo;
    private final UsuarioModelo usuarioModelo = new UsuarioModelo();
    
    @FXML
    private void recuperarContrasena() {
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty() || !validarCorreo(correo)) {
            mostrarAlerta("Error", "Ingrese un correo válido");
            return;
        }
        
        if (!usuarioModelo.existeUsuario(correo)) {
            mostrarAlerta("Error", "Correo no registrado");
            return;
        }
        
        String nuevaContrasena = generarContrasenaAleatoria();
        // Elimina la encriptación: usa la contraseña directamente
        if (usuarioModelo.actualizarContrasena(correo, nuevaContrasena)) {
            enviarCorreoRecuperacion(correo, nuevaContrasena);
            mostrarAlerta("Éxito", "Se ha enviado una nueva contraseña a su correo");
            volverAlLogin();
        } else {
            mostrarAlerta("Error", "Error al recuperar contraseña");
        }
    }
    
    private void enviarCorreoRecuperacion(String correo, String nuevaContrasena) {
        System.out.println("Simulación: Nueva contraseña para " + correo + ": " + nuevaContrasena);
    }
    
    private boolean validarCorreo(String correo) {
        return correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
    
    private String generarContrasenaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return sb.toString();
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