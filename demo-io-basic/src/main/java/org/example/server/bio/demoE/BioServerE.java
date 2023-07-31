package org.example.server.bio.demoE;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class BioServerE {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                Thread aThread = new Thread(() -> {
                    try {
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        String suffix = dis.readUTF();
                        OutputStream os = Files.newOutputStream(
                                Paths.get("demo-io-basic/src/main/resources/files/demo" + UUID.randomUUID() + suffix));
                        byte[] buffer = new byte[1024];
                        int len = dis.read(buffer);
                        while (len != -1) {
                            os.write(buffer, 0, len);
                            len = dis.read(buffer);
                        }
                        os.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                aThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
