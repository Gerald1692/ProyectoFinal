package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class AdministradorController implements Initializable {

    @FXML
    private BorderPane panelPrincipal;
    @FXML
    private StackPane areaContenido;
    @FXML
    private Label etiquetaTitulo;
    @FXML
    private Button BtnUsarios;
    @FXML
    private Button btnSalas;

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
private void manejarUsuarios() throws IOException {
    etiquetaTitulo.setText("Mantenimiento de Usuarios");
    areaContenido.getChildren().clear();
    
    // Usar ruta absoluta desde el classpath raíz
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/proyectofinal/AdmiUsuarios.fxml"));
    Parent root = loader.load();
    areaContenido.getChildren().add(root);
}

    @FXML
    private void manejarSala1() throws IOException {
        etiquetaTitulo.setText("Mantenimiento de Usuarios");
    areaContenido.getChildren().clear();
    
    // Usar ruta absoluta desde el classpath raíz
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/proyectofinal/AdmiObras.fxml"));
    Parent root = loader.load();
    areaContenido.getChildren().add(root);
    }

    private void manejarSala2() {
        cargarVistaSala(2);
    }

    private void manejarSala3() {
        cargarVistaSala(3);
    }

    @FXML
    private void manejarSalir() {
        System.exit(0);
    }

    @FXML
    private void volverLogin() throws IOException {
        App.setRoot("Login");
    }

    private AnchorPane crearContenidoUsuarios() {
        AnchorPane contenido = new AnchorPane();
        contenido.setPadding(new Insets(20));

        Label titulo = new Label("Mantenimiento de Usuarios");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        AnchorPane.setTopAnchor(titulo, 10.0);
        AnchorPane.setLeftAnchor(titulo, 20.0);

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

        Label placeholder = new Label("Contenido de administración de " + nombreSala + " aquí");
        placeholder.setFont(Font.font("Arial", 16));
        AnchorPane.setTopAnchor(placeholder, 50.0);
        AnchorPane.setLeftAnchor(placeholder, 20.0);

        contenido.getChildren().addAll(titulo, placeholder);
        return contenido;
    }

 
    private void cargarVistaSala(int numeroSala) {
        try {
            etiquetaTitulo.setText("Administración de Sala " + numeroSala);
            
            // Cargar el FXML específico para la sala
            String fxmlPath = "/com/mycompany/proyectofinal/Sala" + numeroSala + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node vistaSala = loader.load();
            
            // Obtener el controlador y configurarlo si es necesario
            SalaViewController controller = loader.getController();
            controller.setNumeroSala(numeroSala);
            
            areaContenido.getChildren().setAll(vistaSala);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar sala " + numeroSala);
        }
    }

}
