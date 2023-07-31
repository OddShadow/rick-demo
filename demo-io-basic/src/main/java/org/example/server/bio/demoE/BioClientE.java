package org.example.server.bio.demoE;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BioClientE {
    public static void main(String[] args) {
        try (InputStream is = Files.newInputStream(Paths.get("demo-io-basic/src/main/resources/files/demo.png"))) {
            Socket socket = new Socket("127.0.0.1", 9999);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(".png");
            
            byte[] buffer = new byte[1024];
            int len = is.read(buffer);
            while (len != -1) {
                dos.write(buffer, 0, len);
                len = is.read(buffer);
            }
            dos.flush();
            socket.shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}