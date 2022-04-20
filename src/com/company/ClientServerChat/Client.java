package com.company.ClientServerChat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//This class includes code from WittCode's tutorial (https://www.youtube.com/watch?v=gchR3DpY-8Q)
public class Client {
    Socket socket;
    InputStreamReader inputStreamReader;
    OutputStreamWriter outputStreamWriter;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    int port;

    public Client(int port){
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket("localhost", port);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (socket != null) socket.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (outputStreamWriter != null) outputStreamWriter.close();
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (IOException e2) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(){
        try {
            if (socket != null) socket.close();
            if (inputStreamReader != null) inputStreamReader.close();
            if (outputStreamWriter != null) outputStreamWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String msg) throws IOException {
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        System.out.println("Server: " + bufferedReader.readLine());

        if(msg.equalsIgnoreCase("BYE")) return false;
        else return true;
    }
}

