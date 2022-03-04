package com.company.cryptapp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


// Selects a file and checks it's the file you wanted
public class View {
    public View() {
        Stage anotherStage = new Stage(); // to open filechooser in a new window
        FileChooser fileChooser = new FileChooser();
        Button button = new Button("Select File");
        VBox vBox = new VBox();
        vBox.getChildren().add(button);
        Scene scene = new Scene(vBox, 400, 200);
    }

}
