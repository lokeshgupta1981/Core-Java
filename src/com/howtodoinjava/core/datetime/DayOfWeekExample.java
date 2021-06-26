package com.howtodoinjava.core.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@SuppressWarnings("unused")
public class DayOfWeekExample {
	public static void main(String[] args) 
	{
		LocalDate today = LocalDate.now();
		
		System.out.println( today.getDayOfWeek() );
		System.out.println( today.getDayOfWeek().getValue() );
		
		String textValue = getDayString(today, Locale.getDefault());
		
		System.out.println(textValue);
		
		textValue = getDayString(today, Locale.GERMAN);
		
		System.out.println(textValue);
	}
	
	public static String getDayString(LocalDate date, Locale locale) {
	    DayOfWeek day = date.getDayOfWeek();
	    return day.getDisplayName(TextStyle.FULL, locale);
	}
}
