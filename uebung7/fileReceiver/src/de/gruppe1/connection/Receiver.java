package de.gruppe1.connection;

import de.gruppe1.fsm.Fsm;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;

public class Receiver implements Callable<byte[]> {
    private int listenPort;
    private int packageSize;

    public Receiver(Fsm fsm) {
        this.listenPort = fsm.getListenPort();
        this.packageSize = fsm.getPackageSize();
    }

    private byte[] getPackage() throws IOException {
        DatagramSocket clientSocket = new DatagramSocket(listenPort);

        byte[] receiveData = new byte[packageSize];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        byte[] data = receivePacket.getData();
        clientSocket.close();
        return data;
    }

    @Override
    public byte[] call() throws IOException {
        System.out.println("[INFO] Receiver get Package");
        return getPackage();
    }
}
