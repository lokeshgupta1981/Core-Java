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

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Collectors teeing() in Java: Combine Two Collectors".
 * https://howtodoinjava.com/java12/collectors-teeing-example/
 */
public class CollectorsTeeingFlow {
    static record Employee(String name, double salary) {}
    static record SalaryRange(Employee lowest, Employee highest) {}
    static record Totals(long count, double sum, double max) {}
    static record Order(String status, double amount) {}
    static record MonthlyReport(long delivered, double spent) {}
    public static void main(String[] args) throws Exception {
        {
            List<Integer> songSeconds = List.of(215, 180, 240, 270);
            Collector<Integer, ?, String> totalAndCount = Collectors.teeing(
            Collectors.summingInt(Integer::intValue),           // 905
            Collectors.counting(),                              // 4
            (total, count) -> total + "s in " + count + " songs");
            String summary = songSeconds.stream().collect(totalAndCount);   // "905s in 4 songs"
            show("summary", summary);
        }
        {
            List<Employee> employees = List.of(
            new Employee("Ana", 100),
            new Employee("Ben", 200),
            new Employee("Chris", 300),
            new Employee("Dana", 400));
            Comparator<Employee> bySalary = Comparator.comparingDouble(Employee::salary);

            Optional<SalaryRange> range = employees.stream()
                    .collect(Collectors.teeing(
            Collectors.minBy(bySalary),
            Collectors.maxBy(bySalary),
            (min, max) -> min.flatMap(lo -> max.map(hi -> new SalaryRange(lo, hi)))));

            String lowest = range.map(r -> r.lowest().name()).orElse("none");    // "Ana"
            show("lowest", lowest);
            String highest = range.map(r -> r.highest().name()).orElse("none");  // "Dana"
            show("highest", highest);
        }
        {
            List<Employee> employees = List.of(
            new Employee("Ana", 100),
            new Employee("Ben", 200),
            new Employee("Chris", 300),
            new Employee("Dana", 400));
            Predicate<Employee> wellPaid = e -> e.salary() > 200;

            Map<String, Object> result = employees.stream()
                    .collect(Collectors.teeing(
            Collectors.filtering(wellPaid, Collectors.toList()),
            Collectors.filtering(wellPaid, Collectors.counting()),
            (list, count) -> Map.of("list", list, "count", count)));

            Object count = result.get("count");     // 2
            show("count", count);
            Object names = result.get("list");      // [Employee[name=Chris, salary=300.0], Employee[name=Dana, salary=400.0]]
            show("names", names);
        }
        {
            List<Employee> employees = List.of(
            new Employee("Ana", 100),
            new Employee("Ben", 200),
            new Employee("Chris", 300),
            new Employee("Dana", 400));
            record PaidGroup(List<String> names, double total) {}

            PaidGroup group = employees.stream()
                    .filter(e -> e.salary() > 200)
                    .collect(Collectors.teeing(
            Collectors.mapping(Employee::name, Collectors.toList()),
            Collectors.summingDouble(Employee::salary),
            PaidGroup::new));

            List<String> groupNames = group.names();     // [Chris, Dana]
            show("groupNames", groupNames);
            double groupTotal = group.total();           // 700.0
            show("groupTotal", groupTotal);
        }
        {
            List<Integer> numbers = IntStream.rangeClosed(1, 10).boxed().toList();

            String stats = numbers.stream()
                    .collect(Collectors.teeing(
            Collectors.averagingInt(Integer::intValue),
            Collectors.summingInt(Integer::intValue),
            (average, sum) -> String.format("Average: %.2f, Sum: %d", average, sum)));

            String printed = stats;                 // "Average: 5.50, Sum: 55"
            show("printed", printed);
        }
        {
            List<Employee> employees = List.of(
            new Employee("Ana", 100),
            new Employee("Ben", 200),
            new Employee("Chris", 300),
            new Employee("Dana", 400));
            Totals totals = employees.stream()
                    .collect(Collectors.teeing(
            Collectors.counting(),
            Collectors.teeing(
            Collectors.summingDouble(Employee::salary),
            Collectors.mapping(Employee::salary, Collectors.maxBy(Double::compare)),
            (sum, max) -> new double[] {sum, max.orElse(0.0)}),
            (count, pair) -> new Totals(count, pair[0], pair[1])));

            String report = totals.toString();      // "Totals[count=4, sum=1000.0, max=400.0]"
            show("report", report);
        }
        {
            List<Integer> none = List.of();
            String emptyStats = none.stream()
                    .collect(Collectors.teeing(
            Collectors.averagingInt(Integer::intValue),
            Collectors.counting(),
            (avg, count) -> avg + " / " + count));
            String emptyShown = emptyStats;        // "0.0 / 0"
            show("emptyShown", emptyShown);
        }
        {
            List<Integer> thousand = IntStream.rangeClosed(1, 1_000).boxed().toList();
            Collector<Integer, ?, String> countAndSum = Collectors.teeing(
            Collectors.counting(),
            Collectors.summingLong(Integer::longValue),
            (count, sum) -> count + " / " + sum);
            String parallelResult = thousand.parallelStream().collect(countAndSum);   // "1000 / 500500"
            show("parallelResult", parallelResult);
        }
        {
            List<Employee> employees = List.of(
            new Employee("Ana", 100),
            new Employee("Ben", 200),
            new Employee("Chris", 300),
            new Employee("Dana", 400));
            DoubleSummaryStatistics salaryStats = employees.stream()
                    .collect(Collectors.summarizingDouble(Employee::salary));
            double avgSalary = salaryStats.getAverage();      // 250.0
            show("avgSalary", avgSalary);
            double maxSalary = salaryStats.getMax();          // 400.0
            show("maxSalary", maxSalary);
        }
        {
            List<Order> orders = List.of(
            new Order("DELIVERED", 40.0),
            new Order("CANCELLED", 15.0),
            new Order("DELIVERED", 25.5));

            MonthlyReport monthly = orders.stream()
                    .filter(o -> o.status().equals("DELIVERED"))
                    .collect(Collectors.teeing(
            Collectors.counting(),
            Collectors.summingDouble(Order::amount),
            MonthlyReport::new));

            long deliveredCount = monthly.delivered();    // 2
            show("deliveredCount", deliveredCount);
            double spentTotal = monthly.spent();          // 65.5
            show("spentTotal", spentTotal);
        }
        {
            List<Employee> employees = List.of(
            new Employee("Ana", 100),
            new Employee("Ben", 200),
            new Employee("Chris", 300),
            new Employee("Dana", 400));
            Map<Boolean, String> bands = employees.stream()
                    .collect(Collectors.groupingBy(e -> e.salary() > 200,
            Collectors.teeing(
            Collectors.counting(),
            Collectors.summingDouble(Employee::salary),
            (count, total) -> count + " people, " + total)));
            String upperBand = bands.get(true);     // "2 people, 700.0"
            show("upperBand", upperBand);
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
