package com.mycompany.proyectofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Stage primaryStage;
    private static String currentView="";
    private static String dinosaurioSeleccionado;
    

    
    public static void setRoot(String fxml, String dinosaurio) throws IOException {
        dinosaurioSeleccionado = dinosaurio;
        setRoot(fxml);
    }
    
    public static String getDinosaurioSeleccionado() {
        return dinosaurioSeleccionado;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("Login"), 800, 600); // Tamaño acorde al diseño
        stage.setScene(scene);
        stage.setTitle("Curiosópolis");
        stage.show();
          MusicManager.playBackgroundMusic();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        currentView = fxml;
        scene.setRoot(loadFXML(fxml));
        
        // Solo reanudar la música si está pausada
        MusicManager.playBackgroundMusic();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
    
    
}