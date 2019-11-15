package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient implements ITCPClient{
    private String host = "";
    private BufferedReader fromServer;
    private BufferedWriter toServer;
    private Socket s;

    public BufferedWriter getToServer() {
        return this.toServer;
    }

    public BufferedReader getFromServer() {
        return this.fromServer;
    }

    public TCPClient() {
    }

    public TCPClient(String _host) {
        this.host = _host;
    }

    public void startConnection() {
        try {
            this.s = new Socket(this.host, 80);
            this.toServer = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream(), StandardCharsets.ISO_8859_1));
            this.fromServer = new BufferedReader(new InputStreamReader(this.s.getInputStream(), StandardCharsets.ISO_8859_1));
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void stopConnection() {
        try {
            this.s.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void writeToServer(String message) {
        try {
//            System.out.println(message);
            this.toServer.write(message);
            this.toServer.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public String readFromServer() {
        String messageToClient = "";

        try {
            for(String line = this.fromServer.readLine(); line != null; line = this.fromServer.readLine()) {
                messageToClient = messageToClient + line + "\r\n";
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

//        System.out.println(messageToClient);
        return messageToClient + "\r\n";
    }
}
