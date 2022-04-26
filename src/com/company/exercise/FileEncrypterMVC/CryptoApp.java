package com.company.exercise.FileEncrypterMVC;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;


// Selects a file and checks it's the file you wanted
public class CryptoApp extends Application {
    String dir = "src/com/company/exercise/files";
    String fileName = "Very confidential hacking.pdf";
    Stage window, registerWindow;
    Scene loginScene, cryptoScene, registerScene;
    CryptoTool cryptoTool;
    KeyTool keyTool;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("CryptR");
        cryptoTool = new CryptoTool();
        keyTool = new KeyTool();

        if(keyTool.fileCheck()) {
            LoginView loginView = new LoginView(keyTool);
            loginView.show();
            if(loginView.isLoggedIn) initCryptoView();
        }

        if(!keyTool.fileCheck()){
            RegisterView registerView = new RegisterView(keyTool);
            registerView.show();
            if (registerView.isRegistered) initCryptoView();
        }
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

