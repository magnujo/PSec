package com.company.exercise.FileEncrypterMVC;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bouncycastle.oer.its.HeaderInfoContributorId;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Iterator;

public class KeysBox {
    Stage window;
    KeyTool keyTool;
    boolean closed;
    SecretKeySpec key;
    boolean buttonPressed;
    PWBox pwBox;
    boolean isPWCorrect;

    KeysBox (KeyTool keyTool){

        this.keyTool = keyTool;
        pwBox = new PWBox();
    }

    public void display(String title, String message, boolean deleteFunction){
        buttonPressed = false;
        closed = false;
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
            if(deleteFunction) {
                try {
                    for (Iterator<String> i = keyTool.getKeyAliases().asIterator(); i.hasNext(); ) {
                        String alias = i.next();
                        Button b = new Button(alias);
                        b.setOnAction(e -> {
                            pwBox.display("Enter password", "Enter password for key: " + alias);
                            if (!pwBox.isClosed()) {
                                buttonPressed = true;
                                System.out.println("Alias: " + alias);
                                String pw = pwBox.getPW();
                                System.out.println("Password: " + pwBox.getPW());
                                isPWCorrect = keyTool.checkKeyPW(alias, pw);

                                if (isPWCorrect) {
                                    keyTool.deleteKey(alias);
                                }
                                else {
                                    AlertBox.display("Wrong Password", "Wrong Password");
                                    window.close();
                                }
                            }
                            if (pwBox.buttonPressed) window.close();
                        });
                        items.add(b);
                    }
                }catch (KeyStoreException e) {e.printStackTrace();}
            }

            else{
                try {
                    for (Iterator<String> i = keyTool.getKeyAliases().asIterator(); i.hasNext(); ) {
                        String alias = i.next();
                        Button b = new Button(alias);
                        b.setOnAction(e -> {

                            pwBox.display("Enter password", "Enter password for key: " + alias);
                            if (!pwBox.isClosed()) {
                                buttonPressed = true;
                                System.out.println("Alias: " + alias);
                                String pw = pwBox.getPW();
                                System.out.println("Password: " + pwBox.getPW());
                                isPWCorrect = keyTool.checkKeyPW(alias, pw);

                                if (isPWCorrect) {
                                    key = keyTool.getKey(alias, pw);
                                    System.out.println("Key: " + key.toString());
                                }
                                else {
                                    AlertBox.display("Wrong Password", "Wrong Password");
                                    window.close();
                                }
                            }

                            if (pwBox.buttonPressed) window.close();
                        });
                        items.add(b);
                    }
                }catch (KeyStoreException e) {e.printStackTrace();}
            }
        }


        Label label = new Label(message);

        VBox layout = new VBox();
        layout.getChildren().add(label);
        layout.getChildren().addAll(items);
        Scene scene = new Scene(layout);


        window.setScene(scene);
        window.showAndWait();
        if (!buttonPressed) closed = true;

        window.setOnCloseRequest(e -> {
            System.out.println("Closing keysbox...");
        });
    }

    public boolean isClosed(){
        return closed;
    }
    public SecretKeySpec getKey(){return key;}
}

