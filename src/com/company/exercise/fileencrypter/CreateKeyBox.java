package com.company.exercise.fileencrypter;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateKeyBox {
    Stage window;
    PasswordField pwfield;
    String PW;
    String alias;
    TextField aliasfield;

    public void display(String title, String message){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 = new Label(message);
        Label label2 = new Label("Select name for key");
        Button savebutton = new Button("Save");
        pwfield = new PasswordField();
        aliasfield = new TextField();
        savebutton.setOnAction(e -> {
            this.PW = pwfield.getText();
            this.alias = aliasfield.getText();
            window.close();
        });


        VBox layout = new VBox();
        layout.getChildren().addAll(label2, aliasfield, label1, pwfield, savebutton);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
    }

    public String getPW(){
        return PW;
    }
    public String getAlias(){ return alias; }

}