package com.howtodoinjava.core.datetime;

import java.time.LocalDateTime;

public class CompareLocalDateTimes {
  public static void main(final String[] args) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime pastDate = LocalDateTime.parse("2017-01-14T15:32:56.000");

    boolean isBefore = now.isBefore(pastDate);
    System.out.println("now is before pastDate :: " + isBefore);

    boolean isAfter = now.isAfter(pastDate);
    System.out.println("now is after pastDate :: " + isAfter);

    boolean isEqual = now.isEqual(pastDate);
    System.out.println("now is equal to pastDate :: " + isEqual);

    // 2
    LocalDateTime ldt1 = LocalDateTime.parse("2019-04-28T22:32:38.536");
    LocalDateTime ldt2 = LocalDateTime.parse("2017-01-14T15:32:56.000");

    int diff = ldt1.compareTo(ldt2);

    System.out.println("Compare value = " + diff);

    if (diff > 0) {
      System.out.println("ldt1 is later than ldt2");
    } else if (diff < 0) {
      System.out.println("ldt1 is earlier than ldt2");
    } else {
      System.out.println("both dates are equal");
    }

    // 3

    ldt1 = LocalDateTime.of(2019, 4, 9, 10, 10, 50);
    ldt2 = LocalDateTime.of(2019, 4, 9, 10, 10, 50);
    LocalDateTime ldt3 = LocalDateTime.of(2019, 4, 9, 11, 12, 50);

    System.out.println(ldt1.equals(ldt2));  //true
    System.out.println(ldt1.equals(ldt3));  //false
  }
}
