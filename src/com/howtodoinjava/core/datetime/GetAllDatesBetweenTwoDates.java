package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class GetAllDatesBetweenTwoDates 
{
	public static void main(String[] args) 
	{
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusMonths(2);
		
		// Java 9
		
		List<LocalDate> listOfDates = startDate.datesUntil(endDate)
											.collect(Collectors.toList());
		
		System.out.println(listOfDates.size());
		
		// Java 8 - Method 1
		
		long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
		 
		List<LocalDate> listOfDates1 = Stream.iterate(startDate, date -> date.plusDays(1))
										 	.limit(numOfDays)
										 	.collect(Collectors.toList());
		
		System.out.println(listOfDates1.size());
		
		// Java 8 - Method 2
		 
		List<LocalDate> listOfDates2 = LongStream.range(0, numOfDays)
											.mapToObj(startDate::plusDays)
										 	.collect(Collectors.toList());
		
		System.out.println(listOfDates2.size());
		
		// Java 7
		Date todaysDate = new Date();
		Date anotherDate = new Date(todaysDate.getTime() + 61*24*60*60*1000l);
		
		List<Date> listOfDates3 = getDaysBetweenDates(todaysDate, anotherDate);
		
		System.out.println(listOfDates3.size());
	}
	
	public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Date> dates = new ArrayList<>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
}
