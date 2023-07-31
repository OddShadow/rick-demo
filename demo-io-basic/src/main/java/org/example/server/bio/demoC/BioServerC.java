package org.example.server.bio.demoC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServerC {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                Thread aThread = new Thread(() -> {
                    try {
                        InputStream is = socket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String data;
                        while ((data = br.readLine()) != null) {
                            System.out.println("service-accept::" + data);
                        }
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
