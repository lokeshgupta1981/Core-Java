package com.howtodoinjava.core.array;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CompareArray {
  public static boolean checkEqualityWithStream(final String[] a1,
                                                final String[] a2) {
    if (a1 == a2) {
      return true;
    }

    if (a1 == null || a2 == null
        || a1.length != a2.length) {
      return false;
    }

    return IntStream.range(0, a1.length)
        .allMatch(i -> a1[i].equals(a2[i]));
  }

  public static boolean checkArrayEqualityWithForLoop(final String[] a1,
                                                      final String[] a2) {
    if (a1 == a2) {
      return true;
    }

    if (a1 == null || a2 == null) {
      return false;
    }

    int n = a1.length;
    if (n != a2.length) {
      return false;
    }

    for (int i = 0; i < n; i++) {
      if (!a1[i].equals(a2[i])) {
        return false;
      }
    }
    return true;
  }


  public static void main(final String[] args) {
    String[] a1 = {"A", "B", "C"};
    String[] a2 = {"A", "B", "C"};
    String[] a3 = {"A", "B", "D"};

    String[][] a4 =
        {
            {"A", "B", "C"},
            {"X", "Y", "Z"}
        };

    String[][] a5 =
        {
            {"A", "B", "C"},
            {"X", "Y", "Z"}
        };

    //1
    boolean matches = Arrays.equals(a1, a2);
    System.out.println(matches);

    matches = Arrays.equals(a1, a3);
    System.out.println(matches);

    //2
    matches = Arrays.deepEquals(a4, a5);
    System.out.println(matches);

    //3
    matches = checkEqualityWithStream(a1, a2);
    System.out.println(matches);

    //4
    matches = checkArrayEqualityWithForLoop(a1, a3);
    System.out.println(matches);
  }
}
