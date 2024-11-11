module org.example.englishapp_callapigemini {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.englishapp_callapigemini to javafx.fxml;
    exports org.example.englishapp_callapigemini;
}