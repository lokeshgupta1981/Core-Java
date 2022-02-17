package com.howtodoinjava.core.datetime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FormatZonedDateTime {
  public static void main(String[] args) {
    ZonedDateTime zdt = ZonedDateTime.now();

    String formattedZdt = zdt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    System.out.println(formattedZdt);

    formattedZdt = zdt.format(DateTimeFormatter.ISO_DATE_TIME);
    System.out.println(formattedZdt);

    formattedZdt = zdt.format(DateTimeFormatter.ISO_INSTANT);
    System.out.println(formattedZdt);

    //Custom format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
    formattedZdt = zdt.format(formatter);
    System.out.println(formattedZdt);
  }
}
