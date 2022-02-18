package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaysBetweenDates {
    public static void main(final String[] args) {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.plusDays(99);

        long diffInDays = ChronoUnit.DAYS.between(date1, date2);

        System.out.println(diffInDays);

        diffInDays = date1.until(date2, ChronoUnit.DAYS);

        System.out.println(diffInDays);
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(2);

        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        
        List<LocalDate> listOfDates = Stream.iterate(startDate, date -> date.plusDays(1))
          .limit(numOfDays)
          .collect(Collectors.toList());
        
        System.out.println(listOfDates);
    }
}
