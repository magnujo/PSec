package com.company.ClientServerChat;



public class Server {

    public static void main(String[] args)  {
        Server server = new Server();
        server.run();
    }

    public void run(){
        ClientHandler ch1 = new ClientHandler(1234);
        ClientHandler ch2 = new ClientHandler(1235);

        Thread t1 = new Thread(ch2);
        Thread t2 = new Thread(ch1);

        t1.start();
        t2.start();
    }
}
