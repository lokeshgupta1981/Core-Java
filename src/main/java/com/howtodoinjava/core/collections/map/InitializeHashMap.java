package com.howtodoinjava.core.collections.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InitializeHashMap {

  public static void main(String[] args) {

    // Empty maps
    HashMap<String, String> map1 = new HashMap<>();
    HashMap<String, String> map2 = new HashMap<>(10);
    HashMap<String, String> map3 = HashMap.newHashMap(10);

    // Pre-populated maps
    HashMap<String, String> map4 = new HashMap<>(
        Map.ofEntries(
            Map.entry("key1", "value1"),
            Map.entry("key2", "value3"))
    );

    Map<String, String> map5  = new HashMap<>() {{
      put("key1", "value1");
      put("key2", "value2");
    }};

    HashMap<String, String> map6 = new HashMap<>(
        Map.of("key1", "value1", "key2", "value3")
    );

    // Immutable maps
    Map<String, String> singleEntryMap = Collections.singletonMap("key", "value");
    Map<String, String> emptyMap = Collections.emptyMap();

    //Collecting Stream to Map
    //More examples: https://howtodoinjava.com/java8/collect-stream-to-map/
    Stream<String[]> stream = Stream.of(new String[][] {
        { "Hello", "World" },
        { "John", "Doe" },
    });
    Map<String, String> map7 = stream.collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
  }
}
