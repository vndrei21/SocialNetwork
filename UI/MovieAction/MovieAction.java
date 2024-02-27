package com.example.lab8.UI.MovieAction;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MovieAction {
    public static void showMessage(Stage stage, Alert.AlertType alertType, String title, String msg)
    {
        Alert alert = new Alert(alertType);
        alert.initOwner(stage);
        alert.setContentText(msg);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
