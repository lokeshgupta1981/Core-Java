package com.howtodoinjava.core.streams;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MaxDateExample 
{
    public static void main(final String[] args) 
    {
        Comparator<Employee> employeeAgeComparator = Comparator
                            .comparing(Employee::getDateOfBirth);

        Employee youngestEmployee = getEmployeeList().stream()
                                    .max(employeeAgeComparator)
                                    .get();

        System.out.println(youngestEmployee); //Prints Employee 'B'
    }

    private static List<Employee> getEmployeeList() 
    {
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(1, "A", LocalDate.of(1991, 1, 1), 30000d));
        empList.add(new Employee(2, "B", LocalDate.of(1976, 7, 9), 30000d));
        empList.add(new Employee(3, "C", LocalDate.of(1992, 8, 1), 50000d));
        empList.add(new Employee(4, "D", LocalDate.of(2001, 3, 11), 50000d));
        return empList;
    }
}
