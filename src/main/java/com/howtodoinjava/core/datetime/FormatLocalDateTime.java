package com.howtodoinjava.core.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatLocalDateTime {

  public static void main(String[] args) {
    LocalDateTime currentDateTime = LocalDateTime.now();
    System.out.println(currentDateTime);

    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    String formattedDateTime = currentDateTime.format(formatter);
    System.out.println(formattedDateTime);

    DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");;
    String formattedString = currentDateTime.format(customFormat);
    System.out.println(formattedString);
  }

}
