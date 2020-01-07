package de.gruppe1.fsm.states;

import de.gruppe1.fsm.Fsm;
import de.gruppe1.fsm.STATES;
import de.gruppe1.fsm.Transition;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class State {

    Fsm fsm = null;

    State(Fsm fsm) {
        this.fsm = fsm;
    }

    public abstract STATES run();

    static byte getChecksum(byte[] blk) {
        byte sum = 0;
        for (int i = 0; i < blk.length; i++)
            sum += blk[i];
        return sum;
    }

    boolean checkPkg(byte[] pkg, int ack) {
        byte checksumFromPkg = pkg[pkg.length -1];
        byte calculatedChecksum = getChecksum(Arrays.copyOfRange(pkg, 0, pkg.length - 1));

        byte[] ackData = Arrays.copyOfRange(pkg, pkg.length - 5, pkg.length - 1);

        int ackFromPkg = fromByteArray(ackData);

        boolean isValid = checksumFromPkg == calculatedChecksum && ackFromPkg == ack;
        System.out.println("[INFO] Package is valid: " + isValid);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    void setFilePropertiesFromFirstPackage(byte[] pkg) {
        /* first packages are like:
        4 byte: fileSizeData
        400 byte: fileNameData
         */
        byte[] fileSizeData = Arrays.copyOfRange(pkg, 0, 4);
        byte[] fileNameData = Arrays.copyOfRange(pkg, 4, 404); // 400 byte for the name String

        int fileSize = fromByteArray(fileSizeData);
        String rawName = new String(fileNameData);
        String fileName = rawName.trim();

        System.out.println("[INFO] New file with name: " + fileName);
        System.out.println("[INFO] New file with size: " + fileSize);

        fsm.setTargetFileSize(fileSize);
        fsm.setTargetFileName(fileName);

    }

    // packing an array of 4 bytes to an int, big endian, clean code
    private int fromByteArray(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8 ) |
                ((bytes[3] & 0xFF) << 0 );
    }
}

