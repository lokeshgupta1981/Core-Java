package com.howtodoinjava.interview.design;

/** Lazy, thread-safe singleton with the holder idiom. */
public final class Config {

  private Config() {
  }

  private static final class Holder {
    private static final Config INSTANCE = new Config();
  }

  public static Config getInstance() {
    return Holder.INSTANCE;            // Holder is initialized on the first call only
  }
}
