package de.gruppe1.fsm.states;

import de.gruppe1.FileSaver;
import de.gruppe1.fsm.Fsm;
import de.gruppe1.fsm.STATES;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaitForAck0Test {
    File f;
    private State stateTimeout1000;
    Fsm fsm;

    WaitForAck0Test() {
    }

    @BeforeEach
    void setUp() {
        f = new File("ThisIsMyCoolTestFileName.txt");
        fsm = new Fsm("LOCALHOST", 2000, 2001,768);
        stateTimeout1000 = new WaitForAck0(fsm);
    }

    @AfterEach
    void tearDown() {
        f.delete();
    }

    @Test
    void testRunTimeoutTrue() {
        STATES currentState = null;
        // test duration of getting pkg = 1500 ms
        currentState = stateTimeout1000.run();

        assertEquals(STATES.WAIT_FOR_ACK_0, currentState);
    }

    @Test
    void testRunTimeoutFalse() {
        STATES currentState = null;
        // test duration of getting pkg = 1500 ms
//        stateTimeout1000.setTimeout(2000);
        currentState = stateTimeout1000.run();

        assertEquals(STATES.WAIT_FOR_ACK_1, currentState);
        // TODO implement test
    }

    @Test
    void testFirstPackage() {
        byte[] pkg = createFirstPackageExample();
        try {
            FileSaver.append("firstPackageExample", pkg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stateTimeout1000.setFilePropertiesFromFirstPackage(pkg);

        assertEquals(1, fsm.getTargetFileSize());
        assertEquals(f.getName(), fsm.getTargetFileName());
    }

    private byte[] createFirstPackageExample(){
        int numberOfPackage = 1;
        String nameOfFile = f.getName();

        byte[] pkg = new byte[768];
        byte[] nmbrData = toByteArray(numberOfPackage);
        System.arraycopy(nmbrData,0,pkg, 0, nmbrData.length);
        System.arraycopy(nameOfFile.getBytes(), 0, pkg, 4, nameOfFile.getBytes().length);

        return pkg;
        }
    byte[] toByteArray(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value };
    }


}
