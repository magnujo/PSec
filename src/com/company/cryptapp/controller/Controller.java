package com.company.cryptapp.controller;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


// Selects a file and checks it's the file you wanted
public class Controller extends Application {

    String dir = "files";
    String fileName = "file1.txt";

    public void start(Stage primaryStage) {
        Stage anotherStage = new Stage(); // to open filechooser in a new window
        primaryStage.setTitle("Filechooser example");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(dir));
        Button button = new Button("Select File");
        button.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(anotherStage);
            testCase(selectedFile);
        });
        VBox vBox = new VBox();
        vBox.getChildren().add(button);
        Scene scene = new Scene(vBox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void testCase(File file) {
        if (file != null && file.getName().equals(fileName))
            System.out.println("Test case succeeded");
        else System.out.println("Test case failed");
    }
}