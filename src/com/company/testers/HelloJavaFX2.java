package com.company.testers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane; 
import javafx.stage.Stage;

public class HelloJavaFX2 extends Application {

  public void start(Stage primaryStage) {
    Scene scene = new Scene(new Pane(), 300, 100); // Set window size
    primaryStage.setTitle("Hello JavaFX"); 
    primaryStage.setScene(scene); 
    primaryStage.show();
  }
}
