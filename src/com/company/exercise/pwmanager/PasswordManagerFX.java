package com.company.exercise.pwmanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// This program defines a simple password manager

public class PasswordManagerFX extends Application {

    static String dir = "/Users/nielsj/Desktop/dev/java/keepassxc";
    static String passwordFileName = dir + "/encryptedPasswordTable";

    public void start(Stage stage) {
        stage.setTitle("Password Manager");
        PasswordProtector pp = new PasswordProtector(passwordFileName);
        PMGridPane pm = new PMGridPane(pp);
        Scene scene = new Scene(pm, 700, 300);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            pp.store(pm.pt);
        });
    }

}