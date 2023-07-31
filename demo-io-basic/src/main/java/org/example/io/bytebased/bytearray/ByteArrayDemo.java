package org.example.io.bytebased.bytearray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayDemo {
    public static void main(String[] args) throws IOException {
        byte[] bytes = "0123456789".getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        for (int i = 0; i < 2; i++) {
            int read = is.read();
            System.out.print((char) read);
        }
        System.out.println();
        System.out.println("跳过了" + is.skip(1) + "字节");
        System.out.println("还剩" + is.available());
        is.mark(2);
        System.out.println("还剩" + is.available());
        for (int i = 0; i < 4; i++) {
            int read = is.read();
            System.out.print((char) read);
        }
        System.out.println();
        System.out.println("还剩" + is.available());
        is.reset();
        System.out.println("还剩" + is.available());
        for (int i = 0; i < 4; i++) {
            int read = is.read();
            System.out.print((char) read);
        }
        System.out.println();
        System.out.println("还剩" + is.available());

        int read = is.read();
        while (read != -1) {
            System.out.print((char) read);
            read = is.read();
        }

        is.close();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(32);
        byte[] b = os.toByteArray();
        os.close();

    }
}
