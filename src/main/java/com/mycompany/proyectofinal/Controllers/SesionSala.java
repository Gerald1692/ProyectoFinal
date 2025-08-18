package com.mycompany.proyectofinal.Controllers;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class SesionSala {
    private static int idSala;
    private static int idObra;

    public static void setIdSala(int id) {
        idSala = id;
    }

    public static int getIdSala() {
        return idSala;
    }

    public static void setIdObra(int id) {
        idObra = id;
    }

    public static int getIdObra() {
        return idObra;
    }
}
