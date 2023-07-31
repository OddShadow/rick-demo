package org.example.server.bio.demoF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BioServerF {
    public static final List<Socket> allSocketList = new ArrayList<>();
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                allSocketList.add(socket);
                Thread aThread = new Thread(() -> {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String data = br.readLine();
                        while (data != null) {
                            for (Socket aSocket : allSocketList) {
                                PrintStream ps = new PrintStream(aSocket.getOutputStream());
                                ps.println(data);
                                ps.flush();
                            }
                            data = br.readLine();
                        }
                    } catch (IOException e) {
                        System.out.println("offline!!!");
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
