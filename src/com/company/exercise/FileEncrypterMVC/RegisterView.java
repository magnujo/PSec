package com.company.exercise.FileEncrypterMVC;

import com.company.exercise.fileencrypter.AlertBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.security.Key;

public class RegisterView extends VBox {
    KeyTool keyTool;
    PasswordField registerPWField;
    boolean isRegistered;
    Stage window2;

    RegisterView(KeyTool keyTool, Stage window2){
        this.keyTool = keyTool;
        registerPWField = new PasswordField();
        registerPWField.setAlignment(Pos.CENTER);
        registerPWField.setMaxWidth(100);
        this.window2 = window2;

        Label registerLabel = new Label("Please enter a master password to use for this application");
        registerLabel.setAlignment(Pos.CENTER);
        registerLabel.setTextAlignment(TextAlignment.CENTER);

        getChildren().add(registerLabel);
        getChildren().add(registerPWField);
        getChildren().add(new RegisterButton());

        setSpacing(10);
        setAlignment(Pos.CENTER);

    }

    public void register(){
        if (registerPWField.getText().length() <= 8) {
            AlertBox.display("Password error", "Password must be more than 8 characters long");
            isRegistered = false;
        }

        else {
            System.out.println("hegeheg");
            keyTool.createKeyStore(registerPWField.getText());
            isRegistered = true;
            System.out.println(registerPWField.getText());
            window2.close();
        }
    }
}

class RegisterButton extends Button {
    RegisterButton() {
        setText("Register");
        setOnAction(e -> {
            RegisterView rv = (RegisterView) getParent();
            rv.register();
        });
    }
}
