package com.petta.catui.comm;

import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

public class SocketSender {

    private static final String ROS_IP = "192.168.0.10";
    private static final int PORT = 9001;

    public static void send(JSONObject json) {
        new Thread(() -> {
            try (Socket socket = new Socket(ROS_IP, PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
