package com.petta.catui.core;

import processing.core.PApplet;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import com.petta.catui.parts.EmptyPart;

class FacePartFactory {
    final PApplet app;
    final float s;

    FacePartFactory(PApplet app, float scale) {
        this.app = app;
        this.s = scale;
    }

    /** 短いクラス名から FQCN 候補を列挙（decorators → parts → core の順に探索） */
    private List<String> candidates(String simple) {
        List<String> list = new ArrayList<>();
        if (simple.contains(".")) {
            list.add(simple); // 既にFQCN
        } else {
            list.add("com.petta.catui.decorators." + simple);
            list.add("com.petta.catui.parts."      + simple);
            list.add("com.petta.catui.core."       + simple);
            list.add(simple); // 最後の保険
        }
        return list;
    }

    /**
     * 文字列クラス名からインスタンス生成。
     * decorated != null のときはデコレータ想定。複数のコンストラクタ形を順に試す。
     */
    public UIComponent create(String className, UIComponent decorated) {
        for (String fqcn : candidates(className)) {
            try {
                Class<?> cls = Class.forName(fqcn);

                // --- コンストラクタ候補を総当たり ---
                Constructor<?> c;

                if (decorated != null) {
                    // デコレータ側：FacePart / UIComponent / app,s,FacePart ... などを順に
                    c = tryGet(cls, FacePart.class);
                    if (c != null) return (UIComponent) c.newInstance((FacePart) decorated);

                    c = tryGet(cls, UIComponent.class);
                    if (c != null) return (UIComponent) c.newInstance(decorated);

                    c = tryGet(cls, PApplet.class, float.class, FacePart.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s, (FacePart) decorated);

                    c = tryGet(cls, PApplet.class, float.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s);
                } else {
                    // 素のパーツ側： (PApplet,float) → (PApplet) → () の順
                    c = tryGet(cls, PApplet.class, float.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s);

                    c = tryGet(cls, PApplet.class);
                    if (c != null) return (UIComponent) c.newInstance(app);

                    c = tryGet(cls);
                    if (c != null) return (UIComponent) c.newInstance();
                }
            } catch (ClassNotFoundException ignore) {
                // 次の候補へ
            } catch (Exception e) {
                System.err.println("[Factory] fail: " + fqcn + " -> " + e.getClass().getSimpleName());
            }
        }
        System.err.println("[Factory] fallback: " + className + " -> EmptyPart");
        return new EmptyPart(app, s);
    }

    private Constructor<?> tryGet(Class<?> cls, Class<?>... sig) {
        try { return cls.getConstructor(sig); }
        catch (NoSuchMethodException e) { return null; }
    }
}
