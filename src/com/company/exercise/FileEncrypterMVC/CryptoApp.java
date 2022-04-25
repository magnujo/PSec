package com.company.exercise.FileEncrypterMVC;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


// Selects a file and checks it's the file you wanted
public class CryptoApp extends Application {
    String dir = "src/com/company/exercise/files";
    String fileName = "Very confidential hacking.pdf";
    Stage window, window2;
    Scene loginScene, cryptoScene, registerScene;
    CryptoTool cryptoTool;
    KeyTool keyTool;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("CryptR");
        window2 = new Stage();
        window2.setTitle("Register");
        cryptoTool = new CryptoTool();
        keyTool = new KeyTool();

        if(!keyTool.fileCheck()){
            System.out.println("hej");
            RegisterView registerView = new RegisterView(keyTool, window2);
            registerScene = new Scene(registerView, 300, 150);
            window2.setScene(registerScene);
            window2.show();
        }
        if(keyTool.fileCheck()) initCryptoView();

    }

    public void testCase(File file) {
        if (file != null && file.getName().equals(fileName))
            System.out.println("Test case succeeded");
        else System.out.println("Test case failed");
    }

    public void initCryptoView() {
        CryptoView cryptoView = new CryptoView(cryptoTool, keyTool);
        cryptoScene = new Scene(cryptoView, 500, 70);
        window.setScene(cryptoScene);
        window.setOnCloseRequest(e -> {
            cryptoView.close();
        });
        window.show();
    }


}

