package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.AccesoDatos.DAOs.SalaDAO;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.App;
import com.mycompany.proyectofinal.ModelosPOJOs.Sala;
import com.mycompany.proyectofinal.Controllers.SesionSala;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;

public class PrimaryController {
private SalaDAO salaDAO = new SalaDAO();

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
    
    
   
@FXML
private void irSalaSimple1() throws IOException {
    SesionSala.setIdSala(1); // Sala Dinosaurios
    System.out.println("se paso el id"+ SesionSala.getIdSala());
    App.setRoot("SalaSimple1");
}

@FXML
private void irSalaSimple2() throws IOException {
    SesionSala.setIdSala(4); // Sala Renacentista
    System.out.println("se paso el id"+ SesionSala.getIdSala());
    App.setRoot("SalaSimple1");
}

@FXML
private void irSalaSimple3() throws IOException {
    SesionSala.setIdSala(5); // Sala Egipto
    System.out.println("se paso el id"+ SesionSala.getIdSala());
    App.setRoot("SalaSimple1");
}


 public void onSalaSeleccionada(int idSala) {
        // IMPORTANTE: las llamadas a BD no deben bloquear el hilo UI; ejecuta en background (Task)
        try {
            Sala sala = salaDAO.obtenerSalaPorId(idSala);
            if (sala != null) {
                // hacer lo que necesites con la sala: mostrar en UI, guardar referencia, etc.
                System.out.println("Sala cargada: " + sala.getNombreSala());
                // ejemplo: actualizar campos de la vista
                // nombreLabel.setText(sala.getNombreSala());
            } else {
                System.out.println("No existe sala con id=" + idSala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
 }
           

}
        
