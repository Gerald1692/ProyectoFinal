package com.mycompany.proyectofinal;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void mostrarExposiciones() throws IOException {
        // Lógica para mostrar exposiciones
        System.out.println("Mostrar exposiciones");
    }

    @FXML
    private void mostrarVisitas() throws IOException {
        // Lógica para mostrar visitas guiadas
        System.out.println("Mostrar visitas guiadas");
    }

    @FXML
    private void mostrarEntradas() throws IOException {
        // Lógica para mostrar entradas
        System.out.println("Mostrar entradas");
    }

    @FXML
    private void mostrarEventos() throws IOException {
        // Lógica para mostrar eventos
        System.out.println("Mostrar eventos");
    }

    @FXML
    private void abrirPerfil() throws IOException {
        // Lógica para abrir perfil
        System.out.println("Abrir perfil");
    }

    @FXML
    private void mostrarAyuda() throws IOException {
        // Lógica para mostrar ayuda
        System.out.println("Mostrar ayuda");
    }

    @FXML
    private void cerrarSesion() throws IOException {
        App.setRoot("Login"); // Volver a la vista de login
    }
    
    @FXML
    private void manejarSalir() {
        System.exit(0);
    }
    
    
    // Nuevos métodos para navegar a salas específicas
    @FXML
    private void irSala1() throws IOException {
        App.setRoot("Sala1");
    }
    
    @FXML
    private void irSala2() throws IOException {
        // Implementar lógica para Sala 2
        System.out.println("Navegando a Sala 2");
    }
    
    @FXML
    private void irSala3() throws IOException {
        // Implementar lógica para Sala 3
        System.out.println("Navegando a Sala 3");
    }
}