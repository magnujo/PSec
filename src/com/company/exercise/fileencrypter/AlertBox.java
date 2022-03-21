package com.company.exercise.fileencrypter;
// FROM https://www.youtube.com/watch?v=SpL3EToqaXA&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG&index=5

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AlertBox  {
    static Stage window;

    public static void display(String title, String message){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label(message);
        Button closebutton = new Button("OK");

        closebutton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(label, closebutton);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

    }
}
