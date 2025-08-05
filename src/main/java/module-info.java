module com.mycompany.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.media;
    requires java.sql;
    requires com.zaxxer.hikari;

    opens com.mycompany.proyectofinal to javafx.fxml;
    opens com.mycompany.proyectofinal.Modelos to javafx.base;
    exports com.mycompany.proyectofinal;
}