package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtCorreo;
        @FXML private PasswordField txtContrasena;
    
            
            
    @FXML
    private void iniciarSesion() throws IOException {
         String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String clave  = txtContrasena.getText() == null ? "" : txtContrasena.getText().trim();

        if (correo.isEmpty() && clave.isEmpty()) {
            // ambos vacíos → vista "primary"
            App.setRoot("primary");
        } else if (!correo.isEmpty() && !clave.isEmpty()) {
            // ambos con texto → vista "Administrador"
            App.setRoot("Administrador");
        } else {
            // caso: sólo uno está lleno -> comportamiento por defecto
            // opción A: enviar a 'primary'
            App.setRoot("primary");

            // opción B (mejor UX): mostrar alerta pidiendo completar ambos campos
            // Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor completa correo y contraseña.");
            // alert.showAndWait();
        }
        
    }

    @FXML
    private void REGISTRARSE() throws IOException {
        App.setRoot("REGISTRARSE");
    }

    // AÑADE ESTE MÉTODO QUE FALTA
    @FXML
    private void RECUPERARContra() throws IOException {
       App.setRoot("Recuperar");
    }
}