module com.mycompany.proyectofinal {
    // Módulos de JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    
    
    requires java.sql;      
    requires jbcrypt;       
    
    // Abre tu paquete principal a javafx.fxml
    opens com.mycompany.proyectofinal to javafx.fxml;
    
    // Exporta tu paquete principal
    exports com.mycompany.proyectofinal;
}