package com.howtodoinjava.core.streams.collect;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.groupingByConcurrent;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Examples for the article "Java Stream groupingBy(): Group and Aggregate a List".
 * Each block prints the map that the article shows next to the snippet.
 */
public class GroupingByExamples {

  record Employee(String name, String department, String city, int salary) {
  }

  record DeptCity(String department, String city) {
  }

  public static void main(String[] args) {
    List<Employee> employees = List.of(
        new Employee("Alex", "HR", "Delhi", 100),
        new Employee("Brian", "HR", "Pune", 200),
        new Employee("Charles", "Finance", "Delhi", 900),
        new Employee("David", "Finance", "Delhi", 200),
        new Employee("Edward", "Finance", "Pune", 200),
        new Employee("Frank", "Admin", "Pune", 800),
        new Employee("George", "Admin", "Delhi", 900));

    // 1. One classifier: Map<K, List<T>>
    Map<String, List<Employee>> byDept = employees.stream()
        .collect(groupingBy(Employee::department));
    System.out.println("byDept = " + byDept);

    // 2. Keep only the names in each group
    Map<String, List<String>> namesByDept = employees.stream()
        .collect(groupingBy(Employee::department, mapping(Employee::name, toList())));
    System.out.println("namesByDept = " + namesByDept);

    // 3. Count per group
    Map<String, Long> countByDept = employees.stream()
        .collect(groupingBy(Employee::department, counting()));
    System.out.println("countByDept = " + countByDept);

    Map<Integer, Long> countBySalary = employees.stream()
        .collect(groupingBy(Employee::salary, counting()));
    System.out.println("countBySalary = " + countBySalary);

    // 4. Sum and average per group
    Map<String, Integer> totalByDept = employees.stream()
        .collect(groupingBy(Employee::department, summingInt(Employee::salary)));
    System.out.println("totalByDept = " + totalByDept);

    Map<String, Double> averageByDept = employees.stream()
        .collect(groupingBy(Employee::department, averagingDouble(Employee::salary)));
    System.out.println("averageByDept = " + averageByDept);

    // 5. Max per group, with Optional
    Map<String, Optional<Employee>> topByDept = employees.stream()
        .collect(groupingBy(Employee::department, maxBy(Comparator.comparingInt(Employee::salary))));
    System.out.println("topByDept = " + topByDept);

    Map<String, String> topNameByDept = employees.stream()
        .collect(groupingBy(Employee::department,
            collectingAndThen(maxBy(Comparator.comparingInt(Employee::salary)),
                best -> best.map(Employee::name).orElse("none"))));
    System.out.println("topNameByDept = " + topNameByDept);

    // 6. Multi-level grouping and a record key
    Map<String, Map<String, List<String>>> byDeptThenCity = employees.stream()
        .collect(groupingBy(Employee::department,
            groupingBy(Employee::city, mapping(Employee::name, toList()))));
    System.out.println("byDeptThenCity = " + byDeptThenCity);

    Map<DeptCity, Long> countByDeptCity = employees.stream()
        .collect(groupingBy(e -> new DeptCity(e.department(), e.city()), counting()));
    System.out.println("countByDeptCity = " + countByDeptCity);

    // 7. Choosing the map type
    TreeMap<String, Long> sortedByDept = employees.stream()
        .collect(groupingBy(Employee::department, TreeMap::new, counting()));
    System.out.println("sortedByDept = " + sortedByDept);

    LinkedHashMap<String, Long> inInputOrder = employees.stream()
        .collect(groupingBy(Employee::department, LinkedHashMap::new, counting()));
    System.out.println("inInputOrder = " + inInputOrder);

    // 8. Sorting the result by value
    LinkedHashMap<String, Integer> highestTotalFirst = totalByDept.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    System.out.println("highestTotalFirst = " + highestTotalFirst);

    // 9. partitioningBy: always two keys
    Map<Boolean, List<String>> highEarners = employees.stream()
        .collect(partitioningBy(e -> e.salary() > 500, mapping(Employee::name, toList())));
    System.out.println("highEarners = " + highEarners);

    Map<Boolean, List<String>> nobodyAbove1000 = employees.stream()
        .collect(partitioningBy(e -> e.salary() > 1000, mapping(Employee::name, toList())));
    System.out.println("nobodyAbove1000 = " + nobodyAbove1000);

    // 10. Filter before vs. filter inside the group
    Map<String, Long> above300Filtered = employees.stream()
        .filter(e -> e.salary() > 300)
        .collect(groupingBy(Employee::department, counting()));
    System.out.println("above300Filtered = " + above300Filtered);

    Map<String, Long> above300AllKeys = employees.stream()
        .collect(groupingBy(Employee::department, filtering(e -> e.salary() > 300, counting())));
    System.out.println("above300AllKeys = " + above300AllKeys);

    // 11. groupingByConcurrent on a parallel stream
    ConcurrentMap<String, Long> concurrentCount = employees.parallelStream()
        .collect(groupingByConcurrent(Employee::department, counting()));
    System.out.println("concurrentCount = " + concurrentCount);
  }
}
