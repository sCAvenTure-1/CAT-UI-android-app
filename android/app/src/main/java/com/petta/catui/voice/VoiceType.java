package com.petta.catui.core;

public enum VoiceType {
    HIGH("high", "voice_high_speed3"),
    HIGHHIGH("highhigh", "voice_high_speed5"),
    LOW("low", "voice_low_speed2");

    private final String value;
    private final String directoryName;

    VoiceType(String value, String directoryName) {
        this.value = value;
        this.directoryName = directoryName;
    }

    public String directoryName() {
        return directoryName;
    }
}