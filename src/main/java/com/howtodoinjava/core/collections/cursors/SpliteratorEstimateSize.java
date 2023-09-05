package com.howtodoinjava.core.collections.cursors;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

public class SpliteratorEstimateSize {

    public static void main(String[] args) {

        List<String> big = Stream.generate(() -> "Hello").limit(10).toList();

        Spliterator<String> split = big.spliterator();

        split.tryAdvance(System.out::println);
        split.tryAdvance(System.out::println);
        split.tryAdvance(System.out::println);
        split.tryAdvance(System.out::println);
        split.tryAdvance(System.out::println);

        System.out.println(split.estimateSize());

    }

}
