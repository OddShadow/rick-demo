package org.example.bytebased.inputstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class _02_FileInputStreamDemo {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("demo-io-basic/src/main/resources/files/FileInputStreamText");
        byte[] data = new byte[100];
        int length = inputStream.read(data);
        while (length != -1) {
            doSomething(data, length);
            length = inputStream.read(data);
        }
        inputStream.close();
    }
    
    private static void doSomething(byte[] data, int length) {
        System.out.print(new String(data, 0, length));
    }
}