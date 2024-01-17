package com.howtodoinjava.core.basic;

public class StricfpExample {

  public static void main(String[] args) {
    runMethod();
  }

  strictfp public static void runMethod() {
    double x = 5.899999;
    double y = 13.888345;
    double z = 14.463534545;

    double m1 = (x * y) * z;   // 1185.1596894396725
    double m2 = (x * (y * z)); // 1185.1596894396728

    System.out.println(m1);
    System.out.println(m2);

    System.out.println(m1 == m2);  //false
  }
}
