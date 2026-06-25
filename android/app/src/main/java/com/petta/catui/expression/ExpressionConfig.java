package com.petta.catui.core;

import java.util.function.Function;

public class ExpressionConfig {
  public final String name;
  public final String[] aliases;
  public final char keyBinding;
  public final Function<FacePartFactory, IExpressionState> stateFactory;

  public ExpressionConfig(String name, String[] aliases, char keyBinding,
                          Function<FacePartFactory, IExpressionState> stateFactory) {
    this.name = name;
    this.aliases = aliases;
    this.keyBinding = keyBinding;
    this.stateFactory = stateFactory;
  }
}
