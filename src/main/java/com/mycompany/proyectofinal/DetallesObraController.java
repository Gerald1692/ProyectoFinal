package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.Obra;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;    
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class DetallesObraController {
    // Elementos de la vista
    @FXML private ImageView imgObra;
    @FXML private Label lblTitulo;
    @FXML private TextArea lblDescripcion;
    @FXML private Label lblArtista;
    @FXML private Label lblTecnica;
    @FXML private Label lblAnio;
    
    // Obra actual siendo mostrada
    private Obra obraActual;
    
    
    private void cargarImagenObra() {
        Image imagen = obraActual.getImagen();
        if (imagen != null) {
            imgObra.setImage(imagen);
        } else {
            cargarImagenPlaceholder();
        }
    }
    
    
    // Método para establecer la obra a mostrar
    public void setObra(Obra obra) {
        if (obra == null) {
            throw new IllegalArgumentException("La obra no puede ser nula");
        }
        this.obraActual = obra;
        mostrarDetalles();
    }
    
    
    private void reproducirAudio() {
    String audioPath = obraActual.getRutaAudio();
    Media media = new Media(new File(audioPath).toURI().toString());
    MediaPlayer player = new MediaPlayer(media);
    player.play();
}
    
    
    // Método para mostrar los detalles de la obra
    private void mostrarDetalles() {
        if (obraActual == null) return;
        
        // Asignar valores a todos los elementos de la vista
        lblTitulo.setText(obraActual.getTitulo());
        lblArtista.setText(obraActual.getArtista());
        lblTecnica.setText(obraActual.getTecnica());
        lblAnio.setText(String.valueOf(obraActual.getAnio()));
        lblDescripcion.setText(obraActual.getDescripcion());
        
        // Manejo de la imagen
        cargarImagenObra();
    }
    
    
    
    
    //se hace esto para evitar espacios vacios  ocupa un espacio en una interfaz cuando el contenido real no está disponible o aún no se ha cargado
    // Método para cargar imagen de placeholder
    private void cargarImagenPlaceholder() {
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
            imgObra.setImage(placeholder);
        } catch (Exception e) {
            System.err.println("Error cargando imagen placeholder: " + e.getMessage());
            imgObra.setVisible(false);
        }
    }
    
    // Método para volver al catálogo
    @FXML
    private void volverAlCatalogo() {
        try {
            App.cambiarVentana("Catalogo", "Catálogo de Obras");
        } catch (IOException e) {
            manejarErrorAlVolver(e);
        }
    }
    
    // Método para manejar errores al volver
    private void manejarErrorAlVolver(Exception e) {
        System.err.println("Error al volver al catálogo: " + e.getMessage());
        mostrarAlerta("Error", "No se pudo volver al catálogo");
    }

    // Método para mostrar alertas de error
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}