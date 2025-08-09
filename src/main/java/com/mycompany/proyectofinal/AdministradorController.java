package com.mycompany.proyectofinal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdministradorController implements Initializable {

    @FXML
    private BorderPane panelPrincipal;
    @FXML
    private StackPane areaContenido;
    @FXML
    private Label etiquetaTitulo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarAreaContenido();
    }
    
    private void configurarAreaContenido() {
        // Pantalla de bienvenida inicial
        VBox pantallaBienvenida = new VBox(20);
        pantallaBienvenida.setAlignment(Pos.CENTER);
        pantallaBienvenida.setPadding(new Insets(20));
        
        ImageView imagenAdmin = new ImageView(new Image(getClass().getResourceAsStream("/RECURSOS/afuera.png")));
        imagenAdmin.setFitWidth(300);
        imagenAdmin.setFitHeight(200);
        
        Label etiquetaBienvenida = new Label("Bienvenido al Panel de Administración");
        etiquetaBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        Label etiquetaInstruccion = new Label("Seleccione una opción del menú lateral para comenzar");
        etiquetaInstruccion.setFont(Font.font("Arial", 16));
        
        pantallaBienvenida.getChildren().addAll(imagenAdmin, etiquetaBienvenida, etiquetaInstruccion);
        areaContenido.getChildren().add(pantallaBienvenida);
    }
    
    @FXML
    private void manejarUsuarios() {
        etiquetaTitulo.setText("Mantenimiento de Usuarios");
        areaContenido.getChildren().clear();
        areaContenido.getChildren().add(crearContenidoUsuarios());
    }
    
    @FXML
    private void manejarSala1() {
        etiquetaTitulo.setText("Administración de Sala 1");
        areaContenido.getChildren().clear();
        areaContenido.getChildren().add(crearContenidoSala("Sala 1"));
    }
    
    @FXML
    private void manejarSala2() {
        etiquetaTitulo.setText("Administración de Sala 2");
        areaContenido.getChildren().clear();
        areaContenido.getChildren().add(crearContenidoSala("Sala 2"));
    }
    
    @FXML
    private void manejarSala3() {
        etiquetaTitulo.setText("Administración de Sala 3");
        areaContenido.getChildren().clear();
        areaContenido.getChildren().add(crearContenidoSala("Sala 3"));
    }
    
    @FXML
    private void manejarReporteria() {
        etiquetaTitulo.setText("Reportería y Estadísticas");
        areaContenido.getChildren().clear();
        areaContenido.getChildren().add(crearContenidoReporteria());
    }
    
    @FXML
    private void manejarSalir() {
        System.exit(0);
    }
    @FXML
    private void volverLogin() throws IOException {
        // Volver a la vista de login
        App.setRoot("Login");
    }
    private AnchorPane crearContenidoUsuarios() {
        AnchorPane contenido = new AnchorPane();
        contenido.setPadding(new Insets(20));
        
        Label titulo = new Label("Mantenimiento de Usuarios");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        AnchorPane.setTopAnchor(titulo, 10.0);
        AnchorPane.setLeftAnchor(titulo, 20.0);
        
        // Aquí iría el contenido real de mantenimiento de usuarios
        Label placeholder = new Label("Contenido de mantenimiento de usuarios aquí");
        placeholder.setFont(Font.font("Arial", 16));
        AnchorPane.setTopAnchor(placeholder, 50.0);
        AnchorPane.setLeftAnchor(placeholder, 20.0);
        
        contenido.getChildren().addAll(titulo, placeholder);
        return contenido;
    }
    
    private AnchorPane crearContenidoSala(String nombreSala) {
        AnchorPane contenido = new AnchorPane();
        contenido.setPadding(new Insets(20));
        
        Label titulo = new Label("Administración de " + nombreSala);
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        AnchorPane.setTopAnchor(titulo, 10.0);
        AnchorPane.setLeftAnchor(titulo, 20.0);
        
        // Aquí iría el contenido real de administración de sala
        Label placeholder = new Label("Contenido de administración de " + nombreSala + " aquí");
        placeholder.setFont(Font.font("Arial", 16));
        AnchorPane.setTopAnchor(placeholder, 50.0);
        AnchorPane.setLeftAnchor(placeholder, 20.0);
        
        contenido.getChildren().addAll(titulo, placeholder);
        return contenido;
    }
    
    private AnchorPane crearContenidoReporteria() {
        AnchorPane contenido = new AnchorPane();
        contenido.setPadding(new Insets(20));
        
        Label titulo = new Label("Reportería y Estadísticas");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        AnchorPane.setTopAnchor(titulo, 10.0);
        AnchorPane.setLeftAnchor(titulo, 20.0);
        
        // Aquí iría el contenido real de reportería
        Label placeholder = new Label("Contenido de reportería aquí");
        placeholder.setFont(Font.font("Arial", 16));
        AnchorPane.setTopAnchor(placeholder, 50.0);
        AnchorPane.setLeftAnchor(placeholder, 20.0);
        
        contenido.getChildren().addAll(titulo, placeholder);
        return contenido;
    }
}