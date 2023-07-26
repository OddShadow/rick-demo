package org.example.characterbased;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class LineNumberReaderDemo {
    public static void main(String[] args) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader("demo-io-basic/src/main/resources/files/临江仙"));
        int data = reader.read();
        while (data != -1) {
            System.out.print((char) data);
            data = reader.read();
        }
        reader.close();
    }
}
