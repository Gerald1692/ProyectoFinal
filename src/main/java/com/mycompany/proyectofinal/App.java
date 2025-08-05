package com.mycompany.proyectofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App extends Application {
    private static Scene scene; 
    public static Stage primaryStage; // Referencia estática al Stage principal

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Guardar referencia al stage principal
        scene = new Scene(loadFXML("Login"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Sistema de Museo");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Método para cambiar ventanas con título
    public static void cambiarVentana(String fxml, String title) throws IOException {
        primaryStage.setTitle(title);
        setRoot(fxml);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    
    
    
 
    
    public static void main(String[] args) {
        launch();
    }
    
    
    
}