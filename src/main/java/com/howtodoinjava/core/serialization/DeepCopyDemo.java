package com.howtodoinjava.core.serialization;

import java.util.List;

public class DeepCopyDemo {

  public static void main(String[] args) throws Exception {

    TestClass myClass = new TestClass("Lokesh", "Gupta", List.of("ADMIN", "USER"));

    System.out.println(myClass);

    TestClass deepCopiedInstance = myClass.deepCopy();

    System.out.println(deepCopiedInstance);
  }
}
