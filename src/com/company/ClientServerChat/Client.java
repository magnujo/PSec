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
    DataOutputStream dOut;
    OutputStream socketOutputStream;

    int port;

    public Client(int port){
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket("localhost", port);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            dOut = new DataOutputStream(socket.getOutputStream()); // output for bytes
            socketOutputStream = socket.getOutputStream(); //output for bytes
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (socket != null) socket.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (outputStreamWriter != null) outputStreamWriter.close();
                if (dOut != null) dOut.close();
                if (socketOutputStream != null) socketOutputStream.close();
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (IOException e2) {
                e.printStackTrace();
            }
        }
    }

    public void sendKey(byte[] key) throws IOException {
        System.out.println(key.length);
        dOut.writeBoolean(true);
        dOut.writeInt(key.length);
        dOut.write(key);
        dOut.flush();


        System.out.println("Server: " + bufferedReader.readLine());
    }

    public void receiveKey(byte[] key) throws IOException {
        System.out.println(key.length);
        dOut.writeBoolean(true);
        dOut.writeInt(key.length);
        dOut.write(key);
        dOut.flush();


        System.out.println("Server: " + bufferedReader.readLine());
    }

    public void disconnect(){
        try {
            dOut.writeBoolean(false);
            dOut.flush();
            if (socket != null) socket.close();
            if (inputStreamReader != null) inputStreamReader.close();
            if (outputStreamWriter != null) outputStreamWriter.close();
            if (dOut != null) dOut.close();
            if (socketOutputStream != null) socketOutputStream.close();
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String msg) throws IOException {
        dOut.writeBoolean(true);
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        System.out.println("Server: " + bufferedReader.readLine());

        if(msg.equalsIgnoreCase("BYE")) return false;
        else return true;
    }
}

