package com.howtodoinjava.core.basic.record;

public class GenericRecordExample {

  public static void main(String[] args) {
    Container<Integer> intContainer = new Container<>(1, Integer.valueOf(1));
    Container<String> stringContainer = new Container<>(1, "1");

    Integer intValue = intContainer.value();
    String strValue = stringContainer.value();


  }
}

record Container<T>(int id, T value) {
}
