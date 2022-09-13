package com.howtodoinjava.core.streams.collect;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamCollectToImmutableCollections {

  public static void main(String[] args) {

    // Java 16

    var unmodifiableList = Stream.of(1, 2, 3, 4, 5).toList();

    System.out.println(unmodifiableList);

    //2 - Java 10
    unmodifiableList = Stream.of(1, 2, 3, 4, 5)
        .collect(Collectors.toUnmodifiableList());

    System.out.println(unmodifiableList);

    var unmodifiableSet = Stream.of(1, 2, 3, 4, 5)
        .collect(Collectors.toUnmodifiableSet());

    System.out.println(unmodifiableSet);

    var unmodifiableMap = Stream.of(1, 2, 3, 4, 5)
        .collect(Collectors.toUnmodifiableMap(i -> i,
            i -> i * i));

    System.out.println(unmodifiableMap);

    //3 - Before Java 10

    unmodifiableList = Stream.of(1, 2, 3, 4, 5)
        .collect(Collectors.collectingAndThen(Collectors.toList(),
            Collections::unmodifiableList));

    System.out.println(unmodifiableList);

    var mutableList = Stream.of(1, 2, 3, 4, 5)
        .collect(Collectors.toList());
    unmodifiableList = Collections.unmodifiableList(mutableList);

    System.out.println(unmodifiableList);

    //4. Guava
    unmodifiableList = Stream.of(1, 2, 3, 4, 5)
        .collect(ImmutableList.toImmutableList());

    System.out.println(unmodifiableList);

    unmodifiableList = Stream.of(1, 2, 3, 4, 5)
        .collect(Collectors.collectingAndThen(Collectors.toList(),
            ImmutableList::copyOf));

    System.out.println(unmodifiableList);
  }
}
