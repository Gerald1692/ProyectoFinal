module com.mycompany.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    

    opens com.mycompany.proyectofinal to javafx.fxml, javafx.base;
 
    exports com.mycompany.proyectofinal;
}
