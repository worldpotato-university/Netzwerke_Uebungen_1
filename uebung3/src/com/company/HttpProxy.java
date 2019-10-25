package com.company;

import java.io.IOException;

public class HttpProxy {
    private static String host = "";

    public static void main(String[] args) throws IOException {
        host = "mmix.cs.hm.edu";
        HTTPServer httpServer = new HTTPServer();
        TCPClient tcpClient = new TCPClient(host);

        while (true) {
            httpServer.startServer();
            tcpClient.startConnection();
            String messageToServer = httpServer.readFromClient();
            tcpClient.writeToServer(changeAcceptEncoding(changeHost(messageToServer)));
            String messageFromServer = tcpClient.readFromServer();
            httpServer.writeToClient(replaceMessageLength(replaceImage(makeHappy(messageFromServer))));
            tcpClient.stopConnection();
            httpServer.stopServer();
        }
    }

    private static String changeHost(String s) {
        return s.replaceAll("Host.*", "Host: " + host);
    }

    private static String changeAcceptEncoding(String s) {
        return s.replaceAll("Accept-Encoding.*", "Accept-Encoding: identity");
    }

    private static String makeHappy(String s) {

        String additionalString = " (yeah!)";

        return s.replaceAll("MMIX", "MMIX".concat(additionalString))
                .replaceAll("Java", "Java".concat(additionalString))
                .replaceAll("Computer", "Computer".concat(additionalString))
                .replaceAll("RISC", "RISC".concat(additionalString))
                .replaceAll("CISC", "CISC".concat(additionalString))
                .replaceAll("Debugger", "Debugger".concat(additionalString))
                .replaceAll("Informatik", "Informatik".concat(additionalString))
                .replaceAll("Student", "Studnet".concat(additionalString))
                .replaceAll("Studentin", "Studentin".concat(additionalString))
                .replaceAll("Studierende", "Studierende".concat(additionalString))
                .replaceAll("Windows", "Windows".concat(additionalString))
                .replaceAll("Linux", "Linux").concat(additionalString);
    }

    private static String replaceImage(String s) {
        return s.replaceAll("(<img)(.+)(>)", "<img src=\"https://upload.wikimedia.org/wikipedia/commons/8/8d/Smiley_head_happy.svg\"/>");
    }

    private static String replaceMessageLength(String s) {
        int length = s.length();
        return s.replace("Content-Length.*", "Content-Length: ".concat(Integer.toString(length)));
    }
}