package com.petta.catui.nlp;

import com.petta.catui.config.VoiceConfig;

/**
 * ピッチ（イントネーション）計算専用のドメインロジッククラス。
 * 外部ライブラリには依存せず、純粋なルールのみを管理する。
 */
public class PitchCalculator {

    /**
     * 1文字ごとの最終的なピッチを計算する（感情による上下は廃止、Config連動版）
     * @param pos 品詞（例: "名詞", "助詞"）
     * @param charIndex 単語内の文字インデックス（0始まり）
     * @param accentType UniDicから取得したアクセント型の文字列（"0", "1", "2"...）
     */
    public static float calculateCharPitch(String pos, int charIndex, String accentType) {
        // 品詞ベースのピッチ
        float basePitch = 1.0f;
        if ("助詞".equals(pos) || "助動詞".equals(pos)) {
            basePitch = 0.88f;
        } else if ("名詞".equals(pos) || "動詞".equals(pos) || "形容詞".equals(pos)) {
            basePitch = 1.08f;
        } else if ("感動詞".equals(pos)) {
            basePitch = 1.18f;
        }

        // 🌟 感情による倍率計算を完全に廃止し、ベースピッチをそのまま使用
        float finalPitch = basePitch;

        float HIGH_PITCH = VoiceConfig.HIGH_PITCH;
        float LOW_PITCH = VoiceConfig.LOW_PITCH;

        int accent = -1;
        try {
            if (accentType != null && !accentType.isEmpty() && !accentType.equals("*")) {
                accent = Integer.parseInt(accentType.split(",")[0]);
            }
        } catch (NumberFormatException e) {}

        int moraPos = charIndex + 1; 

        if (accent == 0) {
            if (moraPos == 1) finalPitch += LOW_PITCH;
            else finalPitch += HIGH_PITCH;
        } else if (accent == 1) {
            if (moraPos == 1) finalPitch += HIGH_PITCH;
            else finalPitch += LOW_PITCH;
        } else if (accent > 1) {
            if (moraPos == 1) finalPitch += LOW_PITCH;
            else if (moraPos <= accent) finalPitch += HIGH_PITCH;
            else finalPitch += LOW_PITCH; 
        } else {
            if (charIndex == 0) finalPitch += LOW_PITCH;
            else finalPitch += HIGH_PITCH;
        }

        // ランダムな揺らぎを加算
        finalPitch += (float) ((Math.random() * VoiceConfig.RANDOM_FLUCTUATION) - (VoiceConfig.RANDOM_FLUCTUATION / 2));

        return finalPitch;
    }

    /**
     * 疑問形の語尾上がりピッチを計算する
     */
    public static float calculateQuestionEndPitch() {
        return VoiceConfig.QUESTION_PITCH; 
    }
}