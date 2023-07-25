package org.example.others;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

public class SequenceInputStreamDemo {
    public static void main(String[] args) throws IOException {
        InputStream is1 = new FileInputStream("demo-io-basic/src/main/resources/files/read1");
        InputStream is2 = new FileInputStream("demo-io-basic/src/main/resources/files/read2");
        SequenceInputStream is = new SequenceInputStream(is2, is1);
        int data = is.read();
        while (data != -1) {
            System.out.print((char) data);
            data = is.read();
        }
    }
}
