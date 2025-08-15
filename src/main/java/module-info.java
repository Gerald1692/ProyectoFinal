module com.mycompany.proyectofinal {
<<<<<<< HEAD
=======
    // Módulos requeridos
>>>>>>> 4fccdc03951130e937e70215c1711b1345e56b72
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    
<<<<<<< HEAD
    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.ModelosPOJOs to javafx.base, javafx.fxml;
    
=======
<<<<<<< HEAD
    // Abre tus paquetes a javafx.fxml para reflexión
=======
    
    requires java.sql;      
    requires jbcrypt;       
    requires java.base;
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    requires java.desktop;
    requires java.base;

    // Abre tus paquetes a javafx.fxml para reflexión
    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.Controllers to javafx.fxml;
    
    // Exporta tus paquetes
>>>>>>> 4fccdc03951130e937e70215c1711b1345e56b72
    exports com.mycompany.proyectofinal;
    exports com.mycompany.proyectofinal.Controllers;
}