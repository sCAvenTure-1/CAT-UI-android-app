// =============================================================================
// CatCharacter.java
// 表情制御の中核クラス（再構築型・Java8対応）
// =============================================================================
package com.petta.catui.core;

import processing.core.PApplet;

public class CatCharacter {

    // 現在の表情状態
    private IExpressionState currentState;
    private ExpressionConfig currentConfig;

    // 表情生成のための Factory / Registry
    private FacePartFactory factory;
    private final ExpressionRegistry registry;

    // =========================================================
    // コンストラクタ
    // =========================================================

    public CatCharacter(PApplet app, float scale) {
        this.factory = new FacePartFactory(app, scale);
        this.registry = new ExpressionRegistry();
        initializeExpressions();
        setExpression("NORMAL");
    }

    // =========================================================
    // 表情定義登録
    // =========================================================

    private void initializeExpressions() {

        registry.register(new ExpressionConfig(
                "NORMAL",
                new String[] { "normal", "ノーマル", "通常" },
                '1',
                f -> new ExpressionBuilder(f)
                        .eyes("blinking")
                        .ears("earTwitch")
                        .mouth("normal")
                        .build()
        ));

        registry.register(new ExpressionConfig(
                "QUESTION",
                new String[] { "question", "疑問", "thinking" },
                '2',
                f -> new ExpressionBuilder(f)
                        .eyes("blinking")
                        .ears("earTwitch")
                        .mouth("happy")
                        .effect("questionMark")
                        .build()
        ));

        registry.register(new ExpressionConfig(
                "HAPPY",
                new String[] { "happy", "嬉しい" },
                '3',
                f -> new ExpressionBuilder(f)
                        .eyes("sparkle")
                        .ears("earFlap")
                        .mouth("surprised")
                        .build()
        ));

        registry.register(new ExpressionConfig(
                "SLEEPING",
                new String[] { "sleeping", "sleep", "寝る" },
                '4',
                f -> new ExpressionBuilder(f)
                        .eyes("sleepEyes")
                        .ears("earDroop")
                        .effect("zzz")
                        .build()
        ));

        registry.register(new ExpressionConfig(
                "SMILE",
                new String[] { "smile", "笑顔" },
                '5',
                f -> new ExpressionBuilder(f)
                        .eyes("NikoEyes")
                        .ears("earFlap")
                        .mouth("nikoSmile")
                        .build()
        ));
    }

    // =========================================================
    // 表情切り替え API
    // =========================================================

    public void setExpression(ExpressionState state) {
        if (state == null) return;

        switch (state) {
            case NORMAL:
                setExpression("NORMAL");
                break;
            case QUESTION:
                setExpression("QUESTION");
                break;
            case HAPPY:
                setExpression("HAPPY");
                break;
            case SLEEPING:
                setExpression("SLEEPING");
                break;
            case SMILE:
                setExpression("SMILE");
                break;
            default:
                break;
        }
    }

    public void setExpression(String name) {
        if (name == null) return;

        ExpressionConfig config = registry.findByName(name.trim());
        if (config != null) {
            applyExpression(config);
        }
    }

    private void applyExpression(ExpressionConfig config) {
        if (config == null || config == currentConfig) return;

        currentConfig = config;
        currentState  = config.stateFactory.apply(factory);
    }

    // =========================================================
    // 状態取得
    // =========================================================

    public String getCurrentExpressionName() {
        return currentConfig != null ? currentConfig.name : "UNKNOWN";
    }

    // =========================================================
    // 更新・描画
    // =========================================================

    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    public void draw() {
        if (currentState != null) {
            currentState.draw();
        }
    }

    // =========================================================
    // 画面回転・リサイズ対応（再構築型）
    // =========================================================

    /**
     * 画面サイズ・向きが変わったときに呼ばれる。
     * Factory を作り直し、現在の表情を安全に再構築する。
     */
    public void onScreenResized(PApplet app, float newScale) {

        // Factory を作り直す
        this.factory = new FacePartFactory(app, newScale);

        // 現在の表情を再構築
        if (currentConfig != null) {
            currentState = currentConfig.stateFactory.apply(factory);
        }
    }
}
