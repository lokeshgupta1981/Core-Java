package com.howtodoinjava.core.basic;

import java.util.Objects;
import lombok.AllArgsConstructor;

public class ObjectIdentityString {

  public static void main(String[] args) {
    Record record = new Record(1L, "record name");

    System.out.println(record);

    System.out.println(record != null ? Objects.toIdentityString(record) : "null");

    System.out.println(getIdentityString(record));
  }

  static String getIdentityString(Object object) {
    return object.getClass().getName() + "@"
        + Integer.toHexString(System.identityHashCode(object));
  }
}

@AllArgsConstructor
class Record {
  long id;
  String name;

  @Override
  public String toString() {
    return STR."Record{id=\{id}, name='\{name}\{'\''}\{'}'}";
  }
}
