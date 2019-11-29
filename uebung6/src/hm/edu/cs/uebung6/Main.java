package hm.edu.cs.uebung6;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final int PACKET_SIZE = 1400;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                loop(scanner);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loop(Scanner scanner) throws IOException, InterruptedException {
        System.out.println("============================Menu=================================== \n" +
                "|++++++++++++++Bitte die gewuenschte Option waehlen.++++++++++++++| \n" +
                "| UDP                                                             | \n" +
                "| TCP                                                             | \n" +
                "|=================================================================|");
        String input = scanner.next();
        input = input.toUpperCase();

        switch (input) {
            case "TCP":
                new TCP(PACKET_SIZE).execute(scanner);
                break;
            case "UDP":
                new UDP(PACKET_SIZE).execute(scanner);
                break;
            case "exit":
                System.out.print("Beenden");
                return;
            default:
                System.out.println("Unbekannte Eingabe: Bitte 'UDP' oder 'TCP' eingeben!");
        }
    }
}
