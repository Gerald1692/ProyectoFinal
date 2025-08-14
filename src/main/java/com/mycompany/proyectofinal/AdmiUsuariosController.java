package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.UsuarioDAO;
import com.mycompany.proyectofinal.ModelosPOJOs.Usuario;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdmiUsuariosController implements Initializable {

    @FXML
    private TableView<Usuario> TUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> ColIdUsuario;
    @FXML
    private TableColumn<Usuario, String> ColNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> ColCorreo;
    @FXML
    private TableColumn<Usuario, String> ColTelefono;
   
    @FXML
    private TextArea txtidUsuario;
    @FXML
    private TextArea txtNombre;
    @FXML
    private TextArea txtTelefono;
    @FXML
    private TextArea txtCorreo;

    @FXML
    private Button btnCrear;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnMostrar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;

    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> usuariosList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAO();
        usuariosList = FXCollections.observableArrayList();
        
        configurarTabla();
        cargarTodosUsuarios();
        configurarEventos();
    }
    
    private void configurarTabla() {
        // CORRECCIÓN: Configurar correctamente las columnas
        ColIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        ColNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        ColCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        ColTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        TUsuarios.setItems(usuariosList);
        
        // Listener para selección de filas
        TUsuarios.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    llenarCampos(newSelection);
                }
            }
        );
    }
    
    private void configurarEventos() {
        btnCrear.setOnAction(e -> crearUsuario());
        btnActualizar.setOnAction(e -> actualizarUsuario());
        btnEliminar.setOnAction(e -> eliminarUsuario());
        btnMostrar.setOnAction(e -> buscarUsuario());
        btnLimpiar.setOnAction(e -> limpiarCampos());
    }
    
    private void cargarTodosUsuarios() {
        try {
            usuariosList.setAll(usuarioDAO.obtenerTodosUsuarios());
            TUsuarios.refresh(); // CORRECCIÓN: Refrescar la tabla después de cargar datos
        } catch (SQLException ex) {
            mostrarAlerta("Error al cargar usuarios", "Error: " + ex.getMessage(), AlertType.ERROR);
            ex.printStackTrace(); // CORRECCIÓN: Imprimir stack trace para depuración
        }
    }
    
    private void crearUsuario() {
        if (!validarCamposCreacion()) {
            mostrarAlerta("Campos incompletos", "Complete todos los campos requeridos", AlertType.WARNING);
            return;
        }
        
        try {
            // CORRECCIÓN: Usar constructor vacío y setters
            Usuario nuevo = new Usuario();
            nuevo.setNombreUsuario(txtNombre.getText());
            nuevo.setCorreo(txtCorreo.getText());
            nuevo.setTelefono(txtTelefono.getText());
            
            // Generar contraseña temporal
            String tempPassword = generarContrasenaTemporal();
            nuevo.setContrasenaPlana(tempPassword);
            nuevo.setIdRol(2); // Rol por defecto
            
            usuarioDAO.insertarUsuario(nuevo);
            
            // Mostrar contraseña temporal
            mostrarAlerta("Éxito", "Usuario creado con ID: " + nuevo.getIdUsuario() 
                + "\nContraseña temporal: " + tempPassword, AlertType.INFORMATION);
            
            cargarTodosUsuarios();
            limpiarCampos();
        } catch (SQLException ex) {
            mostrarAlerta("Error al crear usuario", "Error: " + ex.getMessage(), AlertType.ERROR);
            ex.printStackTrace();
        }
    }
    
    private String generarContrasenaTemporal() {
        // Generar contraseña aleatoria de 8 caracteres
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int)(Math.random() * caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        return sb.toString();
    }
    
    private void actualizarUsuario() {
        Usuario seleccionado = TUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un usuario de la tabla", AlertType.WARNING);
            return;
        }
        
        if (!validarCamposActualizacion()) {
            mostrarAlerta("Campos incompletos", "Complete todos los campos requeridos", AlertType.WARNING);
            return;
        }
        
        try {
            // Actualizar datos
            seleccionado.setNombreUsuario(txtNombre.getText());
            seleccionado.setCorreo(txtCorreo.getText());
            seleccionado.setTelefono(txtTelefono.getText());
            
            usuarioDAO.actualizarUsuario(seleccionado);
            mostrarAlerta("Éxito", "Usuario actualizado correctamente", AlertType.INFORMATION);
            cargarTodosUsuarios();
        } catch (SQLException ex) {
            mostrarAlerta("Error al actualizar", "Error: " + ex.getMessage(), AlertType.ERROR);
            ex.printStackTrace();
        }
    }
    
    private void eliminarUsuario() {
        Usuario seleccionado = TUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un usuario de la tabla", AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Eliminar usuario " + seleccionado.getNombreUsuario() + "?");
        alert.setContentText("Esta acción no se puede deshacer");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                usuarioDAO.eliminarUsuario(seleccionado.getIdUsuario());
                mostrarAlerta("Éxito", "Usuario eliminado correctamente", AlertType.INFORMATION);
                cargarTodosUsuarios();
                limpiarCampos();
            } catch (SQLException ex) {
                mostrarAlerta("Error al eliminar", "Error: " + ex.getMessage(), AlertType.ERROR);
                ex.printStackTrace();
            }
        }
    }
    
    private void buscarUsuario() {
        String idText = txtidUsuario.getText().trim();
        if (idText.isEmpty()) {
            mostrarAlerta("Campo requerido", "Ingrese un ID de usuario", AlertType.WARNING);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            Usuario encontrado = usuarioDAO.obtenerUsuarioPorId(id);
            
            if (encontrado != null) {
                usuariosList.clear();
                usuariosList.add(encontrado);
                llenarCampos(encontrado);
                TUsuarios.getSelectionModel().select(encontrado);
            } else {
                mostrarAlerta("No encontrado", "Usuario con ID " + id + " no encontrado", AlertType.INFORMATION);
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("ID inválido", "El ID debe ser un número entero", AlertType.ERROR);
            ex.printStackTrace();
        } catch (SQLException ex) {
            mostrarAlerta("Error de búsqueda", "Error: " + ex.getMessage(), AlertType.ERROR);
            ex.printStackTrace();
        }
    }
    
    private void llenarCampos(Usuario usuario) {
        txtidUsuario.setText(String.valueOf(usuario.getIdUsuario()));
        txtNombre.setText(usuario.getNombreUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getTelefono());
    }
    
    private void limpiarCampos() {
        txtidUsuario.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        TUsuarios.getSelectionModel().clearSelection();
        cargarTodosUsuarios();
    }
    
    private boolean validarCamposCreacion() {
        return !txtNombre.getText().isEmpty() && 
               !txtCorreo.getText().isEmpty() && 
               !txtTelefono.getText().isEmpty();
    }
    
    private boolean validarCamposActualizacion() {
        return !txtNombre.getText().isEmpty() && 
               !txtCorreo.getText().isEmpty() && 
               !txtTelefono.getText().isEmpty();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}