package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.UsuarioDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import com.mycompany.proyectofinal.MusicManager;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.shape.SVGPath;

public class LoginController {
    private boolean isMuted = false;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnVolume;
    @FXML private Slider volumeSlider;

    @FXML
    private void iniciarSesion() {
        String nombreUsuario = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText() == null ? "" : txtContrasena.getText().trim();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error", "Debe completar todos los campos.");
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.obtenerUsuarioPorNombre(nombreUsuario);
            
            if (usuario == null) {
                mostrarAlerta("Error", "Usuario no encontrado.");
                return;
            }
            
            // Mensajes de depuración
            System.out.println("Usuario obtenido: " + usuario.getNombreUsuario());
            System.out.println("Contraseña almacenada: " + usuario.getContrasena());
            System.out.println("Contraseña ingresada: " + contrasena);
            
            // Verificación de contraseña
            boolean contraseñaValida = usuario.verificarContrasena(contrasena);
            System.out.println("Resultado verificación: " + contraseñaValida);
            
            if (contraseñaValida) {
                redirigirSegunRol(usuario.getIdRol());
            } else {
                mostrarAlerta("Error", "Contraseña incorrecta.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error de base de datos: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar la vista: " + e.getMessage());
        }
    }

    private void redirigirSegunRol(int idRol) throws IOException {
        System.out.println("ID de rol recibido: " + idRol); // Log para diagnóstico
        
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
    
    @FXML
    private void toggleVolume() {
        isMuted = !isMuted;
        if (isMuted) {
            MusicManager.pauseMusic();
            // Cambiar a icono de mute
            SVGPath icon = (SVGPath) btnVolume.getGraphic();
            icon.setStyle("-fx-fill: #e74c3c;");
        } else {
            MusicManager.playBackgroundMusic();
            // Volver al color original
            SVGPath icon = (SVGPath) btnVolume.getGraphic();
            icon.setStyle("-fx-fill: #2c3e50;");
        }
    }
    
    @FXML
    private void initialize() {
        // Configurar volumen inicial
        volumeSlider.setValue(MusicManager.getVolume());
        
        // Listener para cambios en el slider
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue();
            MusicManager.setVolume(volume);
            if(volume == 0) {
                isMuted = true;
                SVGPath icon = (SVGPath) btnVolume.getGraphic();
                icon.setStyle("-fx-fill: #e74c3c;");
            } else {
                isMuted = false;
                SVGPath icon = (SVGPath) btnVolume.getGraphic();
                icon.setStyle("-fx-fill: #2c3e50;");
            }
        });
    }
}