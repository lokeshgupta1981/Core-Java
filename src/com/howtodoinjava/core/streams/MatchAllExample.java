package com.howtodoinjava.core.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MatchAllExample 
{
	public static void main(final String[] args) 
    {
        Predicate<Employee> olderThan50 = e -> e.getAge() > 50;
        Predicate<Employee> earningMoreThan40K = e -> e.getSalary() > 40_000;
        Predicate<Employee> combinedCondition = olderThan50.and(earningMoreThan40K);
         
        boolean result = getEmployeeStream().allMatch(combinedCondition);
        System.out.println(result);
    }
     
    private static Stream<Employee> getEmployeeStream()
    {
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(1, "A", 46, 30000));
        empList.add(new Employee(2, "B", 56, 30000));
        empList.add(new Employee(3, "C", 42, 50000));
        empList.add(new Employee(4, "D", 52, 50000));
        empList.add(new Employee(5, "E", 32, 80000));
        empList.add(new Employee(6, "F", 72, 80000));
         
        return empList.stream();
    }
}
