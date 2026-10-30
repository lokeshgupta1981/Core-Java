package com.howtodoinjava;

public class Preview {
  public static void main(String[] args) {
    Object value = 42;
    if (value instanceof int number) {
      System.out.println("Primitive pattern matched " + number);
    }
  }
}
