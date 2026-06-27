package com.petta.catui.voice;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.petta.catui.nlp.IntonationAnalyzer.VoiceSegment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalVoicePlayer {
    private final Context context;
    private final SoundPool soundPool;
    private final Map<String, Integer> soundMap = new HashMap<>();

    public AnimalVoicePlayer(Context context) {
        this.context = context;

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        this.soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attrs)
                .build();

        loadAssetVoices();
    }

    private void loadAssetVoices() {
        for (VoiceType type : VoiceType.values()) {
            for (String part : VoicePartAssets.supportedParts()) {
                String fileName = VoicePartAssets.fileNameFor(part);
                String assetPath = "voice/" + type.directoryName() + "/" + fileName + ".mp3";
                try (AssetFileDescriptor afd = context.getAssets().openFd(assetPath)) {
                    soundMap.put(key(type, part), soundPool.load(afd, 1));
                } catch (IOException e) {
                    System.err.println("[AnimalVoicePlayer] missing voice asset: " + assetPath);
                }
            }
        }
    }

    private String key(VoiceType type, String part) {
        return type.directoryName() + "/" + part;
    }

    public void playVoice(List<VoiceSegment> segments, VoiceType type, float globalSpeed) {
        new Thread(() -> {
            try {
                for (VoiceSegment segment : segments) {
                    if ("SILENCE".equals(segment.part)) {
                        Thread.sleep(150);
                        continue;
                    }

                    Integer soundId = soundMap.get(key(type, segment.part));
                    if (soundId != null) {
                        float finalRate = segment.pitch * globalSpeed;
                        if (finalRate < 0.5f) finalRate = 0.5f;
                        if (finalRate > 2.0f) finalRate = 2.0f;

                        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, finalRate);
                        Thread.sleep((long) (120 / finalRate));
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
