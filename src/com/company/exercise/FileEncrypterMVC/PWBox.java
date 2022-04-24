package com.company.exercise.FileEncrypterMVC;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PWBox {
    Stage window;
    PasswordField pwfield;
    private String PW;
    boolean buttonPressed;
    boolean closed;


    public void display(String title, String message){
        buttonPressed = false;
        closed = false;
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 = new Label(message);
        Button savebutton = new Button("OK");
        pwfield = new PasswordField();
        savebutton.setOnAction(e -> {
            buttonPressed = true;
            PW = pwfield.getText();
            window.close();
        });


        VBox layout = new VBox();
        layout.getChildren().addAll(label1, pwfield, savebutton);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
        if (!buttonPressed) closed = true;

        window.setOnCloseRequest(e -> {
            System.out.println("Closing PWBox...");
        });
    }

    public String getPW(){
        return PW;
    }
    public boolean isClosed(){
        return closed;
    }



}
