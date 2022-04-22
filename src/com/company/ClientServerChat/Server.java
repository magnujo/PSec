package com.company.ClientServerChat;


import javax.crypto.spec.SecretKeySpec;
import java.security.interfaces.RSAPublicKey;

public class Server {
    public static RSAPublicKey rsaPublicKey;
    public static SecretKeySpec aesSymKey;

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
        System.out.println("Just before whileloop");

        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (rsaPublicKey == null) System.out.println("null");
            else System.out.println("Not null");
        }
    }
}
