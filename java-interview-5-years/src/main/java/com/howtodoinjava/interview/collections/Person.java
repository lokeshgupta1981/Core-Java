package com.howtodoinjava.interview.collections;

import java.util.Objects;

/** A mutable key; used to show why HashMap keys must not change. */
public class Person {

  private String name;

  public Person(String name) {
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Person p && Objects.equals(name, p.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
