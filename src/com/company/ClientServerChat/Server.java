package com.company.ClientServerChat;

//Socket code is from WittCode (https://www.youtube.com/watch?v=gchR3DpY-8Q)

public class Server {

    public static void main(String[] args)  {
        ThreadOne threadOne = new ThreadOne();
        ThreadTwo threadTwo = new ThreadTwo();
        ClientFX clientFX = new ClientFX();
        Thread t1 = new Thread(clientFX);
        Thread t2 = new Thread(threadOne);

        t1.start();
        //t2.start();


    }
}
