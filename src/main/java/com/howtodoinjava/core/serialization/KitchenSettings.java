package com.howtodoinjava.core.serialization;

import java.io.Serial;
import java.io.Serializable;

/**
 * A serializable singleton. readResolve() replaces the deserialized copy with INSTANCE,
 * so the JVM keeps a single KitchenSettings object.
 */
public final class KitchenSettings implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public static final KitchenSettings INSTANCE = new KitchenSettings();

  private KitchenSettings() {
  }

  @Serial
  private Object readResolve() {
    return INSTANCE;
  }
}
