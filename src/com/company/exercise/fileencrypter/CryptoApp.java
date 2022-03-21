package com.company.exercise.fileencrypter;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;


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
        window1.setTitle("CryptR");
        window2 = new Stage(); // to open filechooser in a new window

        //Crypto stuff
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(dir));

        Button encryptbutton = new Button("Encrypt");
        Button decryptbutton = new Button("Decrypt");
        Button encrypthashbutton = new Button("Encrypt and hash");
        Button decrypthashbutton = new Button("Decrypt and de-hash");
        Button createkeybutton = new Button("Create Key");

        CryptoTool cryptoTool = new CryptoTool();
        KeyTool keyTool = new KeyTool();

        createkeybutton.setOnAction(e -> {
            CreateKeyBox createKeyBox = new CreateKeyBox();
            createKeyBox.display("New Key", "Enter password for new key");
            keyTool.generateAndAddKey(createKeyBox.getPW(), createKeyBox.getAlias());
        });

        encryptbutton.setOnAction(e -> {
            KeysBox keysBox = new KeysBox(keyTool);
            keysBox.display("Select key", "Select key");
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.encryptFile(selectedFile.getPath(), "AES", false, keysBox.getKey());

        });

        encrypthashbutton.setOnAction(e -> {
            KeysBox keysBox = new KeysBox(keyTool);
            keysBox.display("Select key", "Select key");
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.encryptFile(selectedFile.getPath(), "AES", true, keysBox.getKey());
        });

        decryptbutton.setOnAction(e -> {
            KeysBox keysBox = new KeysBox(keyTool);
            keysBox.display("Select key", "Select key");
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.decryptFile(selectedFile.getPath(), "AES", false, keysBox.getKey());
        });

        decrypthashbutton.setOnAction(e -> {
            KeysBox keysBox = new KeysBox(keyTool);
            keysBox.display("Select key", "Select key");
            File selectedFile = null;
            selectedFile = fileChooser.showOpenDialog(window2);
            cryptoTool.decryptFile(selectedFile.getPath(), "AES", true, keysBox.getKey());
        });

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(encryptbutton, decryptbutton, encrypthashbutton, decrypthashbutton, createkeybutton);
        hBox.setAlignment(Pos.CENTER);
        cryptoScene = new Scene(hBox, 500, 70);

        //Login stuff
        Button loginButton = new Button("Login");
        PasswordField loginPWField = new PasswordField();
        loginPWField.setMaxWidth(100);


        loginButton.setOnAction(e -> {
            String pwInput = loginPWField.getText();
            if(keyTool.checkStorePW(pwInput)){
                window1.setScene(cryptoScene);
                keyTool.load(pwInput);

            }
            else AlertBox.display("Warning!", "Wrong Password, try again");
        });

        VBox loginLayout = new VBox();
        loginLayout.setSpacing(10);
        loginLayout.getChildren().addAll(loginButton, loginPWField);
        loginLayout.setAlignment(Pos.CENTER);
        loginScene = new Scene(loginLayout, 150, 100);

        //Register stuff
        Label registerLabel = new Label("As this is your first time using the keystore,\n please provide a password for the store that" +
                "\n you will use next time you open this app");
        Button registerButton = new Button("Register Store Password");
        registerLabel.setAlignment(Pos.CENTER);
        registerLabel.setTextAlignment(TextAlignment.CENTER);
        PasswordField registerPWField = new PasswordField();
        registerPWField.setAlignment(Pos.CENTER);
        registerPWField.setMaxWidth(100);
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
        registerLayout.setSpacing(10);
        registerLayout.getChildren().addAll(registerLabel, registerPWField, registerButton);
        registerLayout.setAlignment(Pos.CENTER);
        registerScene = new Scene(registerLayout, 300, 150);

        //Scene handling
        if (keyTool.fileCheck()){
            window1.setScene(loginScene);
        }
        else {
            window1.setScene(registerScene);
        }

        window1.setOnCloseRequest(e -> {
            System.out.println(Arrays.toString(keyTool.storePW));
            if(keyTool.storePW != null){
                keyTool.store();
                System.out.println("Saving...");
            }
        });
        window1.show();
    }

    public void testCase(File file) {
        if (file != null && file.getName().equals(fileName))
            System.out.println("Test case succeeded");
        else System.out.println("Test case failed");
    }
}

