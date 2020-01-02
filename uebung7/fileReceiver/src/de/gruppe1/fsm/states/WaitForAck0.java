package de.gruppe1.fsm.states;

import java.util.Arrays;

public class WaitForAck0 extends State {

    public WaitForAck0() {
    }

    @Override
    public void receivePackage(byte[] pkg) {
        System.out.println(Arrays.toString(pkg));
        System.out.println("Wait for Ack 0 - receive Package");
        // TODO implement save(pkg)
        // TODO implement send(ACK0)
    }

    @Override
    public void renewTimeout() {
        System.out.println("Wait for Ack 0 - renewTimeout");
        // TODO implement send(ACK1)
    }
}
