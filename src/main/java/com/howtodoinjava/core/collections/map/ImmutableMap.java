package com.howtodoinjava.core.collections.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.stream.Collectors.*;

public class ImmutableMap {

  public static void main(String[] args) {

    //1
    Map<String, String> immutableMap = Map.of("key1", "value1");
    //throws java.lang.UnsupportedOperationException
    //immutableMap.put("key2", "value2");

    //2
    Map<String, String> map = new HashMap<>();
    map.put("key 1", "value 1");
    map.put("key 2", "value 2");
    map.put("key 3", "value 3");

    Map<String, String> imap = Map.copyOf(map);
    //modify the original map
    map.put("key 4", "value 4");

    System.out.println(map);  // {key 4=value 4, key 3=value 3, key 2=value 2, key 1=value 1}
    System.out.println(imap); // {key 3=value 3, key 1=value 1, key 2=value 2}

    imap = Collections.unmodifiableMap(map);
    //immutableMap.put("key2", "value2");

    System.out.println(imap);

    imap = Stream.of(
            entry("key 1", "value 1"),
            entry("key 2", "value 2"),
            entry("key 3", "value 3"))
        .collect(toUnmodifiableMap(e->e.getKey(), e->e.getValue()));

    imap.put("key 4", "value 4");

    System.out.println(imap);

    imap = Map.ofEntries(
        entry("key 1", null),
        entry("key 2", "value 2"),
        entry("key 3", "value 3"),
        entry("key 4", "value 4"),
        entry("key 5", "value 5"),
        entry("key 6", "value 6"),
        entry("key 7", "value 7"),
        entry("key 8", "value 8"),
        entry("key 9", "value 9"),
        entry("key 10", "value 10"),
        entry("key 11", "value 11"),
        entry("key 12", "value 12")
    );

    System.out.println(imap);
  }
}
