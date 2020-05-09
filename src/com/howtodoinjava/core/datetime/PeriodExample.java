package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class PeriodExample 
{
	public static void main(String[] args) 
	{
		LocalDate startLocalDate = LocalDate.of(2020, 3, 12);
		LocalDate endLocalDate = LocalDate.of(2020, 7, 20);

		Period periodBetween = Period.between(startLocalDate, endLocalDate);
		System.out.println(periodBetween);
		
		System.out.println(periodBetween.getDays());
		System.out.println(periodBetween.getMonths());
		System.out.println(periodBetween.getYears());
		System.out.println(periodBetween.get(ChronoUnit.DAYS));
		//Not supported unit
		//System.out.println(periodBetween.get(ChronoUnit.WEEKS));	
		
		//Get period
		
		Period fromDays = Period.ofDays(150);	// 150 days
		Period fromMonths = Period.ofMonths(4);	// 4 months
		Period fromYears = Period.ofYears(10);	// 10 years
		Period fromWeeks = Period.ofWeeks(15);	// 15 weeks
		
		//Get Period
		Period periodFromUnits = Period.of(20, 3, 20);
		
		//Get Period
		//20 years, 3 months and 20 days
		Period periodFromString1 = Period.parse("P20Y3M20D");

		//365 Days
		Period periodFromString2 = Period.parse("P365D");

		//52 Weeks
		Period periodFromString3 = Period.parse("P52W");
	}
}
