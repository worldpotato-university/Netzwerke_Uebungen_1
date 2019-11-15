package com.company;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClientSSL implements ITCPClient {
    private String host;
    private BufferedReader fromServer;
    private BufferedWriter toServer;
    private SSLSocket s;

    public BufferedWriter getToServer() {
        return this.toServer;
    }

    public BufferedReader getFromServer() {
        return this.fromServer;
    }

    public TCPClientSSL() {
    }

    public TCPClientSSL(String _host) {
        this.host = _host;
    }

    public void startConnection() {
        try {

            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            s = (SSLSocket) factory.createSocket(host, 443);
            s.startHandshake();

            this.toServer = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream(), StandardCharsets.ISO_8859_1));
            this.fromServer = new BufferedReader(new InputStreamReader(this.s.getInputStream(),StandardCharsets.ISO_8859_1));

        } catch (IOException var2) {
            var2.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            for (String line = this.fromServer.readLine(); line != null; line = this.fromServer.readLine()) {
                messageToClient = messageToClient + line + "\r\n";
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

//        System.out.println(messageToClient);
        return messageToClient + "\r\n";
    }
}
