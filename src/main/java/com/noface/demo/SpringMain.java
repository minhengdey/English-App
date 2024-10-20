package com.noface.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import javafx.application.Application;

@SpringBootApplication
@RestController
public class SpringMain {
   public static void main(String[] args) {
      Application.launch(FXMain.class, args);
   }
}
