package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public interface ITCPClient {
    String host = "";

    BufferedWriter getToServer();

    BufferedReader getFromServer();

    void startConnection();

    void stopConnection();

    void writeToServer(String message);

    String readFromServer();
}
