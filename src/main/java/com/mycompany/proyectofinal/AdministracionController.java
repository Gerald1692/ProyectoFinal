package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AdministracionController {
    @FXML private TabPane tabPane;
    @FXML private Tab tabUsuarios;
    @FXML private Tab tabObras;
    @FXML private Tab tabReportes;

    @FXML
    private void salir() {
        try {
            App.cambiarVentana("Login", "Inicio de Sesión");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
private void cambiarTabUsuarios() {
    tabPane.getSelectionModel().select(tabUsuarios);
    // Cargar tabla de usuarios desde BD
}

@FXML
private void cambiarTabObras() {
    tabPane.getSelectionModel().select(tabObras);
    // Cargar tabla de obras desde BD
}
}