package org.example.server.bio.demoA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServerB {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9999);
            Socket socket = ss.accept();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String data;
            if ((data = br.readLine()) != null) {
                System.out.println("service-accept::" + data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
