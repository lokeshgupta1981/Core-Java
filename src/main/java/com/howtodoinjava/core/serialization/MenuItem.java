package com.howtodoinjava.core.serialization;

/**
 * A parent class that is NOT Serializable. Its fields are not written; on deserialization
 * Java calls this no-arg constructor to initialize them.
 */
public class MenuItem {

  protected String name;

  public MenuItem() {
    this.name = "unknown";
  }

  public MenuItem(String name) {
    this.name = name;
  }
}
