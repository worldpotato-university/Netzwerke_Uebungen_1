package de.gruppe1.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Wait1Above implements IState {
    @Override
    public boolean handle(StateMachine stateMachine, byte[][] data, int counter) throws IOException {
        if (counter >= data.length)
            return true;

        DatagramSocket socket = stateMachine.getSocket();

        DatagramPacket packet = new DatagramPacket(data[counter], data[counter].length, stateMachine.getIa(), stateMachine.getPort());

        socket.send(packet);

        stateMachine.setState(new WaitAck1());
        return false;
    }
}
