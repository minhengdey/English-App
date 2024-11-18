module org.example.englishapp_callapigemini {
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.text;
    requires javafx.web;
    requires org.json;
    requires java.desktop;
    requires javafx.media;
    opens org.example.englishapp_callapigemini to javafx.fxml;
    exports org.example.englishapp_callapigemini;
}