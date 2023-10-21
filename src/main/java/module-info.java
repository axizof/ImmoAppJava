module com.example.immoapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires jBCrypt;

    opens com.example.immoapp to javafx.fxml;
    exports com.example.immoapp;
}