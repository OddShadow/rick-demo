package org.example.others;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[1024];
        try (DataOutputStream os = new DataOutputStream(new ByteArrayOutputStream());
             DataInputStream is = new DataInputStream(new ByteArrayInputStream(buffer))) {
            os.writeInt(123);
            os.writeFloat(123.45F);
            os.writeDouble(1.8d);
            is.read();
        }
    }
}
