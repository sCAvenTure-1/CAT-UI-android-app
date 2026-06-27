package com.petta.catui.voice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VoicePartAssets {
    private static final Map<String, String> PART_TO_FILE_NAME = new HashMap<>();

    static {
        // オリジナルの完全なマッピング（完璧です！）
        PART_TO_FILE_NAME.put("あ", "a"); PART_TO_FILE_NAME.put("い", "i"); PART_TO_FILE_NAME.put("う", "u");
        PART_TO_FILE_NAME.put("うぃ", "wi"); PART_TO_FILE_NAME.put("うぇ", "we"); PART_TO_FILE_NAME.put("うぉ", "uwo");
        PART_TO_FILE_NAME.put("え", "e"); PART_TO_FILE_NAME.put("お", "o");
        PART_TO_FILE_NAME.put("か", "ka"); PART_TO_FILE_NAME.put("が", "ga"); PART_TO_FILE_NAME.put("き", "ki");
        PART_TO_FILE_NAME.put("きゃ", "kya"); PART_TO_FILE_NAME.put("きゅ", "kyu"); PART_TO_FILE_NAME.put("きょ", "kyo");
        PART_TO_FILE_NAME.put("ぎ", "gi"); PART_TO_FILE_NAME.put("ぎゃ", "gya"); PART_TO_FILE_NAME.put("ぎゅ", "gyu"); PART_TO_FILE_NAME.put("ぎょ", "gyo");
        PART_TO_FILE_NAME.put("く", "ku"); PART_TO_FILE_NAME.put("ぐ", "gu"); PART_TO_FILE_NAME.put("け", "ke"); PART_TO_FILE_NAME.put("げ", "ge");
        PART_TO_FILE_NAME.put("こ", "ko"); PART_TO_FILE_NAME.put("ご", "go");
        PART_TO_FILE_NAME.put("さ", "sa"); PART_TO_FILE_NAME.put("ざ", "za"); PART_TO_FILE_NAME.put("し", "shi");
        PART_TO_FILE_NAME.put("しぇ", "she"); PART_TO_FILE_NAME.put("しゃ", "sha"); PART_TO_FILE_NAME.put("しゅ", "shu"); PART_TO_FILE_NAME.put("しょ", "sho");
        PART_TO_FILE_NAME.put("じ", "ji"); PART_TO_FILE_NAME.put("じぇ", "je"); PART_TO_FILE_NAME.put("じゃ", "ja"); PART_TO_FILE_NAME.put("じゅ", "ju"); PART_TO_FILE_NAME.put("じょ", "jo");
        PART_TO_FILE_NAME.put("す", "su"); PART_TO_FILE_NAME.put("ず", "zu"); PART_TO_FILE_NAME.put("せ", "se"); PART_TO_FILE_NAME.put("ぜ", "ze");
        PART_TO_FILE_NAME.put("そ", "so"); PART_TO_FILE_NAME.put("ぞ", "zo");
        PART_TO_FILE_NAME.put("た", "ta"); PART_TO_FILE_NAME.put("だ", "da"); PART_TO_FILE_NAME.put("ち", "chi");
        PART_TO_FILE_NAME.put("ちぇ", "che"); PART_TO_FILE_NAME.put("ちゃ", "cha"); PART_TO_FILE_NAME.put("ちゅ", "chu"); PART_TO_FILE_NAME.put("ちょ", "cho");
        PART_TO_FILE_NAME.put("ぢ", "dji"); PART_TO_FILE_NAME.put("つ", "tsu");
        PART_TO_FILE_NAME.put("つぁ", "tsa"); PART_TO_FILE_NAME.put("つぃ", "tsi"); PART_TO_FILE_NAME.put("つぇ", "tse"); PART_TO_FILE_NAME.put("つぉ", "tso");
        PART_TO_FILE_NAME.put("づ", "dzu"); PART_TO_FILE_NAME.put("て", "te"); PART_TO_FILE_NAME.put("てぃ", "ti");
        PART_TO_FILE_NAME.put("で", "de"); PART_TO_FILE_NAME.put("でぃ", "di"); PART_TO_FILE_NAME.put("でゅ", "dyu");
        PART_TO_FILE_NAME.put("と", "to"); PART_TO_FILE_NAME.put("ど", "do");
        PART_TO_FILE_NAME.put("な", "na"); PART_TO_FILE_NAME.put("に", "ni");
        PART_TO_FILE_NAME.put("にゃ", "nya"); PART_TO_FILE_NAME.put("にゅ", "nyu"); PART_TO_FILE_NAME.put("にょ", "nyo");
        PART_TO_FILE_NAME.put("ぬ", "nu"); PART_TO_FILE_NAME.put("ね", "ne"); PART_TO_FILE_NAME.put("の", "no");
        PART_TO_FILE_NAME.put("は", "ha"); PART_TO_FILE_NAME.put("ば", "ba"); PART_TO_FILE_NAME.put("ぱ", "pa");
        PART_TO_FILE_NAME.put("ひ", "hi"); PART_TO_FILE_NAME.put("ひゃ", "hya"); PART_TO_FILE_NAME.put("ひゅ", "hyu"); PART_TO_FILE_NAME.put("ひょ", "hyo");
        PART_TO_FILE_NAME.put("び", "bi"); PART_TO_FILE_NAME.put("びゃ", "bya"); PART_TO_FILE_NAME.put("びゅ", "byu"); PART_TO_FILE_NAME.put("びょ", "byo");
        PART_TO_FILE_NAME.put("ぴ", "pi"); PART_TO_FILE_NAME.put("ぴゃ", "pya"); PART_TO_FILE_NAME.put("ぴゅ", "pyu"); PART_TO_FILE_NAME.put("ぴょ", "pyo");
        PART_TO_FILE_NAME.put("ふ", "fu"); PART_TO_FILE_NAME.put("ふぁ", "fa"); PART_TO_FILE_NAME.put("ふぃ", "fi"); PART_TO_FILE_NAME.put("ふぇ", "fe"); PART_TO_FILE_NAME.put("ふぉ", "fo"); PART_TO_FILE_NAME.put("ふゅ", "fyu");
        PART_TO_FILE_NAME.put("ぶ", "bu"); PART_TO_FILE_NAME.put("ぷ", "pu");
        PART_TO_FILE_NAME.put("へ", "he"); PART_TO_FILE_NAME.put("べ", "be"); PART_TO_FILE_NAME.put("ぺ", "pe");
        PART_TO_FILE_NAME.put("ほ", "ho"); PART_TO_FILE_NAME.put("ぼ", "bo"); PART_TO_FILE_NAME.put("ぽ", "po");
        PART_TO_FILE_NAME.put("ま", "ma"); PART_TO_FILE_NAME.put("み", "mi");
        PART_TO_FILE_NAME.put("みゃ", "mya"); PART_TO_FILE_NAME.put("みゅ", "myu"); PART_TO_FILE_NAME.put("みょ", "myo");
        PART_TO_FILE_NAME.put("む", "mu"); PART_TO_FILE_NAME.put("め", "me"); PART_TO_FILE_NAME.put("も", "mo");
        PART_TO_FILE_NAME.put("や", "ya"); PART_TO_FILE_NAME.put("ゆ", "yu"); PART_TO_FILE_NAME.put("よ", "yo");
        PART_TO_FILE_NAME.put("ら", "ra"); PART_TO_FILE_NAME.put("り", "ri");
        PART_TO_FILE_NAME.put("りゃ", "rya"); PART_TO_FILE_NAME.put("りゅ", "ryu"); PART_TO_FILE_NAME.put("りょ", "ryo");
        PART_TO_FILE_NAME.put("る", "ru"); PART_TO_FILE_NAME.put("れ", "re"); PART_TO_FILE_NAME.put("ろ", "ro");
        PART_TO_FILE_NAME.put("わ", "wa"); PART_TO_FILE_NAME.put("を", "wo"); PART_TO_FILE_NAME.put("ん", "n");
        PART_TO_FILE_NAME.put("ゔぁ", "va"); PART_TO_FILE_NAME.put("ゔぃ", "vi"); PART_TO_FILE_NAME.put("ゔぇ", "ve"); PART_TO_FILE_NAME.put("ゔぉ", "vo");
    }

    public static List<String> supportedParts() {
        return new ArrayList<>(PART_TO_FILE_NAME.values());
    }

    public static String fileNameFor(String part) {
        return part;
    }

    /**
     * 🌟 ユーティリティ：カタカナが混ざっていても強制的にひらがなに変換する
     */
    private static String forceHiragana(String text) {
        if (text == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '\u30a1' && c <= '\u30f6') {
                sb.append((char) (c - 0x60));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static List<String> createSegments(String text) {
        List<String> segments = new ArrayList<>();
        if (text == null) return segments;

        // 🌟 ここで必ずひらがな化されるため、カタカナ漏れバグが完全に消滅します！
        String hiraText = forceHiragana(text);
        String replacedText = hiraText.replace("っ", "つ");
        
        int i = 0;

        while (i < replacedText.length()) {
            boolean matched = false;

            // 長い文字（"きゃ" など2文字）を先に判定
            if (i + 1 < replacedText.length()) {
                String twoChars = replacedText.substring(i, i + 2);
                if (PART_TO_FILE_NAME.containsKey(twoChars)) {
                    segments.add(PART_TO_FILE_NAME.get(twoChars));
                    i += 2;
                    matched = true;
                }
            }
            
            // 2文字でマッチしなかったら1文字で判定
            if (!matched) {
                String oneChar = replacedText.substring(i, i + 1);
                if (PART_TO_FILE_NAME.containsKey(oneChar)) {
                    segments.add(PART_TO_FILE_NAME.get(oneChar));
                } else {
                    // 知らない文字はオリジナル通り「無音（沈黙）」として扱う
                    segments.add("SILENCE");
                }
                i += 1;
            }
        }
        return segments;
    }
}