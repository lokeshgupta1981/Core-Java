package com.howtodoinjava.core.streams;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveOrUpdateElements {

  public static void main(String[] args) {

    List<Employee> list = List.of(
        new Employee(1, "Alex", LocalDate.of(1990, 1, 2), 100d),
        new Employee(2, "Alok", LocalDate.of(1992, 1, 2), 200d),
        new Employee(3, "Brian", LocalDate.of(1994, 1, 2), 300d),
        new Employee(4, "Charles", LocalDate.of(1996, 1, 2), 400d));

    List<Employee> modifiedList = list.stream()
        .filter(e -> e.getName().startsWith("A"))
        .collect(Collectors.toList());

    //Employees whose names start with "A"
    System.out.println(modifiedList);

    List<Employee> newList = list.stream()
        .map(e -> {
          e.setSalary(e.getSalary() + 100);
          return e;
        })
        .collect(Collectors.toList());

    //Employees salary incremented by 100
    System.out.println(newList);
  }
}


