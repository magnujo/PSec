package com.company.exercise.fileencrypter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


// Selects a file and checks it's the file you wanted
public class CryptoApp extends Application {
    String dir = "src/com/company/exercise/files";
    String fileName = "Very confidential hacking.pdf";

    public void start(Stage primaryStage) {
        Stage anotherStage = new Stage(); // to open filechooser in a new window
        primaryStage.setTitle("CryptR");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(dir));
        Button encryptbutton = new Button("Select File To Encrypt");
        Button decryptbutton = new Button("Select File To Decrypt");
        VBox vBox = new VBox();
        vBox.getChildren().add(encryptbutton);
        vBox.getChildren().add(decryptbutton);
        Scene scene = new Scene(vBox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
        CryptoTool cryptoTool = new CryptoTool();
        encryptbutton.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(anotherStage);
            cryptoTool.encryptFile(selectedFile.getPath(), "AES");
        });
        decryptbutton.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(anotherStage);
            cryptoTool.decryptFile(selectedFile.getPath(), "AES");
        });

    }
    public void testCase(File file) {
        if (file != null && file.getName().equals(fileName))
            System.out.println("Test case succeeded");
        else System.out.println("Test case failed");
    }
}

