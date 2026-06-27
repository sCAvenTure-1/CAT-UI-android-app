package com.petta.catui.comm;

import com.petta.catui.expression.ExpressionState;

public class UiEvent {

    public final ExpressionState face;
    public final String text;
    public final double resetAfter;

    public UiEvent(ExpressionState face, String text, double resetAfter) {
        this.face = face;
        this.text = text;
        this.resetAfter = resetAfter;
    }
}
