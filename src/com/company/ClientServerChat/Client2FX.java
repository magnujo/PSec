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

        if (Server.rsaPublicKey == null) {
            System.out.println(Server.rsaPublicKey.getModulus());
            System.out.println("Client 2 stores pub key");
           // client.sendKey(pubkey);
        }

        else {
            System.out.println("Client 2 gets pub key from server and encrypts sym key");
            //client.sendKey(ct.encryptKey(ct.aesSymKey, Server.rsaPublicKey));
        }

        View view = new View(ct, client);
        Scene scene = new Scene(view, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            view.close();
        });
    }
}

