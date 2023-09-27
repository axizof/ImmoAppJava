module com.example.immoapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.immoapp to javafx.fxml;
    exports com.example.immoapp;
}