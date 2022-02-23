package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ExtractDayMonthYear {
  public static void main(final String[] args) {
    LocalDate today = LocalDate.now();
    
    int day = today.getDayOfMonth();
    int month = today.getMonthValue();
    int year = today.getYear();
    
    System.out.println(day);
    System.out.println(month);
    System.out.println(year);
    
    //Java 7
    
      Date date = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      
      day = calendar.get(Calendar.DAY_OF_MONTH);
      month = calendar.get(Calendar.MONTH);
      year = calendar.get(Calendar.YEAR);
    
    System.out.println(day);
    System.out.println(month);
    System.out.println(year);
  }
}
