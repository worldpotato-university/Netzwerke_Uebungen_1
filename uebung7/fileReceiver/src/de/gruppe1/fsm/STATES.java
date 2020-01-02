package de.gruppe1.fsm;

public enum STATES {
    WAIT_FOR_ACK_0(0), WAIT_FOR_ACK_1(1);

    int value;

    STATES(int value){
        this.value = value;
    }
}
