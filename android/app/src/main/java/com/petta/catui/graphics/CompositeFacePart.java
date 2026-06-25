// CompositeFacePart.java
package com.petta.catui.core;
import java.util.*;               // List/ArrayList等を使っていれば
import processing.core.*;   

public class CompositeFacePart implements UIComponent {
  private final List<UIComponent> children = new ArrayList<>();
  public void add(UIComponent c){ children.add(c); }
  public void update(){ for(UIComponent c:children) c.update(); }
  public void draw(){ for(UIComponent c:children) c.draw(); }
}
