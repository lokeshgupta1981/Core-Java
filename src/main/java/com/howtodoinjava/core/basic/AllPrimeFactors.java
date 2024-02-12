package com.howtodoinjava.core.basic;

import java.util.ArrayList;
import java.util.List;

public class AllPrimeFactors {

  public static void main(String[] args) {
    System.out.println(primeFactors(90));
    System.out.println(primeFactors(190));
    System.out.println(primeFactors(350));
  }

  public static List<Integer> primeFactors(int v) {
    List<Integer> factorsList = new ArrayList<>();
    int s = 2;

    while (v > 1) {
      // each perfect division give us a prime factor
      if (v % s == 0) {
        factorsList.add(s);
        v = v / s;
      } else {
        s++;
      }
    }

    return factorsList;
  }
}
