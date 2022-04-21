package com.company.ClientServerChat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

public class Client2FX extends Application {
//client server connections code from https://www.youtube.com/watch?v=gchR3DpY-8Q

    public void start(Stage stage) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        stage.setTitle("Client 2");
        CryptoTool ct = new CryptoTool();
        Client client = new Client(1235);
        client.connect();


       byte[] pubkey = ct.getPubKey().getEncoded();

       /* byte[] pubkey = ("ct.getPubKey().getEncoded();client.sendKey(pubkey); View view = new " +
                "View(ct, client);Scene scene = new Scene(view, 700, 500);stage.setScene(scene);" +
                "stage.show();stage.setOnCloseRequest(e -> {view.close();" +
                "stage.show();stage.setOnCloseRequest(e -> {view.close();" +
                "View(ct, client);Scene scene = new Scene(view, 700, 500);stage.setScene(scene);" +
                "ct.getPubKey().getEncoded();client.sendKey(pubkey); View view = new ").getBytes(StandardCharsets.UTF_8);*/
       // byte[] pubkey = "i am key".getBytes(StandardCharsets.UTF_8);

        client.sendKey(pubkey);

        View view = new View(ct, client);
        Scene scene = new Scene(view, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            view.close();
        });
    }
}

