package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeExample {
  public static void main(String[] args) {
    //Create current date and time
    LocalTime currentTime = LocalTime.now();

    LocalDate currentDate = LocalDate.now();

    LocalDateTime currentDateTime = LocalDateTime.now();

    System.out.println(currentTime);
    System.out.println(currentDate);
    System.out.println(currentDateTime);

    // Parse a date
    String dateString = "2020-04-08 12:30";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime parsedDateTime = LocalDateTime.parse(dateString, formatter);

    System.out.println(parsedDateTime);

    //Format a date
    LocalDateTime myDateObj = LocalDateTime.now();
    System.out.println("Before formatting: " + myDateObj);
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    String formattedDate = myDateObj.format(myFormatObj);
    System.out.println(formattedDate);

  }
}
