package com.petta.catui.nlp;

import com.petta.catui.config.VoiceConfig;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 再生レート（イントネーション）計算専用クラス。
 */
public class PitchCalculator {

    /**
     * 1文字ごとの再生レートを計算する
     *
     * @param pos        品詞
     * @param charIndex  単語内の文字インデックス
     * @param accentType UniDicのアクセント型
     * @return SoundPoolの再生レート
     */
    public static float calculatePlaybackRate(
            String pos,
            int charIndex,
            String accentType) {

        float baseRate = 1.0f;

        if ("助詞".equals(pos) || "助動詞".equals(pos)) {
            baseRate = 0.88f;
        } else if ("名詞".equals(pos)
                || "動詞".equals(pos)
                || "形容詞".equals(pos)) {
            baseRate = 1.08f;
        } else if ("感動詞".equals(pos)) {
            baseRate = 1.18f;
        }

        float finalRate = baseRate;

        float highRate = VoiceConfig.HIGH_PITCH;
        float lowRate = VoiceConfig.LOW_PITCH;

        int accent = -1;

        try {
            if (accentType != null
                    && !accentType.isEmpty()
                    && !accentType.equals("*")) {

                accent = Integer.parseInt(
                        accentType.split(",")[0]);
            }
        } catch (NumberFormatException ignored) {
        }

        int moraPos = charIndex + 1;

        if (accent == 0) {

            if (moraPos == 1) {
                finalRate += lowRate;
            } else {
                finalRate += highRate;
            }

        } else if (accent == 1) {

            if (moraPos == 1) {
                finalRate += highRate;
            } else {
                finalRate += lowRate;
            }

        } else if (accent > 1) {

            if (moraPos == 1) {
                finalRate += lowRate;
            } else if (moraPos <= accent) {
                finalRate += highRate;
            } else {
                finalRate += lowRate;
            }

        } else {

            if (charIndex == 0) {
                finalRate += lowRate;
            } else {
                finalRate += highRate;
            }
        }

        double fluctuation = ThreadLocalRandom.current().nextDouble(
                -VoiceConfig.RANDOM_FLUCTUATION / 2.0,
                VoiceConfig.RANDOM_FLUCTUATION / 2.0);

        finalRate += (float) fluctuation;

        return finalRate;
    }

    /**
     * 疑問文の語尾用再生レート
     */
    public static float calculateQuestionPlaybackRate() {
        return VoiceConfig.QUESTION_PITCH;
    }
}