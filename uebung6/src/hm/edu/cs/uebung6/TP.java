package hm.edu.cs.uebung6;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public abstract class TP {
    protected int _packetSize;

    public TP(int packetSize) {
        _packetSize = packetSize;
    }

    public void execute(Scanner scanner) throws IOException {
        System.out.println("============================Menu=================================== \n" +
                "|++++++++++++++Bitte die gewuenschte Option waehlen.++++++++++++++| \n" +
                "| Client                                                          | \n" +
                "| Server                                                          | \n" +
                "|=================================================================|");
        String input = scanner.next();
        input = input.toUpperCase();

        switch (input) {
            case "CLIENT":
                executeClient(scanner);
                break;
            case "SERVER":
                executeServer(scanner);
                break;
            default:
                System.out.println("Unbekannte Eingabe!");
                break;
        }
    }

    abstract void executeServer(Scanner scanner) throws IOException;
    abstract void executeClient(Scanner scanner) throws IOException;
}
