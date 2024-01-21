package com.howtodoinjava.core.datetime;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class ConvertBetweenMonthNameAndNumber {

  public static void main(String[] args) {

    // MONTH NUMBER -> NAME

    System.out.println(monthNumberToAbbr(1));
    System.out.println(monthNumberToFullName(1));
    System.out.println(monthNumberToName(1));

    // MONTH ABBR -> Number

    System.out.println(monthNameToNumber("January"));
    System.out.println(monthAbbrToNumber("Jan"));

    // MONTH NAME -> NAME

    System.out.println(monthAbbrToFullName("Jan"));
  }

  public static String monthNumberToAbbr(int monthNumber) {
    return Month.of(monthNumber).getDisplayName(
        TextStyle.SHORT, Locale.getDefault()
    );
  }

  public static String monthNumberToFullName(int monthNumber) {
    return Month.of(monthNumber).getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    );
  }

  public static String monthNumberToName(int monthNumber) {
    return Month.of(monthNumber).name();
  }

  public static int monthNameToNumber(String monthName) {
    return Month.valueOf(monthName.toUpperCase()).getValue();
  }

  public static int monthAbbrToNumber(String abbreviation) {
    Optional<Month> monthOptional = Arrays.stream(Month.values())
        .filter(month -> month.name().substring(0, 3).equalsIgnoreCase(abbreviation))
        .findFirst();

    return monthOptional.orElseThrow(IllegalArgumentException::new).getValue();
  }

  public static String monthAbbrToFullName(String abbreviation) {
    Optional<Month> monthOptional = Arrays.stream(Month.values())
        .filter(month -> month.name().substring(0, 3).equalsIgnoreCase(abbreviation))
        .findFirst();

    return monthOptional.orElseThrow(IllegalArgumentException::new)
        .getDisplayName(TextStyle.FULL, Locale.getDefault());
  }

}
