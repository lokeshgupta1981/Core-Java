package com.howtodoinjava.core.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatLocalDateTime {

  final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
  final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void main(String[] args) {
    LocalDateTime currentDateTime = LocalDateTime.now();
    System.out.println(currentDateTime);

    String formattedDateTime = currentDateTime.format(ISO_FORMATTER);
    System.out.println(formattedDateTime);


    String formattedString = currentDateTime.format(CUSTOM_FORMATTER);
    System.out.println(formattedString);
  }

}
