package com.howtodoinjava.core.streams.sort;

import java.util.Comparator;
import java.util.stream.Stream;

public class NumberStream {

    public static void main(String[] args) {
        Stream<Integer> numStream = Stream.of(1, 3, 5, 4, 2);

        numStream.sorted().forEach(System.out::println);

        // Recreated stream for demo purpose
        numStream = Stream.of(1, 3, 5, 4, 2);

        numStream.sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }
}
