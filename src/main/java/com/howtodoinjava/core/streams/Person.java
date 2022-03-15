package com.howtodoinjava.core.streams;

import java.util.Objects;

public record Person(Integer id, String fname, String lname)
  implements Comparable<Person> {

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Person other = (Person) obj;
    return Objects.equals(id, other.id);
  }

  @Override
  public int compareTo(final Person p) {
    return this.id()
      .compareTo(p.id());
  }
}