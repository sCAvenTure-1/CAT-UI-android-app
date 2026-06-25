package com.petta.catui.comm;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.petta.catui.core.CatCharacter;
import com.petta.catui.core.ExpressionState;
import com.petta.catui.text.TextDisplay;
import com.petta.catui.core.AnimalVoicePlayer;
import com.petta.catui.core.VoiceType; // ★追加：VoiceTypeのインポート

public class UiEventController {

    private final CatCharacter character;
    private final TextDisplay textDisplay;
    private final ConcurrentLinkedQueue<Runnable> uiQueue;
    private final Timer timer = new Timer(true);

    private final AnimalVoicePlayer voicePlayer;

    private TimerTask resetTask;

    public UiEventController(
            CatCharacter character,
            TextDisplay textDisplay,
            ConcurrentLinkedQueue<Runnable> uiQueue,
            AnimalVoicePlayer voicePlayer) {

        this.character = character;
        this.textDisplay = textDisplay;
        this.uiQueue = uiQueue;
        this.voicePlayer = voicePlayer;
    }

    public void onEvent(UiEvent event) {

        uiQueue.add(() -> {
            textDisplay.showMessage("UiEventController.onEvent() CALLED");
        });

        // UI操作はキュー経由
        uiQueue.add(() -> {
            if (event.face != null) {
                character.setExpression(event.face);
            }
            if (event.text != null && !event.text.isEmpty()) {
                textDisplay.showMessage(event.text);
            }
        });

        // 🌟オリジナル準拠：表情に合わせて VoiceType（声の高さ）と読み上げ速度（文字数/秒）を切り替える
        if (event.text != null && !event.text.isEmpty() && voicePlayer != null) {
            VoiceType type = VoiceType.HIGH; // デフォルトの声
            int speed = 10;                 // デフォルトの速度（1秒間に10文字）

            if (event.face != null) {
                String faceStr = event.face.name();
                if ("HAPPY".equalsIgnoreCase(faceStr) || "SMILE".equalsIgnoreCase(faceStr)) {
                    type = VoiceType.HIGHHIGH; // 高い声
                    speed = 12;                // 少し早口
                } else if ("SLEEPING".equalsIgnoreCase(faceStr)) {
                    type = VoiceType.LOW;      // 低い声
                    speed = 5;                 // ゆっくり
                }
            }

            // オリジナルのロジックに完全準拠した再生メソッドを呼び出す
            voicePlayer.playVoice(event.text, type, speed);
        }

        // reset_after 処理
        if (resetTask != null) {
            resetTask.cancel();
        }

        if (event.resetAfter > 0) {
            resetTask = new TimerTask() {
                @Override
                public void run() {
                    uiQueue.add(() -> character.setExpression(ExpressionState.NORMAL));
                }
            };
            timer.schedule(resetTask, (long) (event.resetAfter * 1000));
        }
    }

    public void shutdown() {
        timer.cancel();
    }
}