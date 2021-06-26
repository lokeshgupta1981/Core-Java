package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.util.Date;

public class FindNextPrevDay 
{
	private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24L;
	
	public static void main(String[] args) 
	{
		Date today = new Date();
		
		System.out.println("Today     :: " + findNextDay(today));
		System.out.println("Next date :: " + findNextDay(today));
		System.out.println("Prev date :: " + findPrevDay(today));
		
		LocalDate todayDate = LocalDate.now();
		
		System.out.println("Today     :: " + todayDate);
		System.out.println("Next date :: " + findNextDay(todayDate));
		System.out.println("Prev date :: " + findPrevDay(todayDate));
	}
	
	private static Date findNextDay(Date date)
	{
		return new Date(date.getTime() + MILLIS_IN_A_DAY);
	}
	
	private static Date findPrevDay(Date date)
	{
		return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}
	
	private static LocalDate findNextDay(LocalDate localdate)
	{
		return localdate.plusDays(1);
	}
	
	private static LocalDate findPrevDay(LocalDate localdate)
	{
		return localdate.minusDays(1);
	}
}

