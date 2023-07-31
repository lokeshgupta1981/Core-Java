package com.howtodoinjava.core.streams.collect;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectStreamItemsToMap {

  public static void main(String[] args) {

    // 1 A single map key mapped to a single value
    Stream<Item> stream = Stream.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3")
    );

    Map<Long, Item> map = stream
        .collect(Collectors.toMap(Item::id, Function.identity()));

    System.out.println(map);

    stream = Stream.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3")
    );

    Map<Long, String> mapWithValue = stream
        .collect(Collectors.toMap(Item::id, Item::name));

    System.out.println(mapWithValue);

    stream = Stream.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3")
    );

    LinkedHashMap<Long, String> mapWithValueInInsertionOrder = stream
        .collect(Collectors.toMap(Item::id, Item::name, (o, n) -> n, LinkedHashMap::new));

    System.out.println(mapWithValueInInsertionOrder);

    //2 A single map key mapped to multiple values

    Stream<Item> streamWithDuplicates = Stream.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3-1"),
        new Item(3, "Item3-2"),
        new Item(3, "Item3-3")
    );

    Map<Long, Item> mapWithGrouping = streamWithDuplicates
        .collect(Collectors.toMap(Item::id, Function.identity(), (o, n) -> n));

    System.out.println(mapWithGrouping);

    streamWithDuplicates = Stream.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3-1"),
        new Item(3, "Item3-2"),
        new Item(3, "Item3-3")
    );

    Map<Long, List<Item>> mapWithGroupingNew = streamWithDuplicates
        .collect(Collectors.groupingBy(Item::id));

    System.out.println(mapWithGroupingNew);

    streamWithDuplicates = Stream.of(
        new Item(1, "Item1"),
        new Item(2, "Item2"),
        new Item(3, "Item3-1"),
        new Item(3, "Item3-2"),
        new Item(3, "Item3-3")
    );

    Map<Long, List<String>> mapWithGroupedValues = streamWithDuplicates
        .collect(
            Collectors.groupingBy(Item::id, Collectors.mapping(Item::name, Collectors.toList())));

    System.out.println(mapWithGroupedValues);
  }
}

record Item(long id, String name) {

}
