package com.howtodoinjava.core.collections.cursors;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

public class SpliteratorCursor {

    public static void main(String[] args) {

        List<String> big = Stream.generate(() -> "Hello").limit(30000).toList();

        Spliterator<String> split = big.spliterator(); // 30000

        System.out.println(split.estimateSize());

        Spliterator<String> split1 = split.trySplit();

        System.out.println(split.estimateSize()); // 15000

        System.out.println(split1.estimateSize()); // 15000

        new Thread(() -> split.forEachRemaining(elem -> System.out.println("TH1 " + elem))).start();

        new Thread(() -> split1.forEachRemaining(elem -> System.out.println("TH2 " + elem))).start();

    }

}
