package org.example.bytebased.others;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataIOStreamDemo {
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
        DataOutputStream os = new DataOutputStream(buffer);
        os.write(127);
        os.writeByte(127);
        os.writeShort(52);
        os.writeInt(53);
        os.writeLong(53);
        os.writeFloat(10.2f);
        os.writeDouble(8.46972d);
        os.writeBoolean(true);
        os.writeChar(49);
        DataInputStream is = new DataInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        System.out.println(is.read());
        System.out.println(is.readByte());
        System.out.println(is.readShort());
        System.out.println(is.readInt());
        System.out.println(is.readLong());
        System.out.println(is.readFloat());
        System.out.println(is.readDouble());
        System.out.println(is.readBoolean());
        System.out.println(is.readChar());
    
        os.close();
        is.close();
    }
}
