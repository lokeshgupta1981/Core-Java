package com.howtodoinjava.core.serialization;

import java.io.Serial;
import java.io.Serializable;

/**
 * The same singleton without readResolve(): deserialization creates a second instance.
 */
public final class KitchenSettingsNoResolve implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public static final KitchenSettingsNoResolve INSTANCE = new KitchenSettingsNoResolve();

  private KitchenSettingsNoResolve() {
  }
}
