package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.UsuarioDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import com.mycompany.proyectofinal.MusicManager; // 👈 tu clase que maneja la música
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

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;

    // 🔊 Controles de audio
    @FXML private Button btnVolume;
    @FXML private Slider volumeSlider;

    private boolean isMuted = false;

    // ================= LOGIN =================
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

            // Logs de depuración
            System.out.println("Usuario obtenido: " + usuario.getNombreUsuario());
            System.out.println("Contraseña almacenada: " + usuario.getContrasena());
            System.out.println("Contraseña ingresada: " + contrasena);

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

    // ================= VOLUMEN =================
    @FXML
    private void toggleVolume() {
        isMuted = !isMuted;
        SVGPath icon = (SVGPath) btnVolume.getGraphic();

        if (isMuted) {
            MusicManager.pauseMusic();
            icon.setStyle("-fx-fill: #e74c3c;"); // Rojo mute
        } else {
            MusicManager.playBackgroundMusic();
            icon.setStyle("-fx-fill: #2c3e50;"); // Azul/gris normal
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

            SVGPath icon = (SVGPath) btnVolume.getGraphic();
            if (volume == 0) {
                isMuted = true;
                icon.setStyle("-fx-fill: #e74c3c;");
            } else {
                isMuted = false;
                icon.setStyle("-fx-fill: #2c3e50;");
            }
        });
    }
}
