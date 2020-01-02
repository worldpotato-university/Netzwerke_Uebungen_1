package de.gruppe1.fsm.states;

public abstract class State {
    State() {
    }

    public abstract void receivePackage(byte[] pkg);

    public abstract void renewTimeout();

}

