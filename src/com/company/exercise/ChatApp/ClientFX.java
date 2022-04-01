package com.company.exercise.ChatApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientFX extends Application {


    public void start(Stage stage) {
        stage.setTitle("Chat App");
        View view = new View();

        Scene scene = new Scene(view, 700, 500);
        stage.setScene(scene);
        stage.show();
    }
}

