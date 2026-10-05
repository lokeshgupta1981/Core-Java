package com.howtodoinjava.java27;

public class AllFeatures {

  public static void main(String[] args) throws Exception {
    System.out.println("== Runtime defaults (JEP 523, 534, 527)");
    RuntimeDefaults.main(args);
    System.out.println("\n== Lazy constants (JEP 531)");
    LazyGreetings.main(args);
    System.out.println("\n== Primitive patterns (JEP 532)");
    StatusText.main(args);
    System.out.println("\n== Structured concurrency (JEP 533)");
    ProfileLoader.main(args);
    System.out.println("\n== PEM encodings (JEP 538)");
    PemKeys.main(args);
  }
}
