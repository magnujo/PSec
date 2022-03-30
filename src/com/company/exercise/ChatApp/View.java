package com.company.exercise.ChatApp;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class View extends BorderPane {

    double windowHeight = 700;
    double windowWidth = 500;
    double bpHeight = windowHeight*0.25;
    double cpHeight = windowHeight*0.75;
    TextArea textArea;
    VBox chatBox;



    public View() {
        //Center:
        //Chat pane (area to display chat messages)
        ScrollPane chatPane = new ScrollPane();
        chatBox = new VBox();
        chatBox.setPrefHeight(cpHeight*0.5);
        chatBox.setPrefWidth(chatPane.getPrefWidth());
        chatPane.setFitToWidth(true);
        chatPane.setPrefHeight(cpHeight);
        chatPane.setContent(chatBox);

        //Bottom pane (Text field and send button)
        HBox bottomPane = new HBox();
        SendButton sendButton = new SendButton();
        textArea = new TextArea();
        textArea.setPromptText("Write your message here...");
        textArea.setPrefHeight(bpHeight);
        textArea.setPrefWidth(windowWidth*0.60);
        sendButton.setPrefHeight(bpHeight);
        sendButton.setPrefWidth(windowWidth*0.30);
        bottomPane.setPrefHeight(bpHeight);
        bottomPane.getChildren().addAll(textArea, sendButton);

        VBox center = new VBox();
        center.setPrefHeight(700);
        center.getChildren().addAll(chatPane,bottomPane);
        setCenter(center);

        //Left:
        //Side pane
        ScrollPane sidePane = new ScrollPane();
        sidePane.setPrefWidth(250);

        setLeft(sidePane);

    }

    void send(){
        String text = textArea.getText();
        HBox messageBox = new HBox();
        Label label = new Label(text);
        messageBox.getChildren().add(label);
        chatBox.getChildren().add(messageBox);
        textArea.clear();
    }

}

class SendButton extends Button {
    SendButton(){
        setText("end");
        setOnAction(e -> {
            View view = (View) getParent().getParent().getParent();
            view.send();

        });
    }
}