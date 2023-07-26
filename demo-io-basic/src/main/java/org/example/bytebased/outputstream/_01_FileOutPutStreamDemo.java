package org.example.bytebased.outputstream;

import java.io.FileOutputStream;
import java.io.IOException;

public class _01_FileOutPutStreamDemo {
    public static void main(String[] args) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("demo-io-basic/src/main/resources/files/fileoutput");
        for (int i = 65; i <= 90; i++) {
            outputStream.write(i);
        }
        outputStream.write(10);
        for (int i = 97; i <= 122; i++) {
            outputStream.write(i);
        }
        outputStream.write(10);
        for (int i = 48; i <= 57; i++) {
            outputStream.write(i);
        }
        outputStream.flush();
        outputStream.close();
    }
}