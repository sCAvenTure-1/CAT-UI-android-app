package com.petta.catui.config;

import android.content.Context;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class VoiceConfig {
    
    public static float HIGH_PITCH = 0.25f;
    public static float LOW_PITCH = -0.25f;
    public static float QUESTION_PITCH = 1.80f;
    public static double RANDOM_FLUCTUATION = 0.06;

    public static float BASE_SPEED = 1.0f;
    public static String TARGET_VOICE_DIR = "voice_high_speed3";
    
    // 🌟 新規：文字と文字の間に挟む無音の長さ
    public static float CHAR_INTERVAL = 0.0f;

    public static void load(Context context) {
        try {
            InputStream is = context.getAssets().open("voice_config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject obj = new JSONObject(json);
            
            if (obj.has("highPitch")) HIGH_PITCH = (float) obj.getDouble("highPitch");
            if (obj.has("lowPitch")) LOW_PITCH = (float) obj.getDouble("lowPitch");
            if (obj.has("questionPitch")) QUESTION_PITCH = (float) obj.getDouble("questionPitch");
            if (obj.has("randomFluctuation")) RANDOM_FLUCTUATION = obj.getDouble("randomFluctuation");
            
            if (obj.has("baseSpeed")) BASE_SPEED = (float) obj.getDouble("baseSpeed");
            if (obj.has("targetVoiceDir")) TARGET_VOICE_DIR = obj.getString("targetVoiceDir");
            
            // 🌟 追加
            if (obj.has("charInterval")) CHAR_INTERVAL = (float) obj.getDouble("charInterval");

            System.out.println("🐈 VoiceConfig: JSONの読み込みに成功しました！");
        } catch (Exception e) {
            System.err.println("🐈 VoiceConfig: JSONの読み込みに失敗。デフォルト値を使用します。");
            e.printStackTrace();
        }
    }
}