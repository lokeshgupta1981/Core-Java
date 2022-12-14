package com.howtodoinjava.core.streams.predicates;

import com.howtodoinjava.core.streams.Employee;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TestPredicates {

  public static void main(String[] args) {
    Predicate<Employee> isAdult = e -> e.getAge() > 18;
    Predicate<Employee> isMale = p -> p.getName().equalsIgnoreCase("M");
    Predicate<Employee> isAdultAndMale = isAdult.and(isMale);
    Predicate<Employee> isAdultOrMale = isAdult.or(isMale);
    Predicate<Employee> isMinor = isAdult.negate();

    BiPredicate<Integer, String> isAdultMale = (p1, p2) -> p1 > 18 && p2.equalsIgnoreCase("M");

    List<Employee> employeeList = List.of();

    List<Employee> maleEmployeeList = employeeList.stream().filter(isMale).toList();
    List<Employee> adultMalesList = employeeList.stream()
        .filter(x -> isAdultMale.test(x.getAge(), x.getName()))
        .toList();
  }

}
