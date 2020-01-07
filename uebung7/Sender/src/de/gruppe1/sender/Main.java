package de.gruppe1.sender;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static final int TIMEOUT = 1000; // in ms
    public static final int PACKAGE_SIZE = 768;
    public static final int ACK_SIZE = 5;
    public static final int INPUT_PORT = 2000;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: [filePath] [ip] [port]");
            System.exit(-1);
        }

        InetAddress ia = null;
        try {
            ia = InetAddress.getByName(args[1]);
        } catch (UnknownHostException e) {
            System.out.println(args[1] + " is not a valid ip address.");
            System.exit(-1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isFile()) {
            System.out.println(args[0] + " is not a valid file.");
            System.exit(-1);
        }

        int port = 0;
        try {
            port = Integer.parseInt(args[2]);
            if(port < 0 || port > 65335){
                System.out.println(args[0] + " is not a valid port.");
                System.exit(-1);
            }
        } catch (Exception e) {
            System.out.println(args[0] + " is not a valid port.");
            System.exit(-1);
        }

        System.out.println("File Path: " + args[0]);
        System.out.println("IP: " + args[1]);
        System.out.println("Port: " + args[2]);

        byte[] data = null;
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            System.out.println("Colud not read the file");
            System.exit(-1);
        }

        List<byte[]> packages = new ArrayList<>();
        for (int i = 0; i < data.length; i += Main.PACKAGE_SIZE - 1) {
            byte[] pack1 = Arrays.copyOfRange(data, 0, Main.PACKAGE_SIZE - 1);
            byte checksum = getChecksum(pack1);

            byte[] pack = new byte[Main.PACKAGE_SIZE];
            System.arraycopy(pack1, 0, pack, 0, pack1.length);
            pack[Main.PACKAGE_SIZE - 1] = checksum;
            packages.add(pack);
        }

        StateMachine stateMachine = new StateMachine(packages.toArray(new byte[packages.size()][]), ia, port);
        stateMachine.run();
    }

    private static byte getChecksum(byte[] blk) {
        byte sum = 0;
        for (int i = 0; i < blk.length; i++)
            sum += blk[i];
        return sum;
    }
}
