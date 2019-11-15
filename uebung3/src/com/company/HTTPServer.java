package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HTTPServer implements AutoCloseable {
    public static final int PORT = 8082;
    private ServerSocket servSock;
    private Socket s;
    private BufferedReader fromClient;
    private BufferedWriter toClient;

    public BufferedReader getFromClient() {
        return this.fromClient;
    }

    public BufferedWriter getToClient() {
        return this.toClient;
    }

    public void startServer() {
        try {
            this.servSock = new ServerSocket(8083);
            System.out.println("Server started, waiting for clients...");

            try {
                this.s = this.servSock.accept();
                this.fromClient = new BufferedReader(new InputStreamReader(this.s.getInputStream(), StandardCharsets.ISO_8859_1));
                this.toClient = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream(), StandardCharsets.ISO_8859_1));
                System.out.println("Got client connection!");
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void stopServer() {
        try {
            this.servSock.close();
            this.s.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public String readFromClient() {
        String message = "";

        try {
            for (String line = this.fromClient.readLine(); line != null && line.length() > 0; line = this.fromClient.readLine()) {
                message = message + line + "\r\n";
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return message + "\r\n";
    }

    public void writeToClient(String messageToClient) {
        try {
            this.toClient.write(messageToClient);
            this.toClient.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    @Override
    public void close() throws Exception {
        stopServer();
    }
}
