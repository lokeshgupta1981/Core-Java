package com.howtodoinjava.java26.lazy;

import java.util.List;

/** JEP 526 (preview): LazyConstant replaces the StableValue API from Java 25. */
public class LazyConstants {

  static final LazyConstant<PriceList> PRICES = LazyConstant.of(PriceList::load);

  public static void main(String[] args) {
    boolean before = PRICES.isInitialized();
    int apples = PRICES.get().prices().get("apple");
    boolean after = PRICES.isInitialized();
    int againApples = PRICES.get().prices().get("apple");

    List<Integer> squares = List.ofLazy(5, i -> i * i);
    int third = squares.get(3);

    System.out.println("before = " + before + ", apples = " + apples + ", after = " + after
        + ", again = " + againApples + ", loads = " + PriceList.loads + ", squares[3] = " + third);
  }
}
