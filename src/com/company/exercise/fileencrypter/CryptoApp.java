package com.company.exercise.fileencrypter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


// Selects a file and checks it's the file you wanted
public class CryptoApp extends Application {
    String dir = "src/com/company/exercise/files";
    String fileName = "Very confidential hacking.pdf";
    Stage window1, window2;
    Scene loginScene, cryptoScene, registerScene;

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) {
        window1 = primaryStage;
        window2 = new Stage(); // to open filechooser in a new window
        window1.setTitle("CryptR");


        //Crypto stuff
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(dir));

        Button encryptbutton = new Button("Encrypt");
        Button decryptbutton = new Button("Decrypt");
        Button encrypthashbutton = new Button("Encrypt and hash");
        Button decrypthashbutton = new Button("Decrypt and de-hash");

        CryptoTool cryptoTool = new CryptoTool();

        encryptbutton.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.encryptFile(selectedFile.getPath(), "AES", false);
        });

        encrypthashbutton.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.encryptFile(selectedFile.getPath(), "AES", true);
        });

        decryptbutton.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.decryptFile(selectedFile.getPath(), "AES", false);
        });

        decrypthashbutton.setOnAction(e -> {
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.decryptFile(selectedFile.getPath(), "AES", true);
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(encryptbutton, decryptbutton, encrypthashbutton, decrypthashbutton);
        cryptoScene = new Scene(vBox, 400, 200);

        //Login stuff
        Button loginButton = new Button("Login");
        PasswordField loginPWField = new PasswordField();
        KeyTool keyTool = new KeyTool();

        loginButton.setOnAction(e -> {
            window1.setScene(cryptoScene);
            keyTool.load(loginPWField.getText());
            System.out.println(loginPWField.getText());
        });

        VBox loginLayout = new VBox();
        loginLayout.getChildren().addAll(loginButton, loginPWField);
        loginScene = new Scene(loginLayout, 400, 200);

        //Register stuff
        Label registerLabel = new Label("As this is your first time using the keystore, please provide a password for the store that you" +
                "will use next time you open this app");
        Button registerButton = new Button("Register Store Password");
        PasswordField registerPWField = new PasswordField();
        registerButton.setOnAction(e -> {

            if (registerPWField.getText().length() <= 8) {
                    AlertBox.display("Password error", "Password must be more than 8 characters long");
                 }
            else {
                keyTool.createKeyStore(registerPWField.getText());
                window1.setScene(cryptoScene);
                System.out.println(registerPWField.getText());
            }
        });

        VBox registerLayout = new VBox();
        registerLayout.getChildren().addAll(registerLabel, registerPWField, registerButton);
        registerScene = new Scene(registerLayout, 400, 200);


        //Scene handling
        if (keyTool.fileCheck()){
            window1.setScene(loginScene);
        }
        else {
            window1.setScene(registerScene);
        }

        window1.show();


    }
    public void testCase(File file) {
        if (file != null && file.getName().equals(fileName))
            System.out.println("Test case succeeded");
        else System.out.println("Test case failed");
    }
}

