package com.company;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        List<String> lineList = safeInputLinesInList();
        printLog(lineList);
    }

    private static List<String> safeInputLinesInList() {
        List<String> inputList = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        String inputString;
        do {
            inputString = scanner.nextLine();
            inputList.add(inputString);
        } while (!inputString.isEmpty());

        return inputList;
    }

    private static void printLog(List<String> loggedLines) {
        printLogMessages(loggedLines);
        printEndString();
    }

    private static void printLogMessages(List<String> loggedLines) {
        int lineNumber = 1;
        for (String line : loggedLines) {
            printSingleLogLine(lineNumber, line);
            lineNumber++;
        }
    }

    private static void printSingleLogLine(int lineNumber, String message) {
        System.out.println(createPrefix(lineNumber) + message);
    }

    private static String createPrefix(int lineNumber) {
        String prePrefix = lineNumber > 9 ? "[" : "[0";
        String postPrefix = "] ";
        return prePrefix + lineNumber + postPrefix;
    }

    private static void printEndString() {
        System.out.println(createEndString());
    }

    private static String createEndString() {
        return "Aufgezeichnet am " + new Date().toString();
    }
}
