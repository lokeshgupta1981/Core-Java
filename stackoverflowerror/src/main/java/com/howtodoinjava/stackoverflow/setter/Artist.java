package com.howtodoinjava.stackoverflow.setter;

public class Artist {

  private String name;

  public String getName() {
    return name;
  }

  // Bug: calls itself instead of assigning the field
  public void setName(String name) {
    setName(name);
  }

  // Fixed version
  public void setNameFixed(String name) {
    this.name = name;
  }
}
