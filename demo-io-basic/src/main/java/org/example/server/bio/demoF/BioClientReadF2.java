package org.example.server.bio.demoF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BioClientReadF2 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String data = br.readLine();
                if (data != null) {
                    System.out.println(data);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}