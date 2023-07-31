package org.example.io.bytebased.others;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class PushbackInputStreamDemo {
    public static void main(String[] args) throws IOException {
        PushbackInputStream is = new PushbackInputStream(new ByteArrayInputStream(new byte[]{0,1,2,3,4,5,6,7,8,9}));
        System.out.print(is.read());
        is.unread(5);
        int read = is.read();
        while (read != -1) {
            System.out.print(read);
            read = is.read();
        }
        is.close();
    }
}
