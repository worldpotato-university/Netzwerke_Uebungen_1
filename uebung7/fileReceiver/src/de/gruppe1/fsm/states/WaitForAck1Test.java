package de.gruppe1.fsm.states;

import de.gruppe1.fsm.STATES;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import static org.junit.jupiter.api.Assertions.*;

class WaitForAck1Test {
    private State state0;
    File f;
    private State stateTimeout1000;

    WaitForAck1Test() {
    }

    @BeforeEach
    void setUp() {
        f = new File("File 1");
//        stateTimeout1000 = new WaitForAck1(f.getName(), 1000);
    }

    @AfterEach
    void tearDown() {
        f.delete();
    }
    @Test
    void testRunTimeoutTrue() {
        STATES currentState = null;
        // test duration of getting pkg = 1500 ms
//        stateTimeout1000.setTimeout(1);
        currentState = stateTimeout1000.run();

        assertEquals(STATES.WAIT_FOR_ACK_1, currentState);
    }

    @Test
    void testRunTimeoutFalse() {
        STATES currentState = null;
        // test duration of getting pkg = 1500 ms
//        stateTimeout1000.setTimeout(2000);
        currentState = stateTimeout1000.run();

        assertEquals(STATES.WAIT_FOR_ACK_0, currentState);
        // TODO implement test
    }

}
