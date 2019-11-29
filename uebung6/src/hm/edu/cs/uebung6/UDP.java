package hm.edu.cs.uebung6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.Period;
import java.util.Date;
import java.util.Scanner;

public class UDP extends TP {
    public UDP(int packetSize) {
        super(packetSize);
    }

    @Override
    void executeServer(Scanner scanner) throws IOException {
        int counter = 0;
        Date start = null;
        Date stop = null;
        try (DatagramSocket socket = new DatagramSocket(20000)) {
            socket.setSoTimeout(10000);
            byte[] buffer = new byte[_packetSize];
            try {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, _packetSize);
                    socket.receive(packet);
                    counter++;
                    if (start == null)
                        start = new Date();
                    stop = new Date();
                }
            } catch (SocketTimeoutException ex) {
            }
            if (start == null || stop == null)
                System.out.println("Konnte keine Pakete empfangen!");
            else {
                long millis = Math.abs(stop.getTime() - start.getTime());
                System.out.println("Es wurden " + counter + " Pakete empfangen in " + millis + " ms");
                System.out.println("Empfangsrate: " + (counter / (millis / 1000.0)) + " Pakete/s");
                System.out.println("Goodput: " + (counter / (millis / 1000.0) * _packetSize) + " Byte/s");
                // UDP HEADER 8 Byte + IPv4 HEADER 20 Byte
                System.out.println("Troughtput: " + (counter / (millis / 1000.0) * (_packetSize + 28)) + " Byte/s");
            }
        }
    }

    @Override
    void executeClient(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Anzahl der Pakete: ");
        String inputPackets = scanner.next();
        System.out.println();

        System.out.print("Pause nach jedem N-ten Paket in Anzahl der Paketen. N: ");
        String inputN = scanner.next();
        System.out.println();

        System.out.print("Pause nach jedem N-ten Paket in ms. k: ");
        String inputK = scanner.next();
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

        Date start, stop;
        byte[] data = new byte[_packetSize];
        try (DatagramSocket toSocket = new DatagramSocket()) {
            start = new Date();
            for (int i = 0; i < packets; i++) {
                DatagramPacket packet = new DatagramPacket(data, _packetSize, InetAddress.getByName("127.0.0.1"), 20000);
                toSocket.send(packet);
                if ((i + 1) % N == 0) {
                    Thread.sleep(K);
                }
            }
            stop = new Date();
        }
        long millis = Math.abs(stop.getTime() - start.getTime());
        System.out.println("Es wurden " + packets + " Pakete gesendet in " + millis + " ms");
        System.out.println("Senderate: " + (packets / (millis / 1000.0)) + " Pakete/s");
        System.out.println("Goodput: " + (packets / (millis / 1000.0) * _packetSize) + " Byte/s");
        // UDP HEADER 8 Byte + IPv4 HEADER 20 Byte
        System.out.println("Troughtput: " + (packets / (millis / 1000.0) * (_packetSize + 28)) + " Byte/s");
    }
}
