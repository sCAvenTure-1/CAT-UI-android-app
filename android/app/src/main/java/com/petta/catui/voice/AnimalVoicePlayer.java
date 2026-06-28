package com.petta.catui.voice;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.media.MediaMetadataRetriever;
import com.petta.catui.config.VoiceConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

public class AnimalVoicePlayer {

    private final Context context;
    private final SoundPool soundPool;

    /** 音声ID */
    private final Map<String, Integer> soundMap = new HashMap<>();

    /** mp3の長さ(ms) */
    private final Map<String, Long> durationMap = new HashMap<>();

    public AnimalVoicePlayer(Context context) {

        this.context = context;

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attrs)
                .build();

        loadAssetVoices();
    }

    private void loadAssetVoices() {

        for (VoiceType type : VoiceType.values()) {

            for (String part : VoicePartAssets.supportedParts()) {

                String fileName = VoicePartAssets.fileNameFor(part);

                String assetPath = "voice/" +
                        type.directoryName() +
                        "/" +
                        fileName +
                        ".mp3";

                try (AssetFileDescriptor afd = context.getAssets().openFd(assetPath)) {

                    soundMap.put(
                            key(type, part),
                            soundPool.load(afd, 1));

                    durationMap.put(
                            part,
                            getDurationMillis(afd));

                } catch (IOException e) {

                    System.err.println(
                            "[AnimalVoicePlayer] missing voice asset : "
                                    + assetPath);
                }
            }
        }
    }

    private long getDurationMillis(AssetFileDescriptor afd) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        try {

            mmr.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength());

            String duration = mmr.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION);

            if (duration == null) {
                return 0L;
            }

            return Long.parseLong(duration);

        } catch (Throwable e) {

            // ★ここが重要：IOException系も全部吸収
            e.printStackTrace();
            return 0L;

        } finally {

            try {
                mmr.release();
            } catch (Exception ignore) {
            }
        }

    }

    private String key(VoiceType type, String part) {

        return type.directoryName() + "/" + part;
    }

    public void playVoice(
            List<VoiceSegment> segments,
            VoiceType type,
            float globalSpeed) {

        new Thread(() -> {

            long nextPlayTime = System.nanoTime();

            for (VoiceSegment segment : segments) {

                waitUntil(nextPlayTime);

                if (segment.isSilence()) {

                    nextPlayTime += (long) (segment.waitSeconds * 1_000_000_000L);

                    continue;
                }

                Integer soundId = soundMap.get(key(type, segment.part));

                if (soundId == null) {

                    System.err.println(
                            "[AnimalVoicePlayer] Missing : "
                                    + segment.part);

                    continue;
                }

                float finalRate = segment.playbackRate * globalSpeed;

                if (finalRate < 0.5f) {
                    finalRate = 0.5f;
                }

                if (finalRate > 2.0f) {
                    finalRate = 2.0f;
                }

                soundPool.play(
                        soundId,
                        1.0f,
                        1.0f,
                        1,
                        0,
                        finalRate);
                // 音声長(ms)
                long durationMs = durationMap.getOrDefault(segment.part, 0L);

                if (durationMs <= 0L) {
                    durationMs = 100; // fallback 100ms
                }

                // 再生速度を考慮した実際の再生時間(ns)
                long durationNs = (long) ((durationMs * 1_000_000L) / finalRate);

                // 次回再生予定時刻
                nextPlayTime += durationNs;
                nextPlayTime += (long) (segment.waitSeconds * 1_000_000_000L);

                if (segment.waitSeconds > 0f) {
                    nextPlayTime += (long) (segment.waitSeconds * 1_000_000_000L);
                }
            }

        }).start();
    }

    /**
     * 指定した時刻まで高精度に待機する。
     */
    private void waitUntil(long targetTime) {

        while (true) {

            long remaining = targetTime - System.nanoTime();

            if (remaining <= 0L) {
                return;
            }

            // 残り2ms以上はparkNanosでスリープ
            if (remaining > 2_000_000L) {

                LockSupport.parkNanos(remaining - 1_000_000L);

            } else {

                // 最後の約2msだけスピン待機
                Thread.onSpinWait();

            }
        }
    }
}