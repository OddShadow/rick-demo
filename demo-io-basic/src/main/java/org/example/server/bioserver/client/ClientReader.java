package org.example.server.bioserver.client;

import org.example.server.bioserver.utils.Constants;

import java.io.DataInputStream;
import java.net.Socket;

public class ClientReader extends Thread {
    
    private ClientChat clientChat;
    private Socket socket;
    
    public ClientReader(ClientChat clientChat, Socket socket) {
        this.clientChat = clientChat;
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (true) {
                int flag = dis.readInt();
                if (flag == 1){
                    String nameDates = dis.readUTF();
                    String[] names = nameDates.split(Constants.SPLIT);
                    clientChat.onLineUsers.setListData(names);
                } else if (flag == 2) {
                    String msg = dis.readUTF();
                    clientChat.smsContent.append(msg);
                    clientChat.smsContent.setCaretPosition(clientChat.smsContent.getText().length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
