package org.example.io.characterbased;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputStreamReaderDemo {
    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("demo-io-basic/src/main/resources/files/临江仙");
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        int data = isr.read();
        while (data != -1) {
            System.out.print((char) data);
            data = isr.read();
        }
        isr.close();
    }
}
