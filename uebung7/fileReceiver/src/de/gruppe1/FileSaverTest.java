package de.gruppe1;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileSaverTest {

    @Test
    void append() {
        String string = "test";
        byte[] bytes = string.getBytes();
        byte[] loooongByteArray = new byte[768];
        System.arraycopy(bytes,0,loooongByteArray, 0, bytes.length);
        String newString = new String(loooongByteArray);
        System.out.println(newString.trim());
        try {
            FileSaver.append("test123",loooongByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
