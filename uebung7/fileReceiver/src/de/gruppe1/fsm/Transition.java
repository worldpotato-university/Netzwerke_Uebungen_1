package de.gruppe1.fsm;

import java.util.ArrayList;
import java.util.List;

class Transition {
    STATES from;
    STATES to;
    ACTIONS action;

    private Transition(STATES from, STATES to, ACTIONS action) {
        this.from = from;
        this.to = to;
        this.action = action;
    }

    static List<Transition> getList() {
        List<Transition> transitions = new ArrayList<>();

        transitions.add(new Transition(STATES.WAIT_FOR_ACK_0, STATES.WAIT_FOR_ACK_1, ACTIONS.RECEIVE_PACKAGE));
        transitions.add(new Transition(STATES.WAIT_FOR_ACK_1, STATES.WAIT_FOR_ACK_0, ACTIONS.RECEIVE_PACKAGE));

        transitions.add(new Transition(STATES.WAIT_FOR_ACK_0, STATES.WAIT_FOR_ACK_0, ACTIONS.SEND_ACK_AFTER_TIMEOUT));
        transitions.add(new Transition(STATES.WAIT_FOR_ACK_1, STATES.WAIT_FOR_ACK_1, ACTIONS.SEND_ACK_AFTER_TIMEOUT));

        return transitions;
    }
}
