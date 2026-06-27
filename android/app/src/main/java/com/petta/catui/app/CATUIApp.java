package com.petta.catui.app;

import processing.core.PApplet;

import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.petta.catui.graphics.CatCharacter;
import com.petta.catui.text.TextDisplay;
import com.petta.catui.comm.UiEventController;
import com.petta.catui.comm.SocketReceiver;
import com.petta.catui.comm.UiInputSender;
import com.petta.catui.voice.AnimalVoicePlayer;
import com.petta.catui.voice.VoiceController;
import com.petta.catui.expression.ExpressionState;

public class CATUIApp extends PApplet {

  private CatCharacter character;
  private TextDisplay textDisplay;
  private float globalScale;

  private int lastW = -1;
  private int lastH = -1;

  private UiEventController uiEventController;
  private SocketReceiver socketReceiver;

  // 🌟音声系の変数を集約
  private AnimalVoicePlayer voicePlayer;
  private VoiceController voiceController; // ★追加: 新しい音声司令塔

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

    // 🌟1. 音声再生プレイヤー（物理再生担当）を初期化
    voicePlayer = new AnimalVoicePlayer(getActivity());

    // 🌟2. 音声操作の司令塔（ロジック担当）を初期化し、プレイヤーを渡す
    voiceController = new VoiceController(voicePlayer);

    // 🌟3. UIイベントコントローラーに、voicePlayerの代わりにvoiceControllerを渡す
    uiEventController = new UiEventController(character, textDisplay, uiQueue, voiceController);

    socketReceiver = new SocketReceiver(9000, uiEventController);
    socketReceiver.start();

    textDisplay.showMessage("Socket listening on 9000");
  }

  @Override
  public void draw() {

    // ★ サイズ変化検知
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
  public void exit() {
    if (socketReceiver != null)
      socketReceiver.shutdown();
    if (uiEventController != null)
      uiEventController.shutdown();
    timer.cancel();
    super.exit();
  }
  
}
