module com.mycompany.proyectofinal {
    // Módulos requeridos
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    
<<<<<<< HEAD
    // Abre tus paquetes a javafx.fxml para reflexión
=======
    
    requires java.sql;      
    requires jbcrypt;       
    requires java.base;
    requires java.desktop;
    
    // Abre tu paquete principal a javafx.fxml
>>>>>>> 451e650764d99c262e0a853c99de7dbe69604594
    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.Controllers to javafx.fxml;
    
    // Exporta tus paquetes
    exports com.mycompany.proyectofinal;
    exports com.mycompany.proyectofinal.Controllers;
}