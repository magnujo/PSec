package com.company.exercise.fileencrypter;
// FROM https://www.youtube.com/watch?v=SpL3EToqaXA&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG&index=5

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(message);
        Button closebutton = new Button("OK");
        closebutton.setOnAction(e -> window.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(label, closebutton);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

    }
}
