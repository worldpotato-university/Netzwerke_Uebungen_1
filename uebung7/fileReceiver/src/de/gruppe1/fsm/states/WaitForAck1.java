package de.gruppe1.fsm.states;

import de.gruppe1.FileSaver;
import de.gruppe1.connection.Receiver;
import de.gruppe1.fsm.Fsm;
import de.gruppe1.fsm.STATES;

import java.io.IOException;
import java.util.concurrent.*;

public class WaitForAck1 extends State {
    public WaitForAck1(Fsm fsm) {
        super(fsm);
    }

    @Override
    public STATES run() {
        System.out.println("[INFO] Wait for ACK1");
        // returning to this state is the default. To make sure the package get sent again if something fails.
        STATES nextSTATE = STATES.WAIT_FOR_ACK_1;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<byte[]> receiver = executor.submit(new Receiver(fsm));

        try {

            // That's where the timeout enters the stage
            byte[] pkg = receiver.get(fsm.getTIMEOUT(), TimeUnit.MILLISECONDS);

            boolean isValid = checkPkg(pkg, 1);

            if (isValid) {
                boolean thisIsTheFirstPackage = fsm.getTargetFileSize() == -1
                        && fsm.getTargetFileName() == null;
                if (thisIsTheFirstPackage) {
                    setFilePropertiesFromFirstPackage(pkg);
                } else {
                    FileSaver.append(fsm.getTargetFileName(), pkg);
                }
                // everything looks good, so let's send the ack 1
                System.out.println("[INFO] WaitForAck1: Everything good. Sending ACK1");
                fsm.getAckSender().sendAck(1);
                nextSTATE = STATES.WAIT_FOR_ACK_0;
            }

        } catch (ExecutionException | IOException | TimeoutException | InterruptedException e) {
            System.out.println("[INFO] WaitForAck1: Timeout! Sending ACK0");
            // something went wrong, let's act like the transmission failed.
            receiver.cancel(true);
            // mhm, maybe the ack didn't get through. Let's send it again
            fsm.getAckSender().sendAck(0);
        }
        executor.shutdownNow();

        return nextSTATE;
    }
}
