package com.howtodoinjava.core.serialization;

/**
 * A non-serializable parent WITHOUT a no-arg constructor. Reading a serializable child of
 * this class fails with InvalidClassException: no valid constructor.
 */
public class MenuItemNoDefault {

  protected final String name;

  public MenuItemNoDefault(String name) {
    this.name = name;
  }
}
