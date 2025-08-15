module com.mycompany.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires java.base;

    // Abre tus paquetes a javafx.fxml para reflexión
    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.Controllers to javafx.fxml;
    opens com.mycompany.proyectofinal.ModelosPOJOs to javafx.base, javafx.fxml;

    // Exporta tus paquetes
    exports com.mycompany.proyectofinal;
    exports com.mycompany.proyectofinal.Controllers;
}
