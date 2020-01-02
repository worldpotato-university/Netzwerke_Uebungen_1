package de.gruppe1.fsm.states;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitForAck1Test {
    private State state;

    WaitForAck1Test() {
       state = new WaitForAck1();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testReceivePackage() {
        // TODO implement test
    }

    @Test
    void testRenewTimeout() {
        // TODO implement test
    }
}
