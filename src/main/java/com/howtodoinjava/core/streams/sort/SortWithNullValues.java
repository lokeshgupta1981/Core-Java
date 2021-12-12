package com.howtodoinjava.core.streams.sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.howtodoinjava.core.streams.Employee;

public class SortWithNullValues 
{
    public static void main(final String[] args) 
    {
        // 1. nullsLast
        List<String> names = Arrays.asList("C", null, "B", "D", null, "A", "E");
        
        System.out.println("====SORTED STRINGS WITH NULLS LAST");

        List<String> sortedList = names.stream()
            .sorted(Comparator.nullsLast(Comparator.naturalOrder()))
            .collect(Collectors.toList());

        System.out.println(sortedList);
        
        System.out.println("====SORTED STRINGS WITH NULLS FIRST");

        // 2. nullsFirst
        sortedList = names.stream()
            .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
            .collect(Collectors.toList());

        System.out.println(sortedList);
        
        System.out.println("====SORTED OBJECTS - FEW OBJECTS ARE NULL");

        // 3. Custom Object can be Null
        List<Employee> sortedEmployeeList = getEmployeeListWithNullObjects()
            .stream()
            .sorted(Comparator.nullsFirst(new DateOfBirthComparator()))
            .collect(Collectors.toList());
        
        System.out.println(sortedEmployeeList);
        
        System.out.println("====SORTED OBJECTS - FIELD VALUES ARE NULL - 1");

        // 4. Custom Object's field can be Null
        sortedEmployeeList = getEmployeeListWithNullDates().stream()
            .sorted(Comparator.nullsFirst(new DateOfBirthComparator()))
            .collect(Collectors.toList());

        System.out.println(sortedEmployeeList);
        
        System.out.println("====SORTED OBJECTS - FIELD VALUES ARE NULL - 2");

        // 5. Custom Objects field can be null - without custom comparator
        sortedEmployeeList = getEmployeeListWithNullDates().stream()
            .sorted(Comparator.comparing(Employee::getDateOfBirth,
                Comparator.nullsFirst(Comparator.naturalOrder())))
            .collect(Collectors.toList());

        System.out.println(sortedEmployeeList);
    }

    private static List<Employee> getEmployeeListWithNullObjects() {
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(1, "A", LocalDate.of(1992, 1, 1), 30000d));
        empList.add(null);
        empList.add(new Employee(3, "C", LocalDate.of(1992, 9, 1), 50000d));
        empList.add(null);
        empList.add(new Employee(5, "E", LocalDate.of(1992, 8, 1), 60000d));
        return empList;
    }

    private static List<Employee> getEmployeeListWithNullDates() {
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(1, "A", LocalDate.of(1991, 1, 1), 30000d));
        empList.add(new Employee(2, "B", null, 20000d));
        empList.add(new Employee(3, "C", LocalDate.of(1992, 8, 1), 50000d));
        empList.add(new Employee(4, "D", LocalDate.of(2001, 3, 11), 50000d));
        empList.add(new Employee(5, "E", null, 60000d));
        return empList;
    }
}
