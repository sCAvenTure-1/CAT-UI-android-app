package com.petta.catui.voice;

import com.petta.catui.config.VoiceConfig;

public enum VoiceType {
    DEFAULT; // もう1種類だけでOK

    // 🌟 常にJSONで設定されたフォルダ名を返す
    public String directoryName() {
        return VoiceConfig.TARGET_VOICE_DIR;
    }
}