package com.company.ClientServerChat;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Server server = new Server();
        server.run();

        Stage stage2 = new Stage();

        ClientFX client1 = new ClientFX();
        Client2FX client2 = new Client2FX();
        client1.start(stage);
        client2.start(stage2);
    }
}
