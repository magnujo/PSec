package com.company.ClientServerMulti;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args)  {
        ThreadOne threadOne = new ThreadOne();
        ThreadTwo threadTwo = new ThreadTwo();
        Thread t1 = new Thread(threadOne);
        Thread t2 = new Thread(threadTwo);

        t1.start();
        t2.start();


    }
}
