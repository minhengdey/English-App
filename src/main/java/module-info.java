module com.noface.demo {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.web;
    requires java.net.http;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;
    requires org.json;


    opens com.noface.demo;
    opens com.noface.demo.model;
    opens com.noface.demo.controller;
    opens com.noface.demo.screen;
}