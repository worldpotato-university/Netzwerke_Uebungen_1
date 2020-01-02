package de.gruppe1.fsm.states;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaitForAck0Test {
    private State state;

    WaitForAck0Test() {
       state = new WaitForAck0();
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
