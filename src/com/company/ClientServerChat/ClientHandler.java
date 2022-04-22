package com.company.ClientServerChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import org.apache.commons.lang3.SerializationUtils;



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
        DataInputStream dIn;
        RSAPublicKey rsaPublicKey;

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

                dIn = new DataInputStream(socket.getInputStream());

                bufferedReader = new BufferedReader(inputStreamReader);

                bufferedWriter = new BufferedWriter(outputStreamWriter);
                System.out.println("Client connected to port " + port);


                while (true) {
                    if (dIn.readBoolean()){
                        int length = dIn.readInt();
                        if(length>0) {
                            byte[] input = new byte[length];
                            dIn.readFully(input, 0, input.length); // read the message
                            String sInput = new String(input);
                            if(input == null || sInput.equalsIgnoreCase("BYE")) break;
                            System.out.println("Client: " + sInput);
                            rsaPublicKey = (RSAPublicKey) CryptoTool.decodeKey(input);
                            System.out.println("Transfered key: " + rsaPublicKey.toString());
                            System.out.println("Transfered mod: " + rsaPublicKey.getModulus());
                            System.out.println("Transfered exp: " + rsaPublicKey.getPublicExponent());
                            System.out.println("Transfered params: " + rsaPublicKey.getParams());
                        }
                        else break;
                    }
                    else break;


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
                dIn.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
    }
}
