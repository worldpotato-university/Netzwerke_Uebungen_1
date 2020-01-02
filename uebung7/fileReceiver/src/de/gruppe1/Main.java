package de.gruppe1;

import de.gruppe1.fsm.Fsm;

/**
 * Wird ohne Parameter aufgerufen und wartet auf eingehende
 * Dateiübertragungen. Empfangene Dateien werden lokal gespeichert. Nach dem Empfang einer
 * Datei bleibt das Programm weiter aktiv und wartet auf die nächste eingehende Datenverbindung
 */
public class Main {

    public static void main(String[] args) {
        Fsm fsm = new Fsm();
        fsm.receivePackage("42".getBytes());
        fsm.renewTimeout();
        fsm.receivePackage("23".getBytes());

        fsm.renewTimeout();
        fsm.renewTimeout();

    }
}
