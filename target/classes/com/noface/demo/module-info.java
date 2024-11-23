module com.noface.demo{
   requires javafx.fxml;
   requires javafx.controls;
   requires spring.web;

   opens com.noface.demo;
   opens com.noface.demo.card;
   opens com.noface.demo.topic;
   opens com.noface.demo.controller;
}