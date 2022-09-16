package com.howtodoinjava.core.streams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

public class FilterAMap {

  public static void main(String[] args) {
    Map<Integer, User> usersMap = Map.of(
        1, new User(1, "Alex"),
        2, new User(2, "Allen"),
        3, new User(3, "Brian"),
        4, new User(4, "Bob"),
        5, new User(5, "Charles"),
        6, new User(6, "David"),
        7, new User(7, "Don"),
        8, new User(8, "Dave"));

    //1
    List<Integer> idList = List.of(1,3,5,7);

    Map<Integer, User> filteredMap1 = usersMap.entrySet()
        .stream()
        .filter(entry -> idList.contains(entry.getKey()))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    System.out.println(filteredMap1);

    Map<Integer, User> filteredMap2 = usersMap.entrySet()
        .stream()
        .filter(entry -> idList.contains(entry.getValue().getId()))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    System.out.println(filteredMap2);

    //2

    List<User> usersList1 = usersMap.values()
        .stream()
        .filter(user -> idList.contains(user.getId()))
        .collect(Collectors.toUnmodifiableList());

    System.out.println(usersList1);

    //3
    List<User> usersList2 = new ArrayList<>();

    usersMap.entrySet().forEach( entry -> {
      if(idList.contains(entry.getValue().getId())) {
        usersList2.add(entry.getValue());
      }
    });

    System.out.println(usersList2);
  }

}

@Data
@AllArgsConstructor
class User {
  Integer id;
  String name;
}
