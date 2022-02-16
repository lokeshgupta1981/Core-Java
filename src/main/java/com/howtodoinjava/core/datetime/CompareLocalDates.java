package com.howtodoinjava.core.datetime;

import java.time.LocalDate;

public class CompareLocalDates {
  public static void main(final String[] args) {
    LocalDate today = LocalDate.now();
    LocalDate pastDate = LocalDate.parse("2022-01-04");

    boolean isBefore = today.isBefore(pastDate);
    System.out.println("today is before pastDate :: " + isBefore);

    boolean isAfter = today.isAfter(pastDate);
    System.out.println("today is after pastDate :: " + isAfter);

    boolean isEqual = today.isEqual(LocalDate.of(2022, 1, 9));
    System.out.println("today is equal to9th Jan 2022 :: " + isEqual);

    //Compare To 
    int compareValue = today.compareTo(pastDate);

    if (compareValue > 0) {
      System.out.println("today is latter than 4th-Apr-2022");
    } else if (compareValue < 0) {
      System.out.println("today is earlier than 4th-Apr-2022");
    } else {
      System.out.println("both dates are equal");
    }

    // Equals
    isEqual = LocalDate.parse("2019-04-09")
      .equals(LocalDate.of(2019, 4, 9));

    isEqual = LocalDate.parse("2019-04-09")
      .equals(LocalDate.of(2019, 1, 1));
  }
}
