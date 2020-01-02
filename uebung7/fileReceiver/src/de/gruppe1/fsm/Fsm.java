package de.gruppe1.fsm;

import de.gruppe1.fsm.states.*;

import java.util.ArrayList;
import java.util.List;

public class Fsm {

//    private int maxRetries = 20;
//    private int ackTimeout = 500; // ms

    private List<State> states;
    private List<Transition> transitions;
    private STATES currentState = STATES.WAIT_FOR_ACK_0;

    public Fsm() {
        states = new ArrayList<>();
        states.add(new WaitForAck0());
        states.add(new WaitForAck1());

        transitions = Transition.getList();
    }

    public void receivePackage(byte[] pkg) {
        states.get(currentState.value).receivePackage(pkg);
        next(ACTIONS.RECEIVE_PACKAGE);
        // TODO implement send(ACK0)
    }

    public void renewTimeout() {
        states.get(currentState.value).renewTimeout();
        // TODO implement send(ACK1)
        next(ACTIONS.SEND_ACK_AFTER_TIMEOUT);
    }

    /**
     * Sets the next state dependent on the current state and the given action.
     * It's using the transitions list as a sort of lookup table.
     * @param action which is done with the current state.
     */
    private void next(ACTIONS action) {
        STATES newState = null;
        for (Transition transition :
             transitions) {
            if (transition.from == currentState && transition.action == action) {
                newState = transition.to;
            }
        }
        if (newState != null) {
            System.out.println("New State: " + newState.toString());
        }
        currentState = newState;
    }
}
