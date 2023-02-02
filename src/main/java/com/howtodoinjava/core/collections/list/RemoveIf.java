package com.howtodoinjava.core.collections.list;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;

public class RemoveIf {

  public static void main(String[] args) {
    ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    numbers.removeIf(number -> number % 2 == 0);

    System.out.println(numbers);

    ArrayList<Employee> employees = new ArrayList<>();

    employees.add(new Employee(1l, "Alex", ""));
    employees.add(new Employee(4l, "Brian", ""));
    employees.add(new Employee(3l, "Piyush", ""));
    employees.add(new Employee(5l, "Charles", ""));
    employees.add(new Employee(2l, "Pawan", ""));

    Predicate<Employee> condition = employee -> employee.getFirstName().startsWith("P");

    employees.removeIf(condition);

    System.out.println(employees);
  }


}
