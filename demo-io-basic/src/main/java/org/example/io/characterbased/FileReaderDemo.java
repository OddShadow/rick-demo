package org.example.io.characterbased;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileReaderDemo {
    public static void main(String[] args) throws IOException {
        char[] chars = new char[10];
        Reader reader = new FileReader("demo-io-basic/src/main/resources/files/临江仙");
        int ch = reader.read(chars);
        while (ch != -1) {
            System.out.print(new String(chars, 0, ch));
            ch = reader.read(chars);
        }
    }
}
