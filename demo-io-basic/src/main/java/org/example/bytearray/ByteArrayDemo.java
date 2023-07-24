package org.example.bytearray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayDemo {
    public static void main(String[] args) throws IOException {
        byte[] bytes = "this is a good boy!".getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        int read = is.read();
        while (is.available() != 0) {
            System.out.print((char) read);
            read = is.read();
        }
        System.out.println((char) read);
        System.out.println(" ");
    }
}
