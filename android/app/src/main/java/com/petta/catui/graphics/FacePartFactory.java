package com.petta.catui.graphics;

import processing.core.PApplet;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import com.petta.catui.graphics.parts.EmptyPart;

public class FacePartFactory {
    public PApplet app;
    public float s;

    public FacePartFactory(PApplet app, float scale) {
        this.app = app;
        this.s = scale;
    }

    private List<String> candidates(String simple) {
        List<String> list = new ArrayList<>();
        if (simple.contains(".")) {
            list.add(simple);
        } else {
            list.add("com.petta.catui.graphics.decorators." + simple);
            list.add("com.petta.catui.graphics.parts."      + simple);
            list.add("com.petta.catui.decorators."          + simple);
            list.add("com.petta.catui.parts."               + simple);
            list.add("com.petta.catui.core."                + simple);
            list.add(simple);
        }
        return list;
    }

    public UIComponent create(String className, UIComponent decorated) {
        for (String fqcn : candidates(className)) {
            try {
                Class<?> cls = Class.forName(fqcn);
                Constructor<?> c;

                if (decorated != null) {
                    // 🌟 修正：UIComponent を受け取るパターンの探索を追加！
                    c = tryGet(cls, PApplet.class, float.class, UIComponent.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s, decorated);

                    c = tryGet(cls, PApplet.class, float.class, FacePart.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s, (FacePart) decorated);

                    c = tryGet(cls, UIComponent.class);
                    if (c != null) return (UIComponent) c.newInstance(decorated);

                    c = tryGet(cls, FacePart.class);
                    if (c != null) return (UIComponent) c.newInstance((FacePart) decorated);

                    c = tryGet(cls, PApplet.class, float.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s);
                } else {
                    c = tryGet(cls, PApplet.class, float.class);
                    if (c != null) return (UIComponent) c.newInstance(app, s);

                    c = tryGet(cls, PApplet.class);
                    if (c != null) return (UIComponent) c.newInstance(app);

                    c = tryGet(cls);
                    if (c != null) return (UIComponent) c.newInstance();
                }
            } catch (ClassNotFoundException ignore) {
            } catch (Exception e) {
                System.err.println("[Factory] fail: " + fqcn + " -> " + e.getClass().getSimpleName());
            }
        }
        
        // Android Studioの「Logcat」にこの文字が出たら、クラス名が間違っています
        System.err.println("[Factory] fallback ⚠️ 見つかりません: " + className + " -> EmptyPartを返します");
        return new EmptyPart(app, s);
    }

    private Constructor<?> tryGet(Class<?> cls, Class<?>... sig) {
        try { return cls.getConstructor(sig); }
        catch (NoSuchMethodException e) { return null; }
    }
}
