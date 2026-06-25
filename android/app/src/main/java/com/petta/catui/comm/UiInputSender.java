package com.petta.catui.comm;

import org.json.JSONObject;

public class UiInputSender {

    public static void sendTouch(float x, float y) {
        try {
            JSONObject json = new JSONObject();
            json.put("event", "touch");
            json.put("x", x);
            json.put("y", y);

            SocketSender.send(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
