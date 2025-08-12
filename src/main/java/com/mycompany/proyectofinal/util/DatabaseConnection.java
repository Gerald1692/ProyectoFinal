/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author admar
 */

public class DatabaseConnection {
      private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";    
    private static final String  USUARIO = "Marce";
    private static final String  COMTRASENA = "M123";
    
    public static Connection connect() throws SQLException{
        return DriverManager.getConnection(URL,USUARIO,COMTRASENA);
    
    }
    
}
