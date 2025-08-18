module com.mycompany.proyectofinal {
    // Módulos de JavaFX
    requires javafx.controls;
    requires javafx.fxml;
<<<<<<< Updated upstream
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires java.base;
   requires javafx.media;
   requires com.oracle.database.jdbc;
   requires java.naming;

    // Abre tus paquetes a javafx.fxml para reflexión
    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.Controllers to javafx.fxml;
    opens com.mycompany.proyectofinal.ModelosPOJOs to javafx.base, javafx.fxml;

    // Exporta tus paquetes
    exports com.mycompany.proyectofinal;
    exports com.mycompany.proyectofinal.Controllers;
    requires javafx.mediaEmpty;
}
=======
    
    
    requires java.sql;      
    requires jbcrypt;       
    
    // Abre tu paquete principal a javafx.fxml
    opens com.mycompany.proyectofinal to javafx.fxml;
    
    // Exporta tu paquete principal
    exports com.mycompany.proyectofinal;
}
>>>>>>> Stashed changes
