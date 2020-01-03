package de.gruppe1.sender;

import java.io.IOException;
import java.util.concurrent.*;

public class WaitAck0 implements IState {
    @Override
    public boolean handle(StateMachine stateMachine, byte[][] data, int counter) throws IOException {
        FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() {

                // TODO wait for package
                // TODO check if is not corruped
                // TODO check for ack
                boolean isValidNotCorruped = true;

                if (isValidNotCorruped) {
                    stateMachine.increaseConter();
                    stateMachine.setState(new Wait1Above());
                }

                return null;
            }
        });

        Executor executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(task);

        // 1. create timer
        try {
            task.get(Main.TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            stateMachine.setState(new Wait0Above());
        } catch (Exception e) {
            // TODO handle
        }
        return false;
    }
}
