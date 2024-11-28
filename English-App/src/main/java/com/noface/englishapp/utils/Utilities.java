package com.noface.englishapp.utils;

import javafx.scene.control.Alert;

public class Utilities {
    private static  Utilities util;
    private Utilities() {

    }
    public static Utilities getInstance(){
        if(util == null){
            util = new Utilities();
        }
        return util;
    }
    public  void showAlert(String message, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
