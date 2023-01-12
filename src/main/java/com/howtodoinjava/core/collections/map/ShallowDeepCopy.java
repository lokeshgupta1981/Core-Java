package com.howtodoinjava.core.collections.map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;

public class ShallowDeepCopy {

  public static void main(String[] args) {
    //1 Shallow Copy

    //1.1. Map.clone()

    HashMap<Integer, Person> personMap = new HashMap<>();

    personMap.put(1, new Person(1l, "Alex", LocalDate.of(1990, 01, 01)));
    personMap.put(2, new Person(2l, "Bob", LocalDate.of(1990, 02, 01)));

    //Shallow clone
    HashMap<Integer, Person> clonedMap = (HashMap<Integer, Person>) personMap.clone();

    //Same as employeeMap
    Assertions.assertTrue(Maps.difference(personMap, clonedMap).areEqual());

    //Change a value is clonedMap
    clonedMap.get(1).setName("Charles");

    //Verify content of both maps
    Assertions.assertEquals(clonedMap.get(1).getName(), personMap.get(1).getName());

    //1.2

    // Shallow clone
    Map<Integer, Person> clonedMapWithStream = personMap.entrySet()
        .stream()
        //perform customization
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    Assertions.assertTrue(Maps.difference(personMap, clonedMapWithStream).areEqual());

    //2 Deep Copy

    Gson gson = new Gson();
    String jsonString = gson.toJson(personMap);

    Type type = new TypeToken<HashMap<Integer, Employee>>(){}.getType();
    HashMap<Integer, Employee> deepClonedMap = gson.fromJson(jsonString, type);

    Assertions.assertTrue(Maps.difference(personMap, deepClonedMap).areEqual());
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {

  long id;
  String name;
  LocalDate dateOfBirth;
}
