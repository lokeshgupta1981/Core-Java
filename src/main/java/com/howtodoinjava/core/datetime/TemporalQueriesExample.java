package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalQuery;

public class TemporalQueriesExample 
{
	public static void main(String[] args) 
	{
		LocalTime now = LocalTime.now();
		LocalDate today = LocalDate.now();
		
		System.out.println("Currently Working :: " + now.query(WorkingHoursQuery));
		System.out.println("Current Financial Quarter :: " + today.query(CurrentQuarterQuery));
	}

	private static final TemporalQuery<Boolean> WorkingHoursQuery = temporal -> {
		LocalTime t = LocalTime.from(temporal);
		return t.compareTo(LocalTime.of(9, 0)) >= 0 
				&& t.compareTo(LocalTime.of(17, 0)) < 0;
	};
	
	private static final TemporalQuery<Integer> CurrentQuarterQuery = temporal -> {
		LocalDate date = LocalDate.from(temporal);
		return (date.getMonthValue() / 3) + 1;
	};
}
