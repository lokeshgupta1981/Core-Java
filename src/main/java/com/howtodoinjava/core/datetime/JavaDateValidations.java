package com.howtodoinjava.core.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import org.junit.jupiter.api.Assertions;

public class JavaDateValidations {

  static final String CUSTOM_PATTERN = "MM-dd-yyyy";
  static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CUSTOM_PATTERN);

  public static void main(String[] args) {

    //Default pattern
    LocalDate localDate = LocalDate.parse("2023-02-08"); //08-Feb-2023

    Assertions.assertNotNull(localDate);
    Assertions.assertEquals(8, localDate.getDayOfMonth());
    Assertions.assertEquals(2, localDate.getMonthValue());
    Assertions.assertEquals(2023, localDate.getYear());

    //Custom Pattern
    LocalDate parsedLocalDate = isValidLocalDate("01-26-2023", DATE_TIME_FORMATTER);

    Assertions.assertNotNull(parsedLocalDate);
    Assertions.assertEquals(26, parsedLocalDate.getDayOfMonth());
    Assertions.assertEquals(1, parsedLocalDate.getMonthValue());
    Assertions.assertEquals(2023, parsedLocalDate.getYear());

    /*Date parsedDate = validateAndParseDateJava7("05-26-2020", "MM-dd-yyyy");
    System.out.println(parsedDate);*/

    /*LocalDate parsedLocalDate = validateAndParseDateJava8("05-26-2020", dateFormatter);
    System.out.println(parsedLocalDate);*/
  }

  //Java 8 - Use DateTimeFormatter (thread-safe)
  public static LocalDate isValidLocalDate(String dateStr, DateTimeFormatter dateFormatter) {
    LocalDate date = null;
    try {
      date = LocalDate.parse(dateStr, dateFormatter);
    } catch (DateTimeParseException e) {
      //handle exception
      e.printStackTrace();
    }
    return date;
  }

  //Java 7 - Use SimpleDateFormat (not thread-safe)
  public static Date validateAndParseDateJava7(String dateString,
      String dateFormat) {
    Date date = null;
    DateFormat sdf = new SimpleDateFormat(dateFormat);
    sdf.setLenient(false);
    try {
      date = sdf.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }
}
