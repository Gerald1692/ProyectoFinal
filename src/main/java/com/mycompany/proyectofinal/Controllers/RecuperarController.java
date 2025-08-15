/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.UsuarioDAO;
import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.security.SecureRandom;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.mindrot.jbcrypt.BCrypt;

/**
 * FXML Controller class
 *
 * @author geral
 */
public class RecuperarController implements Initializable {

    @FXML
    private TextField txtCorreo;
    @FXML
    private Label lblNuevaContrasena; // Nombre corregido (camelCase)

    private final SecureRandom random = new SecureRandom();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }    
    
    @FXML
    private void volverLogin() throws IOException {
        // Volver a la vista de login
        App.setRoot("Login");
    }
    
    @FXML
    private void recuperarContrasena() {
        String correo = txtCorreo.getText().trim();
        
        if (correo.isEmpty()) {
            mostrarMensajeError("Por favor ingrese su correo electrónico");
            return;
        }
        
        try {
            // 1. Generar contraseña temporal
            String contrasenaTemporal = generarContrasenaTemporal(8);
            
            // 2. Encriptar contraseña
            String contrasenaEncriptada = BCrypt.hashpw(contrasenaTemporal, BCrypt.gensalt());
            
            // 3. Actualizar en BD
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.actualizarContrasena(correo, contrasenaEncriptada);
            
            // 4. Mostrar al usuario
            mostrarContrasenaTemporal("Contraseña temporal: " + contrasenaTemporal);
            
        } catch (SQLException e) {
            mostrarMensajeError("Error: " + e.getMessage());
        }
    }
    
    private String generarContrasenaTemporal(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(longitud);
        
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(indice));
        }
        return sb.toString();
    }
    
    private void mostrarMensajeError(String mensaje) {
        lblNuevaContrasena.setText(mensaje);
        lblNuevaContrasena.setTextFill(Color.RED);
    }
    
    private void mostrarContrasenaTemporal(String mensaje) {
        lblNuevaContrasena.setText(mensaje);
        lblNuevaContrasena.setTextFill(Color.WHITE);
    }
}