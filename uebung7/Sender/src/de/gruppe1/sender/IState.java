package de.gruppe1.sender;

import java.io.IOException;

public interface IState {
    boolean handle(StateMachine stateMachine, byte[][] data, int counter) throws IOException;
}
