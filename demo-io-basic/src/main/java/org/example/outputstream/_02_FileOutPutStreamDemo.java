package org.example.outputstream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class _02_FileOutPutStreamDemo {
    public static void main(String[] args) throws IOException {
        OutputStream outputStream = new FileOutputStream("demo-io-basic/src/main/resources/files/fileoutput");
        byte[] ascii = new byte[127];
        for (byte i = 0; i < ascii.length; i++) {
            ascii[i] = i;
        }
        outputStream.write(ascii, 65, 26);
        outputStream.write(10);
        outputStream.write(ascii, 97, 26);
        outputStream.write(10);
        outputStream.write(ascii, 48, 10);
        outputStream.write(10);
        outputStream.flush();
        outputStream.close();
    }
}