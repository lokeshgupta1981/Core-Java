package com.howtodoinjava.core.streams;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MatchAllExample 
{
    public static void main(final String[] args) 
    {
        Predicate<Employee> olderThan50 = e -> LocalDate.now().getYear() - e.getDateOfBirth().getYear() > 50;
        Predicate<Employee> earningMoreThan40K = e -> e.getSalary() > 40_000;
        Predicate<Employee> combinedCondition = olderThan50.and(earningMoreThan40K);
         
        boolean result = getEmployeeStream().allMatch(combinedCondition);
        System.out.println(result);
    }
     
    private static Stream<Employee> getEmployeeStream()
    {
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(1, "A", LocalDate.of(1991, 1, 1), 30000d));
        empList.add(new Employee(2, "B", LocalDate.of(1976, 7, 9), 30000d));
        empList.add(new Employee(3, "C", LocalDate.of(1992, 8, 1), 50000d));
        empList.add(new Employee(4, "D", LocalDate.of(2001, 3, 11), 50000d));
         
        return empList.stream();
    }
}
