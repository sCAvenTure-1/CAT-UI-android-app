package com.petta.catui.core;

/**
 * 表情状態を表す enum
 * 通信・ROS2・UI 内部で共通に使う
 */
public enum ExpressionState {

    NORMAL(0),
    QUESTION(1),
    HAPPY(2),
    SLEEPING(3),
    SMILE(4);

    private final int id;

    ExpressionState(int id) {
        this.id = id;
    }

    /** 通信用のID */
    public int getId() {
        return id;
    }

    /** ID → enum 変換（不正値は NORMAL） */
    public static ExpressionState fromId(int id) {
        for (ExpressionState s : values()) {
            if (s.id == id) return s;
        }
        return NORMAL;
    }
}
