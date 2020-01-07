package de.gruppe1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSaver {

    public static void append(String targetFileName, byte[] data) throws IOException {
        File f = new File(targetFileName);
        if (!f.exists() && f.isDirectory()) {
            f.createNewFile();
        }
        try (FileOutputStream output = new FileOutputStream(targetFileName, true)) {
            output.write(data);
        }
    }
}
