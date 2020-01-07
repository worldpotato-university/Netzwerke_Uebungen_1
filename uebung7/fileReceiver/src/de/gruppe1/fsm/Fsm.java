package de.gruppe1.fsm;

import de.gruppe1.connection.Sender;
import de.gruppe1.fsm.states.State;
import de.gruppe1.fsm.states.WaitForAck0;
import de.gruppe1.fsm.states.WaitForAck1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Fsm {

    private int TIMEOUT = 5000;

    private STATES currentState = STATES.WAIT_FOR_ACK_0;

    private List<State> states;
    private long targetFileSize = -1;
    private String targetFileName;

    private String serverAddress;
    private int serverPort;
    private int listenPort;
    private int packageSize;

    private Sender ackSender;
    /**
     * Default constructor to create the FSM object.
     */
    public Fsm(String serverAddress, int serverPort, int listenPort, int packageSize) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.listenPort = listenPort;
        this.packageSize = packageSize;

        states = new ArrayList<>();
        states.add(new WaitForAck0(this));
        states.add(new WaitForAck1(this));

        ackSender = new Sender(this);
    }

    public void run() {
        if (targetFileSize == -1 && targetFileName == null) {
        }
        while (currentState != STATES.FINISHED) {
            State nextStateToRun = states.get(currentState.value);
            currentState = nextStateToRun.run();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(targetFileSize != -1
                    && targetFileName != null) {
                long missingBytes = missingBytes();
                if (missingBytes == 0) {
                    currentState = STATES.FINISHED;
                    System.out.println("[INFO] Transmission finished");
                }
            }
        }
    }

    protected long missingBytes() {
        File f = new File(targetFileName);
        return targetFileSize - f.length();
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public long getTargetFileSize() {
        return targetFileSize;
    }

    public void setTargetFileSize(long targetFileSize) {
        this.targetFileSize = targetFileSize;
    }

    public int getTIMEOUT() {
        return TIMEOUT;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getListenPort() {
        return listenPort;
    }

    public int getPackageSize() {
        return packageSize;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Sender getAckSender() {
        return ackSender;
    }
}
