package org.example.inputstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileInputStreamDemo {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("FileInputStreamText-io-basic/src/main/resources/files/FileInputStreamText");
        int data = inputStream.read();
        while (data != -1) {
            data = inputStream.read();
        }
        inputStream.close();
    }
}