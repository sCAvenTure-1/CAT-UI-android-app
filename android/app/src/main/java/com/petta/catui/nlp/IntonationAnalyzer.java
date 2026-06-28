package com.petta.catui.nlp;

import com.atilika.kuromoji.unidic.Token;
import com.atilika.kuromoji.unidic.Tokenizer;
import com.petta.catui.config.VoiceConfig;
import com.petta.catui.expression.ExpressionState;
import com.petta.catui.voice.VoicePartAssets;
import com.petta.catui.voice.VoiceSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * 自然言語処理 (NLP) コーディネータークラス。
 */
public class IntonationAnalyzer {

    private static final Tokenizer tokenizer = new Tokenizer();

    public static List<VoiceSegment> analyze(String text, ExpressionState face) {

        List<VoiceSegment> segments = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return segments;
        }

        boolean isSleeping = (face == ExpressionState.SLEEPING);

        List<Token> tokens = tokenizer.tokenize(text);

        for (Token token : tokens) {

            String surface = token.getSurface();
            String pos = token.getPartOfSpeechLevel1();

            // =====================================================
            // 句読点・記号処理
            // =====================================================

            if ("補助記号".equals(pos)
                    || "記号".equals(pos)) {

                float interval = VoiceConfig.CHAR_INTERVAL;

                switch (surface) {

                    case "、":
                    case ",":
                        interval = VoiceConfig.COMMA_INTERVAL;
                        break;

                    case "。":
                    case ".":
                        interval = VoiceConfig.PERIOD_INTERVAL;
                        break;

                    case "？":
                    case "?":

                        interval = VoiceConfig.QUESTION_INTERVAL;

                        if (!segments.isEmpty()) {

                            VoiceSegment last = segments.get(segments.size() - 1);

                            segments.set(
                                    segments.size() - 1,
                                    new VoiceSegment(
                                            last.part,
                                            PitchCalculator.calculateQuestionPlaybackRate(),
                                            last.waitSeconds,
                                            last.pos));
                        }

                        break;

                    default:
                        interval = VoiceConfig.CHAR_INTERVAL;
                        break;
                }

                if (isSleeping) {
                    interval += 1.0f;
                }

                if (interval > 0.0f) {

                    segments.add(
                            new VoiceSegment(
                                    "SILENCE",
                                    1.0f,
                                    interval,
                                    "空白"));
                }

                continue;
            }

            String reading = token.getPronunciation();

            String[] features = token.getAllFeaturesArray();

            String accentType = "";

            if (features.length > 21) {
                accentType = features[21];
            }

            String targetText;

            if (reading == null
                    || "*".equals(reading)
                    || reading.isEmpty()) {

                targetText = approximateEnglish(surface);

            } else {

                targetText = convertKataToHira(reading);

            }

            if ("助詞".equals(pos)) {

                if ("ハ".equals(reading)) {
                    targetText = "わ";
                }

                if ("ヘ".equals(reading)) {
                    targetText = "え";
                }

            }

            targetText = targetText.replace("ー", "");

            if (targetText.trim().isEmpty()) {
                continue;
            }

            List<String> parts = VoicePartAssets.createSegments(targetText);
            for (int i = 0; i < parts.size(); i++) {

                String part = parts.get(i);

                float playbackRate = PitchCalculator.calculatePlaybackRate(
                        pos,
                        i,
                        accentType);

                float waitSeconds = VoiceConfig.CHAR_INTERVAL;

                if (isSleeping) {
                    waitSeconds += 1.0f;
                }

                segments.add(
                        new VoiceSegment(
                                part,
                                playbackRate,
                                waitSeconds,
                                pos));
            }
        }

        return segments;
    }

    /**
     * ユーティリティ：全角カタカナを全角ひらがなに変換するメソッド
     */
    private static String convertKataToHira(String kata) {

        if (kata == null) {
            return null;
        }

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

        if (text == null) {
            return "";
        }

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