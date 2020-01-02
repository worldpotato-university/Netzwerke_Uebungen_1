package de.gruppe1.fsm.states;

import java.util.Arrays;
public class WaitForAck1 extends State {

    public WaitForAck1() {
    }

    @Override
    public void receivePackage(byte[] pkg) {
        System.out.println(Arrays.toString(pkg));
        System.out.println("Wait for Ack 1 - receive Package");
        // TODO implement save(pkg)
        // TODO implement send(ACK1)
    }

    @Override
    public void renewTimeout() {
        System.out.println("Wait for Ack 1 - renewTimeout");
        // TODO implement send(ACK0)
    }
}
