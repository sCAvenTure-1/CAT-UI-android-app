package com.petta.catui.config;

import android.content.Context;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class VoiceConfig {

    // ------------------------------
    // ピッチ設定
    // ------------------------------

    public static float HIGH_PITCH = 0.25f;
    public static float LOW_PITCH = -0.25f;
    public static float QUESTION_PITCH = 1.80f;
    public static double RANDOM_FLUCTUATION = 0.06;

    // ------------------------------
    // 再生設定
    // ------------------------------

    public static float BASE_SPEED = 1.0f;
    public static String TARGET_VOICE_DIR = "voice_high_speed3";

    // ------------------------------
    // 間隔設定（秒）
    // ------------------------------

    /** 文字と文字の間 */
    public static float CHAR_INTERVAL = 0.0f;

    /** 「、」の長さ */
    public static float COMMA_INTERVAL = 0.08f;

    /** 「。」の長さ */
    public static float PERIOD_INTERVAL = 0.15f;

    /** 「？」の長さ */
    public static float QUESTION_INTERVAL = 0.12f;

    public static void load(Context context) {

        try {

            InputStream is =
                    context.getAssets().open("voice_config.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json =
                    new String(buffer, StandardCharsets.UTF_8);

            JSONObject obj = new JSONObject(json);

            //----------------------------
            // ピッチ
            //----------------------------

            if (obj.has("highPitch"))
                HIGH_PITCH =
                        (float) obj.getDouble("highPitch");

            if (obj.has("lowPitch"))
                LOW_PITCH =
                        (float) obj.getDouble("lowPitch");

            if (obj.has("questionPitch"))
                QUESTION_PITCH =
                        (float) obj.getDouble("questionPitch");

            if (obj.has("randomFluctuation"))
                RANDOM_FLUCTUATION =
                        obj.getDouble("randomFluctuation");

            //----------------------------
            // 再生
            //----------------------------

            if (obj.has("baseSpeed"))
                BASE_SPEED =
                        (float) obj.getDouble("baseSpeed");

            if (obj.has("targetVoiceDir"))
                TARGET_VOICE_DIR =
                        obj.getString("targetVoiceDir");

            //----------------------------
            // 間隔
            //----------------------------

            if (obj.has("charInterval"))
                CHAR_INTERVAL =
                        (float) obj.getDouble("charInterval");

            if (obj.has("commaInterval"))
                COMMA_INTERVAL =
                        (float) obj.getDouble("commaInterval");

            if (obj.has("periodInterval"))
                PERIOD_INTERVAL =
                        (float) obj.getDouble("periodInterval");

            if (obj.has("questionInterval"))
                QUESTION_INTERVAL =
                        (float) obj.getDouble("questionInterval");

            System.out.println("🐈 VoiceConfig: JSON読み込み成功");

        } catch (Exception e) {

            System.err.println("🐈 VoiceConfig: JSON読み込み失敗");

            e.printStackTrace();
        }
    }
}