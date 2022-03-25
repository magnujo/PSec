package com.company.exercise.ChatApp;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class View extends BorderPane {

    double windowHeight = 700;
    double windowWidth = 500;


    public View() {

        VBox center = new VBox();
        ScrollPane chatPane = new ScrollPane();
        HBox bottomPane = new HBox();
        Button sendButton = new Button("Send");
        TextArea textArea = new TextArea();

        center.setPrefHeight(700);

        double bpHeight = windowHeight*0.25;
        double cpHeight = windowHeight*0.75;


        bottomPane.setPrefHeight(bpHeight);
        chatPane.setPrefHeight(cpHeight);

        textArea.setPrefHeight(bpHeight);
        textArea.setPrefWidth(windowWidth*0.50);

        sendButton.setPrefHeight(bpHeight);
        sendButton.setPrefWidth(windowWidth*0.20);

        bottomPane.getChildren().addAll(textArea, sendButton);

        center.getChildren().addAll(chatPane,bottomPane);

        ScrollPane sidePane = new ScrollPane();
        sidePane.setPrefWidth(250);



        setCenter(center);
        setLeft(sidePane);



    }

}
