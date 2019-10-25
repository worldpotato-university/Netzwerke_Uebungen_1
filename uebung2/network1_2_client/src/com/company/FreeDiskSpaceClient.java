package com.company;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;

public class FreeDiskSpaceClient {
    public static void main(String[] args) {

        final int port = 4711;
        String server = null;
        String path = null;

        try {
            if (args.length != 2) {
                throw new InputMismatchException("Wrong Number of Arguments");
            }

            server = args[0];
            path = args[1];

        } catch (InputMismatchException e) {

            System.err.println("Wrong number of Arguments! Should be 2.");
            System.exit(2);

        } catch (Exception e) {
            System.err.println("Something went wrong");
        }

        System.out.println("[INFO] Attempt to connect to " + server);

        try (Socket socket = new Socket(server, port)) {

            // Input reader
            InputStream in = socket.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(in));

            // Output writer
            OutputStream out = socket.getOutputStream();
            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(out));

            System.out.println("[INFO] Send path: " + path);
            // Send path to connected server
            pWriter.println(path);
            pWriter.flush();

            // Getting the response and print it to STDOUT
            String answer = bReader.readLine();
            if (answer == null || answer.isEmpty()) {
                System.err.println("There was a problem at the server");
            } else {
                System.out.println(answer);
            }

        } catch (UnknownHostException e) {
            System.err.println("Host is unknown.");
        } catch (IOException e) {
            System.err.println("Connection failed.");
        } catch (Exception e) {
            System.err.println("Something went wrong");
        }
    }
}
