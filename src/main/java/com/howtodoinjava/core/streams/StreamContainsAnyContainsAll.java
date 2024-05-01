package com.howtodoinjava.core.streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;

public class StreamContainsAnyContainsAll {

  public static void main(String[] args) {
    List<Car> cars = Arrays.asList(
        new Car("Dacia", "diesel", 100),
        new Car("Lexus", "gasoline", 300),
        new Car("Ford", "electric", 200)
    );

    Car car1 = new Car("Dacia", "diesel", 100);  //Present in original list
    Car car2 = new Car("Ford", "electric", 80);
    Car car3 = new Car("Chevrolet", "electric", 150);

    Assertions.assertTrue(StreamUtils.contains(cars.stream(), car1));
    Assertions.assertFalse(StreamUtils.contains(cars.stream(), car2));

    Assertions.assertTrue(StreamUtils.containsAny(cars.stream(), car1, car2));
    Assertions.assertFalse(StreamUtils.containsAny(cars.stream(), car2, car3));
    Assertions.assertTrue(StreamUtils.containsAny(cars.stream(), List.of(car1, car2, car3)));
    Assertions.assertTrue(StreamUtils.containsAny(cars.stream(), Stream.of(car1, car2, car3)));


    Assertions.assertTrue(StreamUtils.containsAll(cars.stream(), car1));
    Assertions.assertFalse(StreamUtils.containsAll(cars.stream(), car1, car3));
    Assertions.assertTrue(StreamUtils.containsAll(cars.stream(), List.of(car1)));
    Assertions.assertFalse(StreamUtils.containsAll(cars.stream(), Stream.of(car2)));
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Car {

  String model;
  String fuel;
  Integer capacity;
}

final class StreamUtils {

  private StreamUtils() {
    // Private constructor to prevent instantiation
  }

  public static <T> boolean contains(Stream<T> stream, T element) {
    Objects.requireNonNull(stream);
    Objects.requireNonNull(element);
    return stream.anyMatch(Predicate.isEqual(element));
  }

  public static <T> boolean containsAny(Stream<T> stream, Collection<? extends T> elements) {
    return containsAny(stream, elements.stream());
  }

  public static <T> boolean containsAny(Stream<T> stream, T... elements) {
    return containsAny(stream, Stream.of(elements));
  }

  public static <T> boolean containsAny(Stream<T> stream, Stream<? extends T> elements) {
    Objects.requireNonNull(stream);
    Objects.requireNonNull(elements);
    return elements.anyMatch(stream.collect(Collectors.toSet())::contains);
  }

  public static <T> boolean containsAll(Stream<T> stream, Collection<? extends T> elements) {
    return containsAll(stream, elements.stream());
  }

  public static <T> boolean containsAll(Stream<T> stream, T... elements) {
    return containsAll(stream, Stream.of(elements));
  }

  public static <T> boolean containsAll(Stream<T> stream, Stream<? extends T> elements) {
    Objects.requireNonNull(stream);
    Objects.requireNonNull(elements);
    return elements.allMatch(stream.collect(Collectors.toSet())::contains);
  }
}
