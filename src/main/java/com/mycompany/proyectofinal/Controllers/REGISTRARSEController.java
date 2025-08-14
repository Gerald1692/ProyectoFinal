package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.UsuarioDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class REGISTRARSEController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnRegistrar;
    @FXML private Button btnVolver;
    @FXML private ComboBox<String> cmbRoles;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo los roles que existen en la base de datos
        cmbRoles.getItems().addAll("Administrador", "Cliente");
        cmbRoles.setValue("Cliente"); // Valor inicial válido
    }    

    @FXML
    private void registrarse() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contrasena = txtContrasena.getText().trim();
        String rolSeleccionado = cmbRoles.getValue();

        // Validar campos
        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || 
            contrasena.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }
        
        if (contrasena.length() < 8) {
            mostrarAlerta("Error", "La contraseña debe tener al menos 8 caracteres");
            return;
        }
        
        try {
            // Crear nuevo usuario con el nombre del rol
            Usuario nuevoUsuario = new Usuario(
                0,
                nombre,
                contrasena,
                correo,
                telefono,
                rolSeleccionado
            );
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.insertarUsuario(nuevoUsuario);
            
            mostrarAlerta("Éxito", "Usuario registrado exitosamente!\n"
                + "Nombre: " + nombre + "\n"
                + "Rol: " + rolSeleccionado + "\n"
                + "ID asignado: " + nuevoUsuario.getIdUsuario());
            
            limpiarFormulario();
            
        } catch (SQLException e) {
            manejarErrorSQL(e);
        }
    }

    private void manejarErrorSQL(SQLException e) {
        String mensajeError = "Error de base de datos: ";
        
        if (e.getMessage().contains("ORA-00001")) {
            if (e.getMessage().contains("NOMBRE_USUARIO")) {
                mensajeError = "El nombre de usuario ya existe";
            } else if (e.getMessage().contains("CORREO")) {
                mensajeError = "El correo electrónico ya está registrado";
            } else {
                mensajeError = "El usuario ya existe (violación de restricción única)";
            }
        } else if (e.getMessage().contains("ORA-02291")) {
            mensajeError = "Error de integridad referencial: Rol no válido. "
                         + "Asegúrese que el rol exista en la base de datos";
        } else if (e.getMessage().contains("Rol no encontrado")) {
            mensajeError = e.getMessage();
        } else {
            mensajeError += e.getMessage();
        }
        
        mostrarAlerta("Error", mensajeError);
    }

    @FXML
    private void volverLogin() throws IOException {
        App.setRoot("Login");
    }
    
    private void limpiarFormulario() {
        txtNombre.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtContrasena.clear();
        cmbRoles.setValue("Cliente");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}