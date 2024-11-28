module com.noface.englishapp {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.web;
    requires java.net.http;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires org.slf4j;
    requires java.desktop;


    opens com.noface.englishapp;
    opens com.noface.englishapp.model;
    opens com.noface.englishapp.controller;
    opens com.noface.englishapp.view;
    opens com.noface.englishapp.topic;
    opens com.noface.englishapp.utils;
}