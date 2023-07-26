package org.example.bytebased.inputstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class _01_FileInputStreamDemo {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("demo-io-basic/src/main/resources/files/FileInputStreamText");
        int data = inputStream.read();
        while (data != -1) {
            doSomething(data);
            data = inputStream.read();
        }
        inputStream.close();
    }
    
    private static void doSomething(int data) {
        System.out.print((char) data);
    }
}