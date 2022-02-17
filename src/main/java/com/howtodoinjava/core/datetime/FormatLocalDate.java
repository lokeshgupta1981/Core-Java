package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class FormatLocalDate {
  public static void main(String[] args) {
    LocalDate today = LocalDate.now();

    String formattedDate = today.format(DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG));
    System.out.println("LONG format: " + formattedDate);

    formattedDate = today.format(DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM));
    System.out.println("MEDIUM format: " + formattedDate);

    formattedDate = today.format(DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT));
    System.out.println("SHORT format: " + formattedDate);

    formattedDate = today.format(DateTimeFormatter
        .ofLocalizedDate(FormatStyle.FULL));
    System.out.println("FULL format: " + formattedDate);
  }
}
