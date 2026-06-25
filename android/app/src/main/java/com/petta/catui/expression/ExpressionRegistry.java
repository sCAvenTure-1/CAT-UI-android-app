package com.petta.catui.core;

import java.util.HashMap;
import java.util.Map;

public class ExpressionRegistry {
  private final Map<String, ExpressionConfig> byName = new HashMap<>();
  private final Map<Character, ExpressionConfig> byKey = new HashMap<>();

  public void register(ExpressionConfig c) {
    if (c == null) return;
    byName.put(c.name.toLowerCase(), c);
    byKey.put(c.keyBinding, c);
    for (String a : c.aliases) byName.put(a.toLowerCase(), c);
  }

  public ExpressionConfig findByName(String name) {
    return (name == null) ? null : byName.get(name.toLowerCase());
  }

  public ExpressionConfig findByKey(char key) {
    return byKey.get(key);
  }
}
