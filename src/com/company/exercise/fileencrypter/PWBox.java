package com.company.exercise.fileencrypter;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PWBox {
    static Stage window;
    static PasswordField pwfield;
    static String PW;


    public static void display(String title, String message){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 = new Label(message);
        Button savebutton = new Button("OK");
        pwfield = new PasswordField();
        savebutton.setOnAction(e -> {
            PW = pwfield.getText();
            window.close();
        });


        VBox layout = new VBox();
        layout.getChildren().addAll(label1, pwfield, savebutton);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
    }

    public String getPW(){
        return PW;
    }


}
