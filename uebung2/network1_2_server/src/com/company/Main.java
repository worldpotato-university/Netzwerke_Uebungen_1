package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {

        final int port = 4711;

        try (ServerSocket servSock = new ServerSocket(port)) {

            // Main loop
            while (true) {
                try (
                        Socket socket = servSock.accept();
                        InputStream inputStream = socket.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                        PrintWriter pWriter = new PrintWriter(writer)
                ) {

                    String inputPath = bufferedReader.readLine();
                    System.out.println("[INFO] Received path: " + inputPath);

                    Path path = Paths.get(inputPath);

                    long unallocated = Files.getFileStore(path).getUnallocatedSpace();
                    long total = Files.getFileStore(path).getTotalSpace();

                    pWriter.println("Info for path \"" + path + "\": " + unallocated + " bytes of " + total + " bytes free");
                    pWriter.flush();

                } catch (FileSystemException e) {
                    System.err.println("There is a problem with the Path");
                } catch (Exception e) {
                    System.err.println("There was a problem");
                }
            }
        } catch (IOException e) {
            System.err.println("There was a problem with the connection");
        }
    }
}
