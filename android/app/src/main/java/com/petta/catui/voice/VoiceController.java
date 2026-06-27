package com.petta.catui.voice;

import com.petta.catui.expression.ExpressionState;
import com.petta.catui.nlp.IntonationAnalyzer;
import com.petta.catui.nlp.IntonationAnalyzer.VoiceSegment;
import com.petta.catui.config.VoiceConfig;

import java.util.List;

public class VoiceController {
    private final AnimalVoicePlayer voicePlayer;

    public VoiceController(AnimalVoicePlayer voicePlayer) {
        this.voicePlayer = voicePlayer;
    }

    public void speak(String text, ExpressionState face) {
        if (text == null || text.isEmpty()) return;

        List<VoiceSegment> segments = IntonationAnalyzer.analyze(text, face);

        // 🌟 表情による分岐を廃止。常に設定通りの声とスピードで喋る
        VoiceType type = VoiceType.DEFAULT; 
        float speed = VoiceConfig.BASE_SPEED;

        if (!segments.isEmpty()) {
            voicePlayer.playVoice(segments, type, speed);
        }
    }
}