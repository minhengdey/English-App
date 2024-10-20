module org.example.englishapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.englishapp to javafx.fxml;
    exports org.example.englishapp;
    exports org.example.englishapp.Controller;
    opens org.example.englishapp.Controller to javafx.fxml;
}