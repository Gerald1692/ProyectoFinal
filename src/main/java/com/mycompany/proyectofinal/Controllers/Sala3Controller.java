/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectofinal.Controllers;

import com.mycompany.proyectofinal.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author geral
 */
public class Sala3Controller implements Initializable {

       @FXML
    private void irSalaPrincipal() throws IOException {
        App.setRoot("SalaSimple3");
    }
     @FXML
    private void volverLogin() throws IOException {
        App.setRoot("Login");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
