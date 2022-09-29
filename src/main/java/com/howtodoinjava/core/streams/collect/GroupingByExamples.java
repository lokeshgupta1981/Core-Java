package com.howtodoinjava.core.streams.collect;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.javatuples.Pair;

public class GroupingByExamples {

  public static void main(String[] args) {
    List<Person> persons = List.of(
        new Person(1, "Alex", 100d, new Department(1, "HR")),
        new Person(2, "Brian", 200d, new Department(1, "HR")),
        new Person(3, "Charles", 900d, new Department(2, "Finance")),
        new Person(4, "David", 200d, new Department(2, "Finance")),
        new Person(5, "Edward", 200d, new Department(2, "Finance")),
        new Person(6, "Frank", 800d, new Department(3, "ADMIN")),
        new Person(7, "George", 900d, new Department(3, "ADMIN")));

    //simple condition

    Map<Department, List<Person>> map = persons.stream().collect(groupingBy(Person::department));
    System.out.println(map);

    Map<Department, List<Integer>> map1 = persons.stream()
        .collect(groupingBy(Person::department, mapping(Person::id, toList())));
    System.out.println(map1);

    //complex condition

    Map<Object, List<Integer>> map4 = persons.stream()
        .collect(groupingBy(person -> new Pair<>(person.salary(), person.department()),
            mapping(Person::id, toList())));
    System.out.println(map4);

    // counting

    Map<Department, Long> map2 = persons.stream()
        .collect(groupingBy(Person::department, counting()));
    System.out.println(map2);

    Map<Double, Long> map3 = persons.stream()
        .collect(groupingBy(Person::salary, counting()));
    System.out.println(map3);

    //average

    Map<Department, Double> map5 = persons.stream()
        .collect(groupingBy(Person::department, averagingDouble(Person::salary)));
    System.out.println(map5);

    //max salaried person

    Map<Department, Optional<Person>> map6 = persons.stream()
        .collect(groupingBy(Person::department, maxBy(Comparator.comparingDouble(Person::salary))));
    System.out.println(map6);

    //filtering java 9

    Map<Department, Long> map7 = persons.stream()
        .filter(p -> p.salary() > 300d)
        .collect(groupingBy(Person::department, counting()));
    System.out.println(map7);

    Map<Department, Long> map8 = persons.stream()
        .collect(groupingBy(Person::department, filtering(p -> p.salary() > 300d, counting())));
    System.out.println(map8);
  }
}

record Person(int id, String name, double salary, Department department) {
}

record Department(int id, String name) {
}