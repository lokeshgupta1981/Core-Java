package com.howtodoinjava.core.streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;
import static java.util.stream.Collectors.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import static java.util.stream.Collectors.*;
import java.util.Map.Entry;
import java.time.YearMonth;
import java.time.LocalDate;

/**
 * Examples for the tutorial "Java Stream groupingBy(): Group, Count and Sum a List".
 * https://howtodoinjava.com/java/stream/collectors-groupingby/
 */
public class GroupingbyNestedMapVsRecordKey {
    static record Employee(String name, String department, String city, int salary) {}

    static final List<Employee> employees = List.of(
    new Employee("Alex", "HR", "Delhi", 100),
    new Employee("Brian", "HR", "Pune", 200),
    new Employee("Charles", "Finance", "Delhi", 900),
    new Employee("David", "Finance", "Delhi", 200),
    new Employee("Edward", "Finance", "Pune", 200),
    new Employee("Frank", "Admin", "Pune", 800),
    new Employee("George", "Admin", "Delhi", 900));
    static record DeptCity(String department, String city) {}
    static record Order(LocalDate date, BigDecimal amount) {}
    static record Dev(String name, List<String> skills) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, List<Employee>> byDept = employees.stream().collect(groupingBy(Employee::department));                      // {Finance=[Employee[name=Charles, ...], ...], HR=[...], Admin=[...]}
            show("byDept", byDept);
            Map<String, Long> countByDept = employees.stream().collect(groupingBy(Employee::department, counting()));                     // {Finance=3, HR=2, Admin=2}
            show("countByDept", countByDept);
            Map<String, Integer> totalByDept = employees.stream().collect(groupingBy(Employee::department, summingInt(Employee::salary))); // {Finance=1300, HR=300, Admin=1700}
            show("totalByDept", totalByDept);
        }
        {
            Map<String, List<Employee>> byDept = employees.stream().collect(groupingBy(Employee::department));   // one list of employees per department
            show("byDept", byDept);
        }
        {
            Map<String, List<String>> namesByDept = employees.stream().collect(groupingBy(Employee::department, mapping(Employee::name, toList())));   // {Finance=[Charles, David, Edward], HR=[Alex, Brian], Admin=[Frank, George]}
            show("namesByDept", namesByDept);
            Map<String, Set<String>> deptsByCity = employees.stream().collect(groupingBy(Employee::city, mapping(Employee::department, toSet())));        // {Delhi=[Finance, HR, Admin], Pune=[Finance, HR, Admin]}
            show("deptsByCity", deptsByCity);
            Map<String, String> joinedByDept = employees.stream().collect(groupingBy(Employee::department, mapping(Employee::name, joining(", "))));     // {Finance=Charles, David, Edward, HR=Alex, Brian, Admin=Frank, George}
            show("joinedByDept", joinedByDept);
        }
        {
            Map<String, Long> countByDept = employees.stream().collect(groupingBy(Employee::department, counting()));   // {Finance=3, HR=2, Admin=2}
            show("countByDept", countByDept);
            Map<String, Long> countByCity = employees.stream().collect(groupingBy(Employee::city, counting()));         // {Delhi=4, Pune=3}
            show("countByCity", countByCity);
        }
        {
            Map<String, Integer> totalByDept = employees.stream().collect(groupingBy(Employee::department, summingInt(Employee::salary)));          // {Finance=1300, HR=300, Admin=1700}
            show("totalByDept", totalByDept);
            Map<String, Double> averageByDept = employees.stream().collect(groupingBy(Employee::department, averagingDouble(Employee::salary)));   // {Finance=433.3333333333333, HR=150.0, Admin=850.0}
            show("averageByDept", averageByDept);
        }
        {
            Map<String, Optional<Employee>> topByDept = employees.stream().collect(groupingBy(Employee::department, maxBy(Comparator.comparingInt(Employee::salary))));   // {Finance=Optional[Employee[name=Charles, ...]], HR=Optional[Employee[name=Brian, ...]], Admin=Optional[Employee[name=George, ...]]}
            show("topByDept", topByDept);
        }
        {
            Map<String, String> topNameByDept = employees.stream()
                    .collect(groupingBy(Employee::department,
            collectingAndThen(maxBy(Comparator.comparingInt(Employee::salary)),
            best -> best.map(Employee::name).orElse("none"))));   // {Finance=Charles, HR=Brian, Admin=George}
        }
        {
            Map<String, Long> above300Filtered = employees.stream().filter(e -> e.salary() > 300).collect(groupingBy(Employee::department, counting()));   // {Finance=1, Admin=2}
            show("above300Filtered", above300Filtered);
            Map<String, Long> above300AllKeys = employees.stream().collect(groupingBy(Employee::department, filtering(e -> e.salary() > 300, counting())));   // {Finance=1, HR=0, Admin=2}
            show("above300AllKeys", above300AllKeys);
        }
        {
            Map<String, IntSummaryStatistics> statsByDept = employees.stream().collect(groupingBy(Employee::department, summarizingInt(Employee::salary)));
            IntSummaryStatistics finance = statsByDept.get("Finance");
            long financeCount = finance.getCount();        // 3
            show("financeCount", financeCount);
            int financeMax = finance.getMax();             // 900
            show("financeMax", financeMax);
            double financeAverage = finance.getAverage();  // 433.3333333333333
            show("financeAverage", financeAverage);
        }
        {
            TreeMap<String, Long> sortedByDept = employees.stream().collect(groupingBy(Employee::department, TreeMap::new, counting()));            // {Admin=2, Finance=3, HR=2}
            show("sortedByDept", sortedByDept);
            LinkedHashMap<String, Long> inInputOrder = employees.stream().collect(groupingBy(Employee::department, LinkedHashMap::new, counting()));   // {HR=2, Finance=3, Admin=2}
            show("inInputOrder", inInputOrder);
            TreeMap<String, List<String>> namesSorted = employees.stream().collect(groupingBy(Employee::department, TreeMap::new, mapping(Employee::name, toList())));   // {Admin=[Frank, George], Finance=[Charles, David, Edward], HR=[Alex, Brian]}
            show("namesSorted", namesSorted);
        }
        {
            Map<String, Map<String, List<String>>> byDeptThenCity = employees.stream()
                    .collect(groupingBy(Employee::department,
            groupingBy(Employee::city, mapping(Employee::name, toList()))));   // {Finance={Delhi=[Charles, David], Pune=[Edward]}, HR={Delhi=[Alex], Pune=[Brian]}, Admin={Delhi=[George], Pune=[Frank]}}
        }
        {
            Map<DeptCity, Long> countByDeptCity = employees.stream().collect(groupingBy(e -> new DeptCity(e.department(), e.city()), counting()));
            long financeDelhi = countByDeptCity.get(new DeptCity("Finance", "Delhi"));   // 2
            show("financeDelhi", financeDelhi);
            int groups = countByDeptCity.size();                                         // 6
            show("groups", groups);
        }
        {
            Map<String, Integer> totalByDept = employees.stream().collect(groupingBy(Employee::department, summingInt(Employee::salary)));
            LinkedHashMap<String, Integer> highestTotalFirst = totalByDept.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));   // {Admin=1700, Finance=1300, HR=300}
        }
        {
            Map<String, Long> bigDepartments = employees.stream().collect(groupingBy(Employee::department, counting()))
                    .entrySet().stream().filter(entry -> entry.getValue() > 2)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));   // {Finance=3}
        }
        {
            Map<Boolean, List<String>> highEarners = employees.stream().collect(partitioningBy(e -> e.salary() > 500, mapping(Employee::name, toList())));       // {false=[Alex, Brian, David, Edward], true=[Charles, Frank, George]}
            show("highEarners", highEarners);
            Map<Boolean, List<String>> nobodyAbove1000 = employees.stream().collect(partitioningBy(e -> e.salary() > 1000, mapping(Employee::name, toList())));  // {false=[Alex, Brian, Charles, David, Edward, Frank, George], true=[]}
            show("nobodyAbove1000", nobodyAbove1000);
            Map<Boolean, List<String>> groupedAbove1000 = employees.stream().collect(groupingBy(e -> e.salary() > 1000, mapping(Employee::name, toList())));     // {false=[Alex, Brian, Charles, David, Edward, Frank, George]}
            show("groupedAbove1000", groupedAbove1000);
        }
        {
            ConcurrentMap<String, Long> concurrentCount = employees.parallelStream().collect(groupingByConcurrent(Employee::department, counting()));   // {Finance=3, HR=2, Admin=2}
            show("concurrentCount", concurrentCount);
        }
        {
            List<Order> orders = List.of(
            new Order(LocalDate.of(2026, 9, 3), new BigDecimal("12.50")),
            new Order(LocalDate.of(2026, 9, 21), new BigDecimal("7.25")),
            new Order(LocalDate.of(2026, 10, 2), new BigDecimal("30.00")));
            TreeMap<YearMonth, BigDecimal> revenueByMonth = orders.stream()
                    .collect(groupingBy(o -> YearMonth.from(o.date()), TreeMap::new, reducing(BigDecimal.ZERO, Order::amount, BigDecimal::add)));   // {2026-09=19.75, 2026-10=30.00}
            TreeMap<YearMonth, Long> ordersByMonth = orders.stream().collect(groupingBy(o -> YearMonth.from(o.date()), TreeMap::new, counting()));   // {2026-09=2, 2026-10=1}
            show("ordersByMonth", ordersByMonth);
        }
        {
            try { Map<String, Long> crash = Stream.of("Delhi", null).collect(groupingBy(city -> city, counting())); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            Map<String, Long> safe = Stream.of("Delhi", null).collect(groupingBy(city -> Objects.requireNonNullElse(city, "Unknown"), counting()));   // {Delhi=1, Unknown=1}
            show("safe", safe);
        }
        {
            List<Dev> devs = List.of(new Dev("Ann", List.of("java", "sql")), new Dev("Bob", List.of("java")));
            Map<String, List<String>> devsBySkill = devs.stream()
                    .flatMap(d -> d.skills().stream().map(skill -> Map.entry(skill, d.name())))
                    .collect(groupingBy(Map.Entry::getKey, TreeMap::new, mapping(Map.Entry::getValue, toList())));   // {java=[Ann, Bob], sql=[Ann]}
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
