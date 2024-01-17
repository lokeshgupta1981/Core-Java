package com.howtodoinjava.core.basic.math;

import java.util.function.BinaryOperator;

public class MathDivideExact {

  public static void main(String[] args) {

    int result = 100 / -1;
    System.out.println(result);

    System.out.println(Integer.MAX_VALUE);
    int x = Integer.MIN_VALUE;
    System.out.println(x);
    int quotient1 = x/-1; // -2,147,483,648
    System.out.println(quotient1);

    long y = Integer.MIN_VALUE;
    long quotient2 = y/-1; // 2,147,483,648
    System.out.println(quotient2);

    int quotientExactFine = Math.divideExact(4, -1);
    System.out.println(quotientExactFine);

    int quotientExactEx = Math.divideExact(x, -1);
    System.out.println(quotientExactEx);

    BinaryOperator<Integer> operator = Math::divideExact;
    int quotientExactBo = operator.apply(x, -1);
    System.out.println(quotientExactBo);
  }

}
