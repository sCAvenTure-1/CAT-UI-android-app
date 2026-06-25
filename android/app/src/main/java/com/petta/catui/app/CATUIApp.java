package com.petta.catui.ui;

import processing.core.PApplet;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.petta.catui.core.CatCharacter;
import com.petta.catui.text.TextDisplay;

// ★ 通信関連
import com.petta.catui.comm.UiEventController;
import com.petta.catui.comm.SocketReceiver;
import com.petta.catui.comm.UiInputSender;

// 🌟追加: 音声プレイヤーをインポート
import com.petta.catui.core.AnimalVoicePlayer;

public class CATUIApp extends PApplet {

  private CatCharacter character;
  private TextDisplay textDisplay;
  private float globalScale;

  private int lastW = -1;
  private int lastH = -1;

  private UiEventController uiEventController;
  private SocketReceiver socketReceiver;

  // 🌟追加: 音声プレイヤーの変数
  private AnimalVoicePlayer voicePlayer;

  private final ConcurrentLinkedQueue<Runnable> uiQueue = new ConcurrentLinkedQueue<>();

  private final Timer timer = new Timer(true);

  @Override
  public void settings() {
    fullScreen();
    smooth(8);
  }

  @Override
  public void setup() {
    globalScale = min(width / 400f, height / 300f);

    character = new CatCharacter(this, globalScale);
    textDisplay = new TextDisplay(this, globalScale);

    textDisplay.setAutoAdvance(true, 2.5f);

    // 🌟追加: 音声プレイヤーを初期化
    // PApplet内では getActivity() を使うと、AndroidのContextを取得できます
    voicePlayer = new AnimalVoicePlayer(getActivity());

    // 🌟修正: voicePlayer を UiEventController に渡す
    uiEventController = new UiEventController(character, textDisplay, uiQueue, voicePlayer);

    socketReceiver = new SocketReceiver(9000, uiEventController);
    socketReceiver.start();

    textDisplay.showMessage("Socket listening on 9000");
  }

  @Override
  public void draw() {

    // ★ サイズ変化検知（最重要）
    if (width != lastW || height != lastH) {
      lastW = width;
      lastH = height;

      globalScale = min(width / 400f, height / 300f);
      character.onScreenResized(this, globalScale);
      textDisplay.onScreenResized(globalScale, width, height);
    }

    // ★ UIキュー処理
    Runnable task;
    while ((task = uiQueue.poll()) != null) {
      task.run();
    }

    background(0);

    character.update();
    character.draw();

    textDisplay.update();
    textDisplay.draw();
  }

  @Override
  public void mousePressed() {
    UiInputSender.sendTouch(mouseX, mouseY);
  }

  @Override
  public void exit() {
    if (socketReceiver != null)
      socketReceiver.shutdown();
    if (uiEventController != null)
      uiEventController.shutdown();
    timer.cancel();
    super.exit();
  }
}