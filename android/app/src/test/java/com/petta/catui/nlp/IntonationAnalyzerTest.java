package com.petta.catui.nlp;

import com.petta.catui.expression.ExpressionState;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

public class IntonationAnalyzerTest {

    @Test
    public void testKuromojiParsingAndPitch() {
        String text = "こんにちは！ こんにちは？ こんにちは! こんにちは?";

        System.out.println("=== 🔍 UniDic 辞書データ デバッグ開始 ===");
        // テスト側で独自にTokenizerを回して、辞書がどう解釈しているか覗き見する
        com.atilika.kuromoji.unidic.Tokenizer debugTokenizer = new com.atilika.kuromoji.unidic.Tokenizer();
        for (com.atilika.kuromoji.unidic.Token token : debugTokenizer.tokenize(text)) {
            String surface = token.getSurface();
            String[] features = token.getAllFeaturesArray();
            System.out.println("★ 表面: " + surface + " | 配列長: " + features.length + " | 中身: " + String.join(",", features));
        }
        System.out.println("=========================================\n");

        // 従来の解析テスト
        List<IntonationAnalyzer.VoiceSegment> results = IntonationAnalyzer.analyze(text, ExpressionState.NORMAL);

        assertNotNull("解析結果がnullになっています", results);
        assertFalse("解析結果が空っぽです", results.isEmpty());

        System.out.println("=== 🐈 解析テスト開始 ===");
        System.out.println("対象テキスト: " + text);
        System.out.println("-------------------------");
        
        for (IntonationAnalyzer.VoiceSegment segment : results) {
            System.out.printf("パーツ: %-5s | 品詞: %-5s | ピッチ: %.2f%n", segment.part, segment.pos, segment.pitch);
        }
        
        System.out.println("=========================");
    }
}