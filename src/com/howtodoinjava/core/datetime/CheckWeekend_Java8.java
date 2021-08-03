package com.howtodoinjava.core.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class CheckWeekend_Java8 {
    public static void main(final String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Is weekend : " + isWeekend(today));
        
        LocalDate someDate = LocalDate.of(2021, 1, 2); //2nd-Jan-2021
        System.out.println("Is weekend : " + isWeekend(someDate));
    }

    public static boolean isWeekend(final LocalDate ld) {
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) {
            return true;
        }
        return false;
    }
}
