package com.company.exercise.FileEncrypterMVC;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.KeyStoreException;


// Selects a file and checks it's the file you wanted
public class CryptoApp extends Application {
    String dir = "src/com/company/exercise/files";
    String fileName = "Very confidential file_testing.pdf";
    String testFile = "files/test files/Very confidential file_testing.pdf";
    Stage window;
    Scene cryptoScene;
    CryptoTool cryptoTool;
    KeyTool keyTool;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        test1();
        test2();
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

    public void initCryptoView() {
        CryptoView cryptoView = new CryptoView(cryptoTool, keyTool);
        cryptoScene = new Scene(cryptoView, 500, 70);
        window.setScene(cryptoScene);
        window.setOnCloseRequest(e -> {
            cryptoView.close();
        });
        window.show();
    }

    /**
     Test case 1: Test that checks if hash gets rejected when the encrypted file is tampered.
     The results are printed in the terminal.
     **/

    public void test1(){
        System.out.println("Test case 1: Test that checks if hash gets verified when encrypted file is not tampered");
        CryptoTool ct_test = new CryptoTool();
        KeyTool kt_test = new KeyTool();
        kt_test.createKeyStore("storepassword", true);
        kt_test.generateAndAddKey("keypassword", "key");
        SecretKeySpec testKey = kt_test.getKey("key", "keypassword");
        try {
            kt_test.printContent();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        ct_test.encryptFile(testFile, "AES", testKey);
        ct_test.decryptFile(testFile + ".AES", "AES", testKey, true);
        new File(testFile + ".AES").delete();
    }

    /**
     Test case 2: Test that checks if hash gets verified when the encrypted file is not tampered.
     The results are printed in the terminal.
     **/

    public void test2(){
        System.out.println("Test case 2: Test if hash gets verified when encrypted file is not tampered");
        CryptoTool ct_test = new CryptoTool();
        KeyTool kt_test = new KeyTool();
        kt_test.createKeyStore("storepassword", true);
        kt_test.generateAndAddKey("keypassword", "key");
        SecretKeySpec testKey = kt_test.getKey("key", "keypassword");
        try {
            kt_test.printContent();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        ct_test.encryptFile(testFile, "AES", testKey);
        ct_test.decryptFile(testFile + ".AES", "AES", testKey, false);
        new File(testFile + ".AES").delete();
        new File(testFile + ".test").delete();

    }
}

