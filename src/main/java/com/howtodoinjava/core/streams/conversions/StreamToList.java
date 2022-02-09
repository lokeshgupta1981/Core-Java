package com.howtodoinjava.core.streams.conversions;

import com.howtodoinjava.core.streams.Employee;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamToList {
  public static void main(String[] args) {
    Stream<String> tokenStream = Stream.of("A", "B", "C", "D");  //Stream

    List<String> tokenList1 = tokenStream.toList();

    List<String> tokenList2 =
        tokenStream.collect(Collectors.toUnmodifiableList());

    List<String> tokenList3 = tokenStream.collect(Collectors.toList());

    List<String> tokenList4 = tokenStream
        .collect(Collectors.toCollection(LinkedList::new));

    Stream<Employee> employeeStream = Stream.of(
        new Employee(1, "A", LocalDate.of(2000, 1, 1), 100d),
        new Employee(2, "B", LocalDate.of(2000, 2, 1), 200d),
        new Employee(3, "C", LocalDate.of(2000, 3, 1), 300d),
        new Employee(4, "D", LocalDate.of(2000, 4, 1), 400d),
        new Employee(5, "E", LocalDate.of(2000, 5, 1), 500d),
        new Employee(6, "F", LocalDate.of(2000, 6, 1), 600d));

    List<Employee> employeeList = employeeStream
        .filter(e -> e.getSalary() < 400)
        .collect(Collectors.toList());

    System.out.println(employeeList);

    IntStream infiniteNumberStream = IntStream.iterate(1, i -> i+1);
    List<Integer> finiteList = infiniteNumberStream.limit(10)
        .boxed()
        .collect(Collectors.toList());
  }
}
