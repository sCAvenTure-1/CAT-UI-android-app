# CAT UI Android App v2.0

This repository contains **CAT UI Android App v2.0**.

Version 2.0 introduces a **TCP + JSON based communication layer**
that allows external systems (Python, ROS2, etc.) to control
cat expressions and on-screen messages in real time.



# CAT UI Android App — Communication Layer

このリポジトリは、**Android 上で稼働する猫型キャラクター UI** に  
外部プロセス（Python / ROS2 / 任意の TCP クライアント）から  
**JSON 経由で制御情報を送るための通信レイヤー** を実装したものです。

表情や描画ロジックの詳細は **別リポジトリに分離**しており、  
本リポジトリでは **通信とイベント反映部分を中心**に扱っています。

---

## 📦 関連リポジトリ

- 🐱 表情・描画ロジック:  [CAT-UI-processing](https://github.com/Petta-Yukiyanagi/CAT-UI-processing)

    この Android アプリでは  
    `CatCharacter` やパーツ描画のロジック自体は上記リポジトリを基にしています。  
    UI 表示の実装やパーツ構成・アニメーションもこちらの設計が元になっています。

---

## 📂 ディレクトリ構成
```
com.petta.catui
 ├── app/         # アプリの根幹・エントリーポイント
 │    ├── MainActivity.java （Android OSからの起動口）
 │    └── CATUIApp.java     （Processing/UIの総司令塔）
 │
 ├── comm/        # 通信レイヤーとUIスレッド安全機構
 │    ├── SocketReceiver.java （別スレッドでのTCP受信）
 │    └── UiEventController.java （JSON解析とUIキューへの追加）
 │
 ├── voice/       # 音声再生・イントネーション制御
 │    ├── AnimalVoicePlayer.java （SoundPoolによる複数音源の並列再生）
 │    ├── VoicePartAssets.java   （テキストの音声パーツ分解ロジック）
 │    └── VoiceType.java         （声の高さ定義 Enum）
 │
 ├── expression/  # 表情データの定義と状態管理
 │    ├── ExpressionState.java
 │    └── ExpressionBuilder.java...
 │
 └── graphics/    # 実際の描画・レンダリング処理
      ├── CatCharacter.java
      ├── parts/       （目、口などの個別パーツ）
      └── decorators/  （汗、怒りマークなどの装飾）
```

## 🎯 このリポジトリの目的

- **外部システムからのリアルタイム UI 操作**
- ROS2 などのロボット制御系ノードと連携した表情制御
- Android 端末単独で動作可能（オフライン対応）
- WebSocket を使わず **単純 TCP + JSON で低レイヤーメッセージング**

表情ロジックは別リポジトリにあり、  
ここでは「**通信 → UI 反映** のパイプラインを実装」しています。

---

## 🔌 通信方式

### プロトコル

- **TCP Socket**
- ポート: **9000**
- 1 接続 = 1 JSON メッセージ（改行終端）
- 軽量で安定したシンプルなプロトコル

**WebSocket ではありません。**

---

## 📡 JSON フォーマット

アプリが受信・反映する JSON は次のような形式です：

```json
{
  "face": 3,
  "text": "こんにちは！Androidから来たよ",
  "reset_after": 3
}
```
キー	型	説明
face	int	表情 ID
text	string	画面に表示したい文字列
reset_after	float	指定秒後に表情を NORMAL に戻す（省略可能）

🧠 通信クラス構成
📌 SocketReceiver
com.petta.catui.comm.SocketReceiver

別スレッドで TCP サーバを起動

1 行（\n まで）の JSON を受信して処理

📌 JsonUiEventParser
com.petta.catui.comm.JsonUiEventParser

受信した文字列を UiEvent オブジェクトに変換

📌 UiEventController
com.petta.catui.comm.UiEventController

JSON を UI 反映用のイベントに変換

UI スレッド安全なキューへ追加

🧱 UI スレッド安全設計
Processing Android では 描画と UI 操作はメインスレッドで実行する必要があり、
別スレッド（SocketReceiver など）から直接 UI を操作するとクラッシュします。

そこでこのリポジトリでは キュー方式で UI スレッドへ引き渡す設計 を採用しています：

java
コードをコピーする
// 通信スレッド
uiQueue.add(() -> {
    character.setExpression(...);
    textDisplay.showMessage(...);
});
java
コードをコピーする
// draw() 内
while ((task = uiQueue.poll()) != null) {
    task.run();
}
これにより：

通信処理と UI 表示が安全に分離

Android の描画スレッドポリシーに準拠

回転（縦↔横）や他スレッド競合によるクラッシュ回避

🧪 テスト用 Python 送信スクリプト
開発・デバッグ用の簡易スクリプト例です：

```python
import socket
import json

HOST = "192.168.10.109"  # Android の IP
PORT = 9000

data = {
    "face": 3,
    "text": "デバッグ送信テスト",
    "reset_after": 2
}

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    s.sendall((json.dumps(data) + "\n").encode("utf-8"))
```
### 📌 なぜこの方式なのか
方式	採用理由
TCP + JSON	実装がシンプル、低レイヤーで依存が少ない
TCP サーバ on Android	adb不要、ネットワーク経由でシンプルに受信
Poll Queue UI アップデート	スレッド間の安全を確保

## 🚀 使い方（簡単導入手順）
Android 端末・タブレットに APK をインストール

同一 LAN に PC（や ROS2 環境）を接続

JSON 送信（上記 Python など）で表情・テキストを制御

表示が適切に行われることを確認

### 📌 注意点
Android のネットワークパーミッションを付与

ポート 9000 は他と競合しないこと

JSON の改行は必須（\n）


## 🔊 音声機能のセットアップ (Voice Assets)
このアプリでどうぶつ語を鳴らすには、音声ファイル（.mp3）の配置が必要です。
android/app/src/main/assets/voice/ 内に、以下のフォルダ構成で各ひらがなに対応するローマ字ファイル（例: a.mp3, ko.mp3）を配置してください。

```
assets/
 └── voice/
      ├── voice_high_speed3/ (高音ベースのファイル群)
      ├── voice_high_speed5/ (さらに高音のファイル群)
      └── voice_low_speed2/  (低音ベースのファイル群)
```
※未知の文字や記号が送られてきた場合は、自動的に 500ms の無音（沈黙）として処理される安全設計になっています。

※現在はひらがなのみの対応になっています。