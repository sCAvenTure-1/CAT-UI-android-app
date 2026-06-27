package com.petta.catui.nlp;

import com.atilika.kuromoji.unidic.Token;
import com.atilika.kuromoji.unidic.Tokenizer;
import com.petta.catui.expression.ExpressionState;
import com.petta.catui.voice.VoicePartAssets;
import com.petta.catui.config.VoiceConfig; // 🌟 Configをインポート

import java.util.ArrayList;
import java.util.List;

/**
 * 自然言語処理 (NLP) コーディネータークラス。
 */
public class IntonationAnalyzer {

    private static final Tokenizer tokenizer = new Tokenizer();

    public static class VoiceSegment {
        public final String part;
        public final float pitch;
        public final String pos;

        public VoiceSegment(String part, float pitch, String pos) {
            this.part = part;
            this.pitch = pitch;
            this.pos = pos;
        }
    }

    public static List<VoiceSegment> analyze(String text, ExpressionState face) {
        List<VoiceSegment> segments = new ArrayList<>();
        if (text == null || text.isEmpty()) return segments;

        boolean isSleeping = (face == ExpressionState.SLEEPING);

        List<Token> tokens = tokenizer.tokenize(text);

        for (Token token : tokens) {
            String surface = token.getSurface();
            String pos = token.getPartOfSpeechLevel1();
            
            // 記号判定：疑問符の語尾上がり
            if ("補助記号".equals(pos) || surface.equals("？") || surface.equals("?")) {
                if ((surface.equals("？") || surface.equals("?")) && !segments.isEmpty()) {
                    VoiceSegment last = segments.get(segments.size() - 1);
                    float questionPitch = PitchCalculator.calculateQuestionEndPitch();
                    segments.set(segments.size() - 1, new VoiceSegment(last.part, questionPitch, last.pos));
                }
            }

            String reading = token.getPronunciation();
            String[] features = token.getAllFeaturesArray();
            String accentType = "";
            if (features.length > 21) {
                accentType = features[21]; 
            }

            String targetText;
            if (reading == null || "*".equals(reading) || reading.isEmpty()) {
                targetText = approximateEnglish(surface);
            } else {
                targetText = convertKataToHira(reading);
            }

            if ("助詞".equals(pos)) {
                if ("ハ".equals(reading)) targetText = "わ";
                if ("ヘ".equals(reading)) targetText = "え";
            }
            targetText = targetText.replace("ー", "");

            if (targetText.trim().isEmpty()) continue;

            List<String> parts = VoicePartAssets.createSegments(targetText);

            for (int i = 0; i < parts.size(); i++) {
                float finalPitch = PitchCalculator.calculateCharPitch(pos, i, accentType);
                segments.add(new VoiceSegment(parts.get(i), finalPitch, pos));

                // =========================================================
                // 🌟 文字間隔（スピード）コントロールロジック
                // =========================================================
                
                // 1. まずJSONで設定された基本の隙間時間を取得
                float gapDuration = VoiceConfig.CHAR_INTERVAL;
                
                // 2. 眠い顔の時は、さらに強制的に+1.0fの間延びを追加する
                if (isSleeping) {
                    gapDuration += 1.0f; 
                }

                // 3. 隙間時間が0より大きい場合のみ、SILENCEパーツを挿入する
                if (gapDuration > 0.0f) {
                    segments.add(new VoiceSegment("SILENCE", gapDuration, "空白"));
                }
            }
        }
        return segments;
    }

    /**
     * ユーティリティ：全角カタカナを全角ひらがなに変換するメソッド
     */
    private static String convertKataToHira(String kata) {
        if (kata == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kata.length(); i++) {
            char c = kata.charAt(i);
            if (c >= '\u30a1' && c <= '\u30f6') {
                sb.append((char) (c - 0x60));
            } else {
                sb.append(c); 
            }
        }
        return sb.toString();
    }

    /**
     * ユーティリティ：英語（アルファベット）をひらがな読みに近似するメソッド
     */
    private static String approximateEnglish(String text) {
        if (text == null) return "";
        String lower = text.toLowerCase(); 
        StringBuilder sb = new StringBuilder();
        
        String[] alphaKana = {
            "えー", "びー", "しー", "でぃー", "いー", "えふ", "じー", 
            "えいち", "あい", "じぇー", "けー", "える", "えむ", "えぬ", 
            "おー", "ぴー", "きゅー", "あーる", "えす", "てぃー", 
            "ゆー", "ぶい", "だぶりゅ", "えっくす", "わい", "ぜっと"
        };

        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (c >= 'a' && c <= 'z') {
                sb.append(alphaKana[c - 'a']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}