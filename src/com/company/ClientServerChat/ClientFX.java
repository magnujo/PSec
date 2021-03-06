package com.company.ClientServerChat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientFX extends Application {
//client server connections code from https://www.youtube.com/watch?v=gchR3DpY-8Q

    public void start(Stage stage) throws IOException {
        stage.setTitle("Client 1");
        CryptoTool ct = new CryptoTool();
        Client client = new Client(1234);
        client.connect();

        byte[] pubkey = ct.getPubKey().getEncoded();

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

