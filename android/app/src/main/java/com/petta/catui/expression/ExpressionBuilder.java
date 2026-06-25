package com.petta.catui.core;
import com.petta.catui.parts.Mouth;

class ExpressionBuilder {
    private final FacePartFactory factory;
    private final CompositeFacePart face = new CompositeFacePart();

    public ExpressionBuilder(FacePartFactory factory) {
        this.factory = factory;
        // デフォルトで鼻とヒゲは常に追加（必要なければ外してOK）
        withNose().withWhiskers();
    }

    /** "earTwitch" -> "EarTwitchDecorator" などへ変換 */
    private String toClassName(String base, String suffix) {
        if (base == null || base.isEmpty()) return "";
        return Character.toUpperCase(base.charAt(0)) + base.substring(1) + suffix;
    }

    // --- パーツの組み立て（Builderは描画しない。作って足すだけ） ---
    public ExpressionBuilder eyes(String type) {
        UIComponent base = factory.create("Eyes", null);                     // parts.Eyes
        face.add(factory.create(toClassName(type, "Decorator"), base));      // decorators.BlinkingDecorator 等
        return this;
    }

    public ExpressionBuilder ears(String type) {
        UIComponent base = factory.create("Ears", null);                     // parts.Ears
        face.add(factory.create(toClassName(type, "Decorator"), base));
        return this;
    }

    public ExpressionBuilder mouth(String type) {
        // Mouth は既存の実装に合わせて直接 new（PApplet, float, String）想定
        face.add(new Mouth(factory.app, factory.s, type));
        return this;
    }

    public ExpressionBuilder effect(String type) {
        // ★効果系（? / Zzz など）は中心座標が必要 → 空パーツを“台座”にしてデコレータ化する
        UIComponent anchor = factory.create("EmptyPart", null);              // parts.EmptyPart
        face.add(factory.create(toClassName(type, "Decorator"), anchor));    // decorators.QuestionMarkDecorator / ZzzDecorator
        return this;
    }

    public ExpressionBuilder withNose() { face.add(factory.create("Nose", null)); return this; }
    public ExpressionBuilder withWhiskers() { face.add(factory.create("Whiskers", null)); return this; }

    public IExpressionState build() {
        return new IExpressionState() {
            @Override public void update() { face.update(); }
            @Override public void draw()   { face.draw();   }
        };
    }
}
