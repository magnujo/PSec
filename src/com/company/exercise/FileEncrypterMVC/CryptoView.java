package com.company.exercise.FileEncrypterMVC;

import com.company.exercise.pwmanager.PMGridPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class CryptoView extends HBox {
    CryptoTool cryptoTool;
    String dir = "files";
    FileChooser fileChooser;
    KeyTool keyTool;
    KeysBox keysBox;
    CreateKeyBox createKeyBox;

    public CryptoView(CryptoTool cryptoTool, KeyTool keyTool) {

        this.cryptoTool = cryptoTool;
        this.keyTool = keyTool;
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(dir));
        keysBox = new KeysBox(keyTool);
        createKeyBox = new CreateKeyBox();

        setSpacing(10);
        getChildren().add(new EncryptButton());
        getChildren().add(new DecryptButton());
        getChildren().add(new EncryptHashButton());
        getChildren().add(new DecryptDehashButton());
        getChildren().add(new CreateKeyButton());
        getChildren().add(new DeleteKeyButton());
        setAlignment(Pos.CENTER);
    }

    void createKey(){
        createKeyBox.display("New Key", "Enter password for new key");
        if (!createKeyBox.isClosed()) {
            System.out.println("Generating new key");
            keyTool.generateAndAddKey(createKeyBox.getPW(), createKeyBox.getAlias());
            AlertBox.display("Success!", "Key was created successfully");
        }
    }

    void deleteKey(){
        if (keyTool.size() > 0){
            keysBox.display("Delete", "Select key to delete", true);
            if (!keysBox.isClosed() && keysBox.isPWCorrect) {
                keysBox.display("Delete", "Select key to delete", true);
                }
            }
        else AlertBox.display("Error", "No keys were found");
        }

    void encrypt(boolean hash){
        if (keyTool.size() > 0){
            keysBox.display("Select key", "Select key", false);
            if (!keysBox.isClosed() && keysBox.isPWCorrect) {
                File selectedFile = null;
                selectedFile = fileChooser.showOpenDialog(new Stage());
                if (selectedFile != null) {
                    cryptoTool.encryptFile(selectedFile.getPath(), "AES", hash, keysBox.getKey());
                    AlertBox.display("Success!", "File was encrypted successfully");
                }
                else AlertBox.display("Error", "Error");
            }
        }
        else AlertBox.display("Error", "Please create key before encrypting");
    }

    void decrypt(boolean hash){
        if (keyTool.size() > 0){
            keysBox.display("Select key", "Select key", false);
            if (!keysBox.isClosed()) {
                File selectedFile = null;
                selectedFile = fileChooser.showOpenDialog(new Stage());
                if (selectedFile != null) {
                    String res = cryptoTool.decryptFile(selectedFile.getPath(), "AES", hash, keysBox.getKey());
                    if (res.equalsIgnoreCase("WrongKey")) AlertBox.display("Wrong Key", "Failed to decrypt. Try using another key");
                    else if(res.equalsIgnoreCase("FileChanged")) AlertBox.display("Warning", "WARNING: Decryption failed: File has been changed by an untrusted party");
                    else AlertBox.display("Success", "Decryption was successful");
                }
            }
        }
        else AlertBox.display("Error", "Please create key before encrypting");
    }

    void close(){
        System.out.println(Arrays.toString(keyTool.storePW));
        if(keyTool.storePW != null){
            keyTool.store();
            System.out.println("Saving...");
        }
    }
}

class CreateKeyButton extends Button {
    CreateKeyButton(){
        setText("Create Key");
        setOnAction(e -> {
            CryptoView cv = (CryptoView) getParent();
            cv.createKey();
        });
    }
}

class DeleteKeyButton extends Button {
    DeleteKeyButton(){
        setText("Delete Key");
        setOnAction(e -> {
            CryptoView cv = (CryptoView) getParent();
            cv.deleteKey();
        });
    }
}

class EncryptButton extends Button {
    EncryptButton() {
        setText("Encrypt File");
        setOnAction(e -> {
            CryptoView cv = (CryptoView) getParent();
            cv.encrypt(false);
        });
    }
} // LoadButton

class DecryptButton extends Button {
    DecryptButton() {
        setText("Decrypt File");
        setOnAction(e -> {
            CryptoView cv = (CryptoView) getParent();
            cv.decrypt(false);
        });
    }
}

class EncryptHashButton extends Button {
    EncryptHashButton() {
        setText("Encrypt and Hash");
        setOnAction(e -> {
            CryptoView cv = (CryptoView) getParent();
            cv.encrypt(true);
        });
    }
}

class DecryptDehashButton extends Button {
    DecryptDehashButton() {
        setText("Decrypt and De-Hash");
        setOnAction(e -> {
            CryptoView cv = (CryptoView) getParent();
            cv.decrypt(true);
        });
    }
}