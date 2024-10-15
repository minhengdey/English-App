module com.noface.demo {
    requires javafx.fxml;
    requires javafx.web;
    requires org.fxmisc.richtext;
    requires org.jsoup;
//    ;


    opens com.noface.demo to javafx.fxml;
    opens com.noface.demo.model to javafx.base;

    opens com.noface.demo.view to javafx.fxml;
    exports com.noface.demo;
    exports com.noface.demo.view;
}
