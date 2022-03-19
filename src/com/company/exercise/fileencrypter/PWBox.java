package com.company.exercise.fileencrypter;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PWBox  {
    Stage window;
    PasswordField pwfield;
    String PW;

    public void display(String title, String message){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(message);
        Button savebutton = new Button("Save");
        pwfield = new PasswordField();
        savebutton.setOnAction(e -> {
            this.PW = pwfield.getText();
            window.close();
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(label, pwfield, savebutton);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
    }

    public String getPW(){
        return PW;
    }
}