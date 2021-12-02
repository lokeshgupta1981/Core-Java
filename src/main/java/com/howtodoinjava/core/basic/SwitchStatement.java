package com.howtodoinjava.core.basic;

public class SwitchStatement 
{
    public static void main(String[] args) 
    {
        System.out.println("Monday is : " + isWeekDay(Day.TUE));
        System.out.println("Monday is : " + isWeekDay(Day.SUN));
    }
    
    public static Boolean isWeekDay(Day day) 
    {
         Boolean result = switch(day) {
            case MON, TUE, WED, THUR, FRI ->  {
                System.out.println("It's Weekday");
                yield true;
            }
            case SAT, SUN -> {
                System.out.println("It's Weekend");
                yield false;
            }
            default -> throw new IllegalArgumentException("Invalid day: " + day.name());
        };
        return result;
    }
}

enum Day {
    MON, TUE, WED, THUR, FRI, SAT, SUN
}
