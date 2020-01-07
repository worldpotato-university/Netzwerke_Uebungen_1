package de.gruppe1.connection;

import de.gruppe1.fsm.Fsm;

import java.io.IOException;
import java.net.*;

public class Sender {

    private int ACK_SIZE = 5;

    private Fsm fsm;

    public Sender(Fsm fsm) {
        this.fsm = fsm;
    }

    private byte getChecksum(byte[] blk) {
        byte sum = 0;
        for (int i = 0; i < blk.length; i++)
            sum += blk[i];
        return sum;
    }

    public void sendAck(int ackNumber) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(fsm.getServerAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] ackNumberData = toByteArray(ackNumber);
            byte[] pkg = new byte[ACK_SIZE];
            byte checksum = getChecksum(ackNumberData);

            System.arraycopy(ackNumberData, 0, pkg, 0, ackNumberData.length);
            pkg[ACK_SIZE - 1] = checksum;

            DatagramPacket request = new DatagramPacket(pkg, pkg.length, address, fsm.getServerPort());
            socket.send(request);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] toByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
    }
}
