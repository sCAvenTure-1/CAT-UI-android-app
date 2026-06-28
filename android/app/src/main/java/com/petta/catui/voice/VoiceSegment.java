package com.petta.catui.voice;

/**
 * 1つの音声再生単位を表すクラス。
 *
 * AnimalVoicePlayer はこの情報だけを見て再生を行う。
 */
public class VoiceSegment {

    /**
     * 再生する音素。
     *
     * 例：
     * "ka"
     * "shi"
     * "n"
     * "SILENCE"
     */
    public final String part;

    /**
     * SoundPool.play() に渡す再生速度。
     *
     * 通常は 0.5 ～ 2.0
     */
    public final float playbackRate;

    /**
     * この音の再生後に待機する秒数。
     *
     * 例
     * 0.012f
     * 0.08f
     * 0.15f
     */
    public final float waitSeconds;

    /**
     * 品詞。
     *
     * イントネーション解析や
     * デバッグ表示に利用する。
     */
    public final String pos;

    /**
     * VoiceSegment生成
     *
     * @param part 再生する音素
     * @param playbackRate 再生速度
     * @param waitSeconds 再生後の待機時間（秒）
     * @param pos 品詞
     */
    public VoiceSegment(
            String part,
            float playbackRate,
            float waitSeconds,
            String pos) {

        this.part = part;
        this.playbackRate = playbackRate;
        this.waitSeconds = waitSeconds;
        this.pos = pos;
    }

    /**
     * 無音かどうか
     */
    public boolean isSilence() {
        return "SILENCE".equals(part);
    }

    @Override
    public String toString() {
        return "VoiceSegment{" +
                "part='" + part + '\'' +
                ", playbackRate=" + playbackRate +
                ", waitSeconds=" + waitSeconds +
                ", pos='" + pos + '\'' +
                '}';
    }
}