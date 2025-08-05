package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.Obra;
import com.mycompany.proyectofinal.Modelos.ObraModelo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class VisitantesController implements Initializable {

    @FXML private TextField txtBusqueda;
    @FXML private ComboBox<String> cbTipoBusqueda;
    @FXML private ComboBox<String> cbSalas;
    @FXML private ListView<Obra> listObras;
    @FXML private Label lblTituloSeccion;
    
    private final ObservableList<Obra> todasLasObras = FXCollections.observableArrayList();
    private final ObraModelo obraModelo = new ObraModelo();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar ComboBox
        cbTipoBusqueda.getItems().addAll("Todas", "Por título", "Por descripción");
        cbTipoBusqueda.getSelectionModel().selectFirst();
        
        cbSalas.getItems().addAll("Todas", "Sala de Historia", "Sala de Fósiles", "Sala de Minerales", "Sala de Biología");
        cbSalas.getSelectionModel().selectFirst();
        
        // Cargar obras
        cargarObras();
        
        // Configurar lista
        configurarLista();
        
        lblTituloSeccion.setText("OBRAS DISPONIBLES");
    }
    
    private void cargarObras() {
        todasLasObras.clear();
        try {
            todasLasObras.addAll(obraModelo.obtenerTodasObras());
        } catch (Exception e) {
            System.out.println("Error al cargar obras: " + e.getMessage());
            cargarDatosEjemplo();
        }
    }
    
    private void cargarDatosEjemplo() {
        todasLasObras.add(new Obra("Tiranosaurio Rex", "Gran dinosaurio carnívoro", "Sala de Fósiles"));
        todasLasObras.add(new Obra("Meteorito Allende", "Meteorito famoso encontrado en México", "Sala de Minerales"));
        todasLasObras.add(new Obra("Códices Aztecas", "Documentos históricos antiguos", "Sala de Historia"));
        todasLasObras.add(new Obra("Ecosistema Amazónico", "Diversidad de la selva amazónica", "Sala de Biología"));
    }
    
    private void configurarLista() {
        listObras.setItems(todasLasObras);
        listObras.setCellFactory(param -> new javafx.scene.control.ListCell<Obra>() {
            @Override
            protected void updateItem(Obra obra, boolean empty) {
                super.updateItem(obra, empty);
                if (empty || obra == null) {
                    setText(null);
                } else {
                    setText(obra.getTitulo() + " - " + obra.getSala());
                }
            }
        });
    }
    
    @FXML
    private void buscarObras(ActionEvent event) {
        String textoBusqueda = txtBusqueda.getText().toLowerCase();
        String tipoBusqueda = cbTipoBusqueda.getValue();
        String salaSeleccionada = cbSalas.getValue();
        
        ObservableList<Obra> resultados = FXCollections.observableArrayList();
        
        for (Obra obra : todasLasObras) {
            // Filtrar por sala
            if (!salaSeleccionada.equals("Todas") && !obra.getSala().equals(salaSeleccionada)) {
                continue;
            }
            
            // Filtrar por texto
            if (!textoBusqueda.isEmpty()) {
                boolean coincide = false;
                
                switch (tipoBusqueda) {
                    case "Por título":
                        coincide = obra.getTitulo().toLowerCase().contains(textoBusqueda);
                        break;
                    case "Por descripción":
                        coincide = obra.getDescripcion().toLowerCase().contains(textoBusqueda);
                        break;
                    default: // "Todas"
                        coincide = obra.getTitulo().toLowerCase().contains(textoBusqueda) ||
                                   obra.getDescripcion().toLowerCase().contains(textoBusqueda);
                }
                
                if (!coincide) continue;
            }
            
            resultados.add(obra);
        }
        
        listObras.setItems(resultados);
        lblTituloSeccion.setText(resultados.isEmpty() ? "NO HAY RESULTADOS" : "RESULTADOS DE BÚSQUEDA");
    }

    // Métodos para cambiar entre salas
    @FXML private void cambiarASalaHistoria(ActionEvent event) {
        filtrarPorSala("Sala de Historia");
        lblTituloSeccion.setText("SALA DE HISTORIA");
    }

    @FXML private void cambiarASalaFosiles(ActionEvent event) {
        filtrarPorSala("Sala de Fósiles");
        lblTituloSeccion.setText("SALA DE FÓSILES");
    }

    @FXML private void cambiarASalaMinerales(ActionEvent event) {
        filtrarPorSala("Sala de Minerales");
        lblTituloSeccion.setText("SALA DE MINERALES");
    }

    @FXML private void cambiarASalaBiologia(ActionEvent event) {
        filtrarPorSala("Sala de Biología");
        lblTituloSeccion.setText("SALA DE BIOLOGÍA");
    }
    
    private void filtrarPorSala(String sala) {
        ObservableList<Obra> obrasSala = FXCollections.observableArrayList();
        
        for (Obra obra : todasLasObras) {
            if (obra.getSala().equals(sala)) {
                obrasSala.add(obra);
            }
        }
        
        listObras.setItems(obrasSala);
        cbSalas.getSelectionModel().select(sala);
    }

    // Métodos para otras secciones
    @FXML private void mostrarTodas(ActionEvent event) {
        listObras.setItems(todasLasObras);
        cbSalas.getSelectionModel().select("Todas");
        lblTituloSeccion.setText("TODAS LAS OBRAS");
    }

    @FXML private void mostrarExposicionActual(ActionEvent event) {
        listObras.setItems(todasLasObras);
        lblTituloSeccion.setText("EXPOSICIÓN ACTUAL");
    }

    @FXML private void mostrarExposicionesPasadas(ActionEvent event) {
        listObras.getItems().clear();
        lblTituloSeccion.setText("EXPOSICIONES PASADAS");
    }
}