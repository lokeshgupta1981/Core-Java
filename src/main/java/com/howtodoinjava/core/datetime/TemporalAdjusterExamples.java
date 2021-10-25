package com.howtodoinjava.core.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class TemporalAdjusterExamples 
{
	public static void main(String[] args) 
	{
		LocalDate localDate = LocalDate.of(2020, 5, 5);
		
		//First day of month
		LocalDate fdom = localDate.with(TemporalAdjusters.firstDayOfMonth());
		System.out.println(fdom);
		
		//Last day of month
		LocalDate ldom = localDate.with(TemporalAdjusters.lastDayOfMonth());
		System.out.println(ldom);
		
		//Next monday
		LocalDate nextMonday = localDate
							.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
	    System.out.println(nextMonday);
		
	    //Custom temporal adjuster with interface
		NextBirthDay nbAdjuster = new NextBirthDay();
		LocalDate nextBirthDay = localDate.with(nbAdjuster);
		System.out.println(nextBirthDay);
		
		//Custom temporal adjuster with lambda
		TemporalAdjuster temporalAdjuster = t -> t.plus(Period.ofDays(7));
		LocalDate nextRunDay = localDate.with(temporalAdjuster);
		System.out.println(nextRunDay);
		
		System.out.println("================");
		
		List<LocalDate> meetingDates = getWeeklyMeetingDates(LocalDate.now(), 5);
		System.out.println(meetingDates);
	}
	
	private static List<LocalDate> getWeeklyMeetingDates(LocalDate localDate, int count) {
		List<LocalDate> dates = new ArrayList<>();
		for(int i = 0; i < count; i++)
		{
			localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
			dates.add(localDate);
		}
	    return dates;
	}
}

class NextBirthDay implements TemporalAdjuster 
{
    @Override
    public Temporal adjustInto(Temporal temporal) 
    {
        return temporal.with(ChronoField.MONTH_OF_YEAR, 11)
        				.with(ChronoField.DAY_OF_MONTH, 22);
    }
}
