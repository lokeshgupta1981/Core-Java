package com.howtodoinjava.core;

import com.howtodoinjava.core.streams.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class PrimitiveStreams {
  public static void main(String[] args) {

    //1. of() Method
    IntStream intStream = IntStream.of(1, 2, 3, 4, 5);

    //2. From Primitive Arrays

    int[] array = new int[]{1, 2, 3, 4, 5};
    IntStream stream = Arrays.stream(array);

    long[] array1 = new long[]{1, 2, 3, 4, 5};
    LongStream stream1 = Arrays.stream(array1);

    double[] array2 = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
    DoubleStream stream2 = Arrays.stream(array2);

    //3. Stream.mapToInt()
    List<Integer> integerList = List.of(1, 2, 3, 4, 5);
    IntStream stream4 = integerList.stream().mapToInt(i -> i);

    try (Stream<Employee> streamOfEmployees = getEmployeeStream()) {
      DoubleStream stream5 = streamOfEmployees.mapToDouble(Employee::getSalary);
    }

    //4
    int max = IntStream.of(10, 18, 12, 70, 5)
        .max()
        .getAsInt();

    double avg = IntStream.of(1, 2, 3, 4, 5)
        .average()
        .getAsDouble();

    int sum = IntStream.range(1, 10)
        .sum();

    //5
    IntSummaryStatistics summary =
        IntStream.of(10, 18, 12, 70, 5).summaryStatistics();
    int max1 = summary.getMax();

    //6
    Stream<Integer> boxedStream1 = IntStream.of(1, 2, 3, 4, 5).boxed();
    Stream<Long> boxedStream = LongStream.of(1, 2, 3, 4, 5).boxed();
    Stream<Double> boxedStream2 =
        DoubleStream.of(1.0, 2.0, 3.0, 4.0, 5.0).boxed();
  }

  private static Stream<Employee> getEmployeeStream() {
    List<Employee> empList = new ArrayList<>();
    empList.add(new Employee(1, "A", LocalDate.of(1991, 1, 1), 30000d));
    empList.add(new Employee(2, "B", LocalDate.of(1976, 7, 9), 30000d));
    empList.add(new Employee(3, "C", LocalDate.of(1992, 8, 1), 50000d));
    empList.add(new Employee(4, "D", LocalDate.of(2001, 3, 11), 50000d));

    return empList.stream();
  }
}
