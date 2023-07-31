package org.example.server.bio.demoA;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class BioClientA {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            OutputStream os = socket.getOutputStream();
            PrintStream ps = new PrintStream(os);
            ps.println("hello World!!");
            ps.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
