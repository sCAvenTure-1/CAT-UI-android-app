package com.petta.catui.core;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalVoicePlayer {
    private SoundPool soundPool;
    // VoiceType ごとに音源IDを管理するマップ
    private Map<VoiceType, Map<String, Integer>> voiceMaps;
    private Context context;

    public AnimalVoicePlayer(Context context) {
        this.context = context;
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                // 🌟修正ポイント1：同時に重なって鳴る音の上限を 10 から 32 に大幅アップ！
                // これにより、前の音が鳴り終わる前に次の音が来ても強制カットされにくくなります。
                .setMaxStreams(32)
                .setAudioAttributes(attributes)
                .build();

        voiceMaps = new HashMap<>();
        loadAllSounds();
    }

    private void loadAllSounds() {
        AssetManager assets = context.getAssets();
        List<String> allParts = VoicePartAssets.supportedParts();

        // HIGH, HIGHHIGH, LOW すべてのフォルダをロードする
        for (VoiceType type : VoiceType.values()) {
            Map<String, Integer> map = new HashMap<>();
            for (String part : allParts) {
                try {
                    // assets/voice/voice_high_speed3/a.mp3 のようなパスを作る
                    String fileName = "voice/" + type.directoryName() + "/" + VoicePartAssets.fileNameFor(part) + ".mp3";
                    AssetFileDescriptor afd = assets.openFd(fileName);
                    int soundId = soundPool.load(afd, 1);
                    map.put(part, soundId);
                } catch (Exception e) {
                    // ファイルがない場合は無視
                }
            }
            voiceMaps.put(type, map);
        }
    }

    // オリジナルの generate(text, speed, voiceType) に相当
    public void playVoice(String text, VoiceType voiceType, int speed) {
        List<String> parsedParts = VoicePartAssets.createSegments(text);
        Map<String, Integer> currentVoiceMap = voiceMaps.get(voiceType);

        new Thread(() -> {
            for (String part : parsedParts) {
                // 沈黙処理（オリジナル通り 500ms）
                if (part.equals("SILENCE")) {
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                    continue;
                }

                // 音声再生
                if (currentVoiceMap != null && currentVoiceMap.containsKey(part)) {
                    int soundId = currentVoiceMap.get(part);
                    soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
                }

                // 速度（speed: 1秒間に何文字読み上げるか）に応じた待機
                try {
                    // 🌟修正ポイント2：計算された待機時間に 40ms の「ゆとり」を足す
                    long delayMs = (1000 / speed) + 20; 
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {}
            }
        }).start();
    }
}