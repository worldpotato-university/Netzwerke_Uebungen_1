package de.gruppe1.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.*;

public class WaitAck0 implements IState {
    @Override
    public boolean handle(StateMachine stateMachine, byte[][] data, int counter) throws IOException {
        FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() throws IOException {

                byte[] pkg = getPackage();
                boolean isValidNotCorrupted = checkPkg(pkg, 0);

                if (isValidNotCorrupted) {
                    stateMachine.increaseConter();
                    stateMachine.setState(new Wait1Above());
                }

                return null;
            }

            byte[] getPackage() throws IOException {
                byte[] pkg = new byte[Main.ACK_SIZE];
                DatagramSocket clientSocket = new DatagramSocket(Main.ACK_SIZE);

                DatagramPacket receivePacket = new DatagramPacket(pkg, pkg.length);
                clientSocket.receive(receivePacket);
                byte[] data = receivePacket.getData();
                clientSocket.close();
                return data;

            }

           byte getChecksum(byte[] blk) {
                byte sum = 0;
                for (int i = 0; i < blk.length; i++)
                    sum += blk[i];
                return sum;
            }

           boolean checkPkg(byte[] pkg, int ack) {
                byte checksumFromPkg = pkg[pkg.length - 1];
                byte calculatedChecksum = getChecksum(Arrays.copyOfRange(pkg, 0, pkg.length - 1));

                byte[] ackData = Arrays.copyOfRange(pkg, 0, pkg.length - 1);

                int ackFromPkg = fromByteArray(ackData);

                boolean isValid = checksumFromPkg == calculatedChecksum && ackFromPkg == ack;
                System.out.println("[INFO] ACK is valid: " + isValid);
                return isValid;
            }

            // packing an array of 4 bytes to an int, big endian, clean code
            private int fromByteArray(byte[] bytes) {
                return ((bytes[0] & 0xFF) << 24) |
                        ((bytes[1] & 0xFF) << 16) |
                        ((bytes[2] & 0xFF) << 8 ) |
                        ((bytes[3] & 0xFF) << 0 );
            }
        });

        Executor executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(task);

        // 1. create timer
        try {
            task.get(Main.TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            stateMachine.setState(new Wait0Above());
        } catch (Exception e) {
            e.printStackTrace();
            // TODO handle
        }
        return false;
    }

}
