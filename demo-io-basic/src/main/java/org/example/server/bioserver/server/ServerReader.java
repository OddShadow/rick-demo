package org.example.server.bioserver.server;

import org.example.server.bioserver.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;

public class ServerReader extends Thread {
    
    private Socket socket;
    public ServerReader(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            while (true) {
                int flag = dis.readInt();
                if (flag == 1) {
                    String name = dis.readUTF();
                    System.out.println(name + "===>" + socket.getRemoteSocketAddress());
                    ServerChat.onLineSockets.put(socket, name);
                }
                writeMsg(flag, dis);
            }
        } catch (Exception e) {
            System.out.println("===有人下线了===");
            ServerChat.onLineSockets.remove(socket);
            try {
                writeMsg(1, dis);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    private void writeMsg(int flag, DataInputStream dis) throws Exception {
        String msg = null;
        if (flag == 1) {
            StringBuilder sb = new StringBuilder();
            Collection<String> onLineNameList = ServerChat.onLineSockets.values();
            if (onLineNameList != null && onLineNameList.size() > 0) {
                for (String name : onLineNameList) {
                    sb.append(name + Constants.SPLIT);
                }
                msg = sb.substring(0, sb.lastIndexOf(Constants.SPLIT));
                sendMsgToAll(flag, msg);
            }
        } else if (flag == 2 || flag == 3) {
            String newMsg = dis.readUTF();
            String sendName = ServerChat.onLineSockets.get(socket);
            StringBuilder msgFinal = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
            if (flag == 2) {
                msgFinal.append(sendName).append("   ").append(sdf.format(System.currentTimeMillis() * 2)).append("\r\n");
                msgFinal.append("      ").append(newMsg).append("\r\n");
                sendMsgToAll(flag, msgFinal.toString());
            } else if (flag == 3) {
                msgFinal.append(sendName).append("   ").append(sdf.format(System.currentTimeMillis() * 2)).append("对您私发\r\n");
                msgFinal.append("      ").append(newMsg).append("\r\n");
                String destName = dis.readUTF();
                sendMsgToOne(destName, msgFinal.toString());
            }
        }
    }
    
    private void sendMsgToOne(String destName, String msg) throws Exception {
        Set<Socket> allOnLineSockets = ServerChat.onLineSockets.keySet();
        for (Socket socket : allOnLineSockets) {
            if (ServerChat.onLineSockets.get(socket).trim().equals(destName)) {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(2);
                dos.writeUTF(msg);
                dos.flush();
            }
        }
    }
    
    private void sendMsgToAll(int flag, String msg) throws Exception {
        Set<Socket> allOnLineSockets = ServerChat.onLineSockets.keySet();
        for (Socket socket : allOnLineSockets) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(flag);
            dos.writeUTF(msg);
            dos.flush();
        }
    }
}
