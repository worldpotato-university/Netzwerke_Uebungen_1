package de.gruppe1;

import de.gruppe1.fsm.Fsm;

/**
 * Wird ohne Parameter aufgerufen und wartet auf eingehende
 * Dateiübertragungen. Empfangene Dateien werden lokal gespeichert. Nach dem Empfang einer
 * Datei bleibt das Programm weiter aktiv und wartet auf die nächste eingehende Datenverbindung
 */
public class Main {

    private static final int PACKAGE_SIZE = 768;
    private static final int LISTEN_PORT = 2001;
    private static final int SERVER_PORT = 2000;
    private static final String SERVER_ADDRESS = "LOCALHOST";

    public static void main(String[] args) {
        while (true) {
            Fsm fsm = new Fsm(SERVER_ADDRESS, SERVER_PORT, LISTEN_PORT, PACKAGE_SIZE);
            fsm.run();
            System.out.println("[INFO] Finite State machine stopped");
        }
    }


}
