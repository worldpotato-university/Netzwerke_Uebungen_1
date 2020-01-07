package de.gruppe1.fsm;

import de.gruppe1.FileSaver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FsmTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void run() {

    }

    @Test
    void missingBytesTest() {
        File f = new File("test");
        f.delete();
        Fsm fsm = new Fsm("Localhost", 2000, 2001, 768);
        byte[] pkg = "42".getBytes();

        fsm.setTargetFileSize(pkg.length * 2);
        fsm.setTargetFileName("test");
        try {
            FileSaver.append("test", pkg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(pkg.length, fsm.missingBytes());

    }

}
