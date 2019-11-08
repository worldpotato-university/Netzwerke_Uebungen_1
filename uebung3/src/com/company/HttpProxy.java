package com.company;

import java.io.IOException;
import java.util.Scanner;

public class HttpProxy {
    private static String host = "";

    public static void main(String[] args) throws IOException {
        ITCPClient tcpClient;

        boolean useTLS = !(args.length == 0) && args[0].matches("TLS");


        if (useTLS) {
            host = "blog.fefe.de";
            tcpClient = new TCPClientSSL(host);
        } else {
            host = "mmix.cs.hm.edu";
            tcpClient = new TCPClient(host);
        }

        while (true) {
            try (HTTPServer httpServer = new HTTPServer()) {
                httpServer.startServer();
                tcpClient.startConnection();

                String messageToServer = httpServer.readFromClient();
                tcpClient.writeToServer(changeAcceptEncoding(changeHost(messageToServer)));
                String messageFromServer = tcpClient.readFromServer();
//            System.out.println("[INFO] ------------------ OLD MESSAGE ------------------");
//            System.out.print(messageFromServer);
//            System.out.println("[INFO] --------------------- END OLD MESSAGE ----------------");

                String out = replaceMessageLength(replaceImage(makeHappy(messageFromServer)));
//            System.out.println("--------------- NEW MESSAGE --------------");
//            System.out.println(out);

                httpServer.writeToClient(out);
                tcpClient.stopConnection();
                httpServer.stopServer();

            } catch (Exception e) {
                e.printStackTrace();

            }
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
                .replaceAll("Linux", "Linux".concat(additionalString));
    }

    private static String replaceImage(String s) {
        return s.replaceAll("(<img)(.+)(>)", "<img src=\"http://upload.wikimedia.org/wikipedia/commons/8/8d/Smiley_head_happy.svg\"/>");
    }

    private static String replaceMessageLength(String s) {

        String message = "";
        Scanner scanner = new Scanner(s);
        boolean isMessage = false;
        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            if (isMessage) {
                message = message.concat(line).concat("\r\n");
            }
            if (line.isEmpty()) isMessage = true;
        }

        int length1 = message.getBytes().length;
        int length = message.length();
        String out = s.replaceAll("Content-Length.*", "Content-Length: " + length1);


        return out;
    }
}
