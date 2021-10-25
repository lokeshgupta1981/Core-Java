package com.howtodoinjava.core.basic;

public class SwitchExpressions 
{
	public static void main(String[] argv) 
	{
		System.out.println(isWeekDayV1_1(Day.MON));
		System.out.println(isWeekDayV1_2(Day.MON));
		System.out.println(isWeekDayV2(Day.MON));
		
		System.out.println(isWeekDayV3("XYZ"));
	}
	
	//1
	
	enum Day {
		MON, TUE, WED, THUR, FRI, SAT, SUN
	}
 
	public static Boolean isWeekDayV1_1 (Day day) 
	{
		return switch(day) {
			case MON, TUE, WED, THUR, FRI -> true;
			case SAT, SUN -> false;
		};
	}
	
	public static Boolean isWeekDayV1_2 (Day day) 
	{
		return switch(day) {
			case MON, TUE, WED, THUR, FRI : yield true;
			case SAT, SUN : yield false;
		};
	}
	
	//2
	
	public static Boolean isWeekDayV2 (Day day) 
	{
		return switch(day) {
			case MON, TUE, WED, THUR, FRI ->
			{ 
				System.out.println("It is WeekDay");
				yield true; 
			}
			case SAT, SUN ->
			{ 
				System.out.println("It is Weekend");
				yield false; 
			}
		};
	}
	
	public static Boolean isWeekDayV3 (String day) 
	{
		return switch(day) {
			case "MON", "TUE", "WED", "THUR", "FRI" -> true;
			case "SAT", "SUN" -> false;
			default -> false;
		};
	}
	
	public static Boolean isWeekDayV4 (Integer day) 
	{
		return switch(day) {
			case 1,2,3 -> true;
			case 4,5 -> false;
			default -> false;
		};
	}
}
