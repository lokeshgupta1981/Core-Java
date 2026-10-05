package com.howtodoinjava.gradle9;

public class FruitApp {

  // Java 25: instance main method and the java.lang.IO class
  void main() {
    FruitBasket basket = new FruitBasket();
    basket.add("apple", 5);
    basket.add("banana", 3);
    basket.add("apple", 2);

    IO.println("apple  = " + basket.countOf("apple"));
    IO.println("banana = " + basket.countOf("banana"));
    IO.println("total  = " + basket.total());
    IO.println("Java   = " + Runtime.version().feature());
  }
}
