package com.company.ClientServerMulti;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//Socket code is from WittCode (https://www.youtube.com/watch?v=gchR3DpY-8Q)

public class Server {

    public static void main(String[] args)  {
        ThreadOne threadOne = new ThreadOne();
        ThreadTwo threadTwo = new ThreadTwo();
        Thread t1 = new Thread(threadTwo);
        Thread t2 = new Thread(threadOne);

        t1.start();
        t2.start();


    }
}
