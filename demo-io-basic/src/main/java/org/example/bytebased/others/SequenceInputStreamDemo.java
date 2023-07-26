package org.example.bytebased.others;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

public class SequenceInputStreamDemo {
    public static void main(String[] args) throws IOException {
        InputStream is1 = new ByteArrayInputStream(new byte[]{'0','1','2','3','4','5','6','7','8','9'});
        InputStream is2 = new ByteArrayInputStream(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'});
        SequenceInputStream is = new SequenceInputStream(is2, is1);
        int data = is.read();
        while (data != -1) {
            System.out.print((char) data);
            data = is.read();
        }
    }
}
