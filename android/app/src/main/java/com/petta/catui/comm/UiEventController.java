package com.petta.catui.comm;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.petta.catui.graphics.CatCharacter;
import com.petta.catui.text.TextDisplay;
import com.petta.catui.expression.ExpressionState; // ★ここ重要
import com.petta.catui.voice.VoiceController;

public class UiEventController {

    private final CatCharacter character;
    private final TextDisplay textDisplay;
    private final ConcurrentLinkedQueue<Runnable> uiQueue;
    private final Timer timer = new Timer(true);

    // ★変更：Playerを直接持つのではなく、音声司令塔（Controller）を持つ
    private final VoiceController voiceController;

    private TimerTask resetTask;

    public UiEventController(
            CatCharacter character,
            TextDisplay textDisplay,
            ConcurrentLinkedQueue<Runnable> uiQueue,
            VoiceController voiceController) { // ★変更

        this.character = character;
        this.textDisplay = textDisplay;
        this.uiQueue = uiQueue;
        this.voiceController = voiceController; // ★変更
    }

    public void onEvent(UiEvent event) {

        uiQueue.add(() -> {
            textDisplay.showMessage("UiEventController.onEvent() CALLED");
        });

        // UI操作（表情と文字表示）はキュー経由でメインスレッドへ
        uiQueue.add(() -> {
            if (event.face != null) {
                character.setExpression(event.face);
            }
            if (event.text != null && !event.text.isEmpty()) {
                textDisplay.showMessage(event.text);
            }
            if (event.text != null && !event.text.isEmpty() && voiceController != null) {
                voiceController.speak(event.text, event.face);
            }
        });

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
