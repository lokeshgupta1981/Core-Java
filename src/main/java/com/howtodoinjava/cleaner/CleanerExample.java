package com.howtodoinjava.cleaner;

public class CleanerExample {

  public static void main(final String[] args) throws Exception {

    //1 Implicit Cleanup
    try (final ClassAccessingResource clazzInstance
        = new ClassAccessingResource()) {
      // Safely use the resource
      clazzInstance.businessOperation();
      clazzInstance.anotherBusinessOperation();
    }

    //2 Explicit Cleanup
    final ClassAccessingResource clazzInstance = new ClassAccessingResource();
    clazzInstance.businessOperation();
    clazzInstance.anotherBusinessOperation();
    clazzInstance.close();
  }
}
