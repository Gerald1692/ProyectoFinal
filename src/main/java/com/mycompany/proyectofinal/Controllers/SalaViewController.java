package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.SalaDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Sala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import javafx.scene.control.Label;

public class SalaViewController {

    @FXML protected Label tituloSala;
    @FXML protected TextField txtNombre;
    @FXML protected TextField txtTematica;
    @FXML protected TextField txtNumeroPuerta;
    @FXML protected TableView<Sala> tablaSala;
    @FXML protected TableColumn<Sala, Integer> colId;
    @FXML protected TableColumn<Sala, String> colNombre;
    @FXML protected TableColumn<Sala, String> colTematica;
    @FXML protected TableColumn<Sala, Integer> colNumeroPuerta;

    protected int numeroSala;
    private Sala salaSeleccionada;
    private final SalaDAO salaDAO = new SalaDAO();
    private ObservableList<Sala> data = FXCollections.observableArrayList();

    public void setNumeroSala(int numeroSala) {
        this.numeroSala = numeroSala;
        tituloSala.setText("Administración de Sala " + numeroSala);
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idSala"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
        colTematica.setCellValueFactory(new PropertyValueFactory<>("tematica"));
        colNumeroPuerta.setCellValueFactory(new PropertyValueFactory<>("numeroPuerta"));
        
        tablaSala.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                salaSeleccionada = newSelection;
                if (newSelection != null) {
                    llenarFormulario(newSelection);
                }
            }
        );
    }

    private void cargarDatos() {
        try {
            data = FXCollections.observableArrayList(salaDAO.listarSalas());
            tablaSala.setItems(data);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    private void llenarFormulario(Sala sala) {
        txtNombre.setText(sala.getNombreSala());
        txtTematica.setText(sala.getTematica());
        txtNumeroPuerta.setText(String.valueOf(sala.getNumeroPuerta()));
    }

    @FXML
    protected void nuevoRegistro() {
        salaSeleccionada = null;
        limpiarFormulario();
    }

    @FXML
    protected void guardarRegistro() {
        try {
            Sala sala;
            if (salaSeleccionada == null) {
                sala = new Sala(
                    0,
                    txtNombre.getText(),
                    txtTematica.getText(),
                    Integer.parseInt(txtNumeroPuerta.getText())
                );
                salaDAO.insertarSala(sala);
            } else {
                sala = salaSeleccionada;
                sala.setNombreSala(txtNombre.getText());
                sala.setTematica(txtTematica.getText());
                sala.setNumeroPuerta(Integer.parseInt(txtNumeroPuerta.getText()));
                // Actualizar en BD (necesitarás implementar actualizarSala en SalaDAO)
            }
            cargarDatos();
            limpiarFormulario();
            mostrarAlerta("Éxito", "Operación realizada correctamente");
        } catch (SQLException | NumberFormatException e) {
            mostrarAlerta("Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    protected void eliminarRegistro() {
        if (salaSeleccionada != null) {
            // Implementar eliminarSala en SalaDAO
            // salaDAO.eliminarSala(salaSeleccionada.getIdSala());
            cargarDatos();
            limpiarFormulario();
            mostrarAlerta("Éxito", "Sala eliminada correctamente");
        } else {
            mostrarAlerta("Advertencia", "Seleccione una sala primero");
        }
    }

    @FXML
    protected void actualizarRegistro() {
        guardarRegistro();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtTematica.clear();
        txtNumeroPuerta.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}