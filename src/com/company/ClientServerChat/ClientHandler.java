package com.company.ClientServerChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//This class contains code from WittCodes tutorial (https://www.youtube.com/watch?v=gchR3DpY-8Q)

public class ClientHandler implements Runnable {
    int port;

    public ClientHandler(int port){
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                socket = serverSocket.accept();

                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                System.out.println("Client connected to port " + port);

                while (true) {
                    String msgFromClient = bufferedReader.readLine();

                    if(msgFromClient == null) break;

                    System.out.println("Client: " +  msgFromClient);

                    bufferedWriter.write("Message received");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                System.out.println("Client disconnected from port " + port);

                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
