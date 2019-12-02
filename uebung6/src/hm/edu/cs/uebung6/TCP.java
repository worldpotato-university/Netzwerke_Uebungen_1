package hm.edu.cs.uebung6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Scanner;

public class TCP extends TP {

    Date startServer;
    Date stopServer;

    Date startClient;
    Date stopClient;

    byte[] dataArray = new byte[_packetSize];

    int counter = 0;
    int timeout = 5000;


    public TCP(int packetSize) {
        super(packetSize);
    }

    @Override
    void executeServer(Scanner scanner) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(6789);
        welcomeSocket.setSoTimeout(timeout);

        try (
                Socket connectionSocket = welcomeSocket.accept();
                InputStream inFromClient =
                        connectionSocket.getInputStream();
        ) {

            while (true) {
                inFromClient.available();
                if (inFromClient.read(dataArray, 0, _packetSize) == -1)
                    break; // read the message

                if (startServer == null)
                    startServer = new Date();
                stopServer = new Date();
                counter++;
            }
        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout!!");
        } catch (EOFException ex) {
            ex.printStackTrace();
        }

        if (startServer == null || stopServer == null)
            System.out.println("Konnte keine Pakete empfangen!");
        else {
            long millis = Math.abs(stopServer.getTime() - startServer.getTime());
            System.out.println("Es wurden " + counter + " Pakete empfangen in " + millis + " ms");
            System.out.println("Empfangsrate: " + (counter / (millis / 1000.0)) + " Pakete/s");
            System.out.println("Goodput: " + (counter / (millis / 1000.0) * _packetSize) + " Byte/s");
            // TCP HEADER 20 Byte + IPv4 HEADER 20 Byte
            System.out.println("Troughtput: " + (counter / (millis / 1000.0) * (_packetSize + 40)) + " Byte/s");
        }
        welcomeSocket.close();
    }

    @Override
    void executeClient(Scanner scanner) throws IOException {

        System.out.print("Anzahl der Pakete: ");
        String inputPackets = scanner.next();
        System.out.println();

        System.out.print("Pause nach jedem N-ten Paket in Anzahl der Paketen. N: ");
        String inputN = scanner.next();
        System.out.println();

        System.out.print("Pause nach jedem N-ten Paket in ms. k: ");
        String inputK = scanner.next();
        System.out.println();

        System.out.print("Host: ");
        String inputHost = scanner.next();
        System.out.println();

        int packets, N, K;
        try {
            packets = Integer.parseInt(inputPackets);
            N = Integer.parseInt(inputN);
            K = Integer.parseInt(inputK);
        } catch (NumberFormatException ex) {
            System.out.println("Die Eingaben müssen ganze Zahlen sein.");
            return;
        }

        try (
                Socket clientSocket = new Socket(inputHost, 6789);
                OutputStream outToServer = clientSocket.getOutputStream();
        ) {

            startClient = new Date();
            for (int i = 0; i < packets; i++) {
                stopClient = new Date();
                outToServer.write(dataArray);
                outToServer.flush();
                if ((i + 1) % N == 0) {
                    Thread.sleep(K);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long millis = Math.abs(stopClient.getTime() - startClient.getTime());
        System.out.println("Es wurden " + packets + " Pakete gesendet in " + millis + " ms");
        System.out.println("Senderate: " + (packets / (millis / 1000.0)) + " Pakete/s");
        System.out.println("Goodput: " + (packets / (millis / 1000.0) * _packetSize) + " Byte/s");
        // UDP HEADER 8 Byte + IPv4 HEADER 20 Byte
        System.out.println("Troughput: " + (packets / (millis / 1000.0) * (_packetSize + 28)) + " Byte/s");

    }
}
