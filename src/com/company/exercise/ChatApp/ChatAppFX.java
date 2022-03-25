package com.company.exercise.ChatApp;

import com.company.exercise.pwmanager.PMGridPane;
import com.company.exercise.pwmanager.PasswordProtector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatAppFX extends Application {




    public void start(Stage stage) {
        stage.setTitle("Chat App");
        View view = new View();
        Scene scene = new Scene(view, 700, 500);
        stage.setScene(scene);
        stage.show();

    }

}

