package com.howtodoinjava.cloning;

/**
 * Overrides clone() but forgets to implement Cloneable, so Object.clone() throws CloneNotSupportedException.
 */
public class Podcast {

  private final String host;

  public Podcast(String host) {
    this.host = host;
  }

  @Override
  public Podcast clone() throws CloneNotSupportedException {
    return (Podcast) super.clone();
  }

  public String getHost() {
    return host;
  }
}
