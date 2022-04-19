package com.company.SingleClientChat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class View extends BorderPane {

    double windowHeight = 700;
    double windowWidth = 500;
    double bpHeight = windowHeight*0.25;
    double cpHeight = windowHeight*0.75;
    TextArea textArea;
    VBox chatBox;
    CryptoTool ct;



    public View(CryptoTool ct) {
        this.ct = ct;
        //Center:
        //Chat pane (area to display chat messages)
        ScrollPane chatPane = new ScrollPane();
        chatBox = new VBox();
        chatBox.setPrefHeight(cpHeight*0.5);
        chatBox.setPrefWidth(chatPane.getPrefWidth());
        chatPane.setFitToWidth(true);
        chatPane.setPrefHeight(cpHeight);
        chatPane.setContent(chatBox);
        chatPane.setPadding(new Insets(10,10,10,10));

        //Bottom pane (Text field and send button)
        HBox bottomPane = new HBox();
        SendButton sendButton = new SendButton();
        RecieveButton recieveButton = new RecieveButton(); //just for testing
        textArea = new TextArea();
        textArea.setPromptText("Write your message here...");
        textArea.setPrefHeight(bpHeight);
        textArea.setPrefWidth(windowWidth*0.60);
        sendButton.setPrefHeight(bpHeight/2);
        sendButton.setPrefWidth(windowWidth*0.30);
        recieveButton.setPrefHeight(bpHeight/2);
        recieveButton.setPrefWidth(windowWidth*0.30);
        bottomPane.setPrefHeight(bpHeight);
        bottomPane.getChildren().addAll(textArea, sendButton, recieveButton);

        VBox center = new VBox();
        center.setPrefHeight(700);
        center.getChildren().addAll(chatPane,bottomPane);
        setCenter(center);

        //Left:
        //Side pane (Contacts list)
        ScrollPane sidePane = new ScrollPane();
        sidePane.setPrefWidth(250);

        setLeft(sidePane);

    }



    String send(){
        String text = textArea.getText();
        HBox messageBox = new HBox();
        Label label = new Label(text);
        messageBox.getChildren().add(label);
        chatBox.getChildren().add(messageBox);
        textArea.clear();
        return text;
    }

    String recieve(){
        String text = textArea.getText();
        HBox messageBox = new HBox();
       // messageBox.setPrefWidth(chatBox.getPrefWidth());
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        Label label = new Label(text);
        messageBox.getChildren().add(label);
        chatBox.getChildren().add(messageBox);
        textArea.clear();
        return text;
    }
}

class SendButton extends Button {
    SendButton(){
        setText("Send");
        setOnAction(e -> {
            View view = (View) getParent().getParent().getParent();
            view.send();
        });
    }
}

class RecieveButton extends Button {
    RecieveButton(){
        setText("Recieve");
        setOnAction(e -> {
            View view = (View) getParent().getParent().getParent();
            view.recieve();
        });
    }
}