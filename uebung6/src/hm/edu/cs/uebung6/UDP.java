package hm.edu.cs.uebung6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
        DatagramSocket socket = new DatagramSocket(20000);
        socket.setSoTimeout(10000);
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[_packetSize], _packetSize);
            try {
                socket.receive(packet);
                counter++;
                if (start == null)
                    start = new Date();
                stop = new Date();
            } catch (SocketTimeoutException ex) {
                break;
            }
        }
        if (start == null || stop == null)
            System.out.println("Konnte keine Pakete empfangen!");
        else {
            long millis = Math.abs(stop.getTime() - start.getTime());
            System.out.println("Es wurden " + counter + " Pakete empfangen in " + millis + "ms");
        }

    }

    @Override
    void executeClient(Scanner scanner) throws IOException {
        System.out.print("Sendezeit in Sekunden: ");
        String inputTime = scanner.next();
        System.out.println();

        System.out.print("Pause nach jedem N-ten Paket in Anzahl der Paketen. N: ");
        String inputN = scanner.next();
        System.out.println();

        System.out.print("Pause nach jedem N-ten Paket in ms. k: ");
        String inputK = scanner.next();
        System.out.println();

        int time, N, K;
        try {
            time = Integer.parseInt(inputTime);
            N = Integer.parseInt(inputN);
            K = Integer.parseInt(inputK);
        } catch (NumberFormatException ex) {
            System.out.println("Die Eingaben müssen ganze Zahjlen sein.");
            return;
        }

        // TODO send packets
    }
}
