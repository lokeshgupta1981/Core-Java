package com.howtodoinjava.core.streams.sort;

import java.util.Comparator;
import java.util.stream.Stream;

public class StringStream {

    public static void main(String[] args) {
        Stream<String> wordStream = Stream.of("A", "C", "E", "B", "D");

        wordStream.sorted() // ascending
                .forEach(System.out::println);

        wordStream.sorted(Comparator.reverseOrder()) // descending
                .forEach(System.out::println);
    }
}
