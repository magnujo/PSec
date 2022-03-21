package com.company.exercise.fileencrypter;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class KeysBox {
    Stage window;
    KeyTool keyTool;
    boolean closed = false;
    SecretKeySpec key;

    KeysBox (KeyTool keyTool){
        this.keyTool = keyTool;
    }

    public void display(String title, String message){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        ArrayList<Button> items = new ArrayList<>();
        try {
            keyTool.printContent();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        if (keyTool.size() > 0) {
            try {
                for (Iterator<String> i = keyTool.getKeyAliases().asIterator(); i.hasNext(); ) {
                    String alias = i.next();
                    Button b = new Button(alias);
                    b.setOnAction(e -> {
                        PWBox.display("Enter password", "Enter password for key: " + alias);
                        System.out.println("Alias: " + alias);
                        String pw = PWBox.PW;
                        System.out.println("Password: " + PWBox.PW);
                        key = keyTool.getKey(alias, pw);

                        System.out.println("Key: " + key.toString());
                        window.close();
                    });
                    items.add(b);
                }
            }catch (KeyStoreException e) {e.printStackTrace();}
        }

        Label label = new Label(message);

        VBox layout = new VBox();
        layout.getChildren().add(label);
        layout.getChildren().addAll(items);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
        window.setOnCloseRequest(e -> {
            closed = true;
        });
    }

    public boolean isClosed(){
        return closed;
    }
    public SecretKeySpec getKey(){return key;}
}

