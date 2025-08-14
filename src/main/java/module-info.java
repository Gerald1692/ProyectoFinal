module com.mycompany.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    
    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.ModelosPOJOs to javafx.base, javafx.fxml;
    
    exports com.mycompany.proyectofinal;
}