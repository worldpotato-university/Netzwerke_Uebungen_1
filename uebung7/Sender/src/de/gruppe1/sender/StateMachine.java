package de.gruppe1.sender;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class StateMachine {
    private IState _state;
    private byte[][] _data;
    private DatagramSocket _socket;
    private InetAddress _ia;
    private int _port;
    private int _counter;

    public StateMachine(byte[][] data, InetAddress ia, int port) {
        _data = data;
        _ia = ia;
        _port = port;
    }

    public void run() throws IOException {
        _socket = new DatagramSocket();
        _counter = 0;

        _state = new Wait0Above();
        // Initialize State

        while (!_state.handle(this, _data, _counter)) {
        }
    }

    public DatagramSocket getSocket() {
        return _socket;
    }

    public InetAddress getIa() {
        return _ia;
    }

    public int getPort() {
        return _port;
    }

    public void setState(IState state){
        _state = state;
    }

    public void increaseConter(){
        _counter++;
    }
}
