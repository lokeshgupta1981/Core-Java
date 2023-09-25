package com.howtodoinjava.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

public class JsonArrayToList {

  public static void main(String[] args) throws IOException, URISyntaxException {

    //Avoid declaring static methods
    JsonArrayToList instance = new JsonArrayToList();
    List<Person> personList;

    URL fileUrl = JsonArrayToList.class.getClassLoader().getResource("data.json");
    Path filePath = Paths.get(fileUrl.toURI());
    String jsonArray = Files.readString(filePath);

    //System.out.println(jsonArray);

    personList = instance.usingJson(jsonArray);

    Assertions.assertEquals(3, personList.size());
    Assertions.assertEquals(1, personList.get(0).getId());
    Assertions.assertEquals("Alex", personList.get(0).getName());
    Assertions.assertEquals(41, personList.get(0).getAge());

    personList = instance.usingGson(jsonArray);

    Assertions.assertEquals(3, personList.size());
    Assertions.assertEquals(1, personList.get(0).getId());
    Assertions.assertEquals("Alex", personList.get(0).getName());
    Assertions.assertEquals(41, personList.get(0).getAge());

    personList = instance.usingJackson(jsonArray);

    Assertions.assertEquals(3, personList.size());
    Assertions.assertEquals(1, personList.get(0).getId());
    Assertions.assertEquals("Alex", personList.get(0).getName());
    Assertions.assertEquals(41, personList.get(0).getAge());
  }

  List<Person> usingJson(String json) {
    JSONArray jsonArray = new JSONArray(json);
    List<Person> personList = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {

      JSONObject jsonPerson = jsonArray.getJSONObject(i);

      long id = jsonPerson.getLong("id");
      String name = jsonPerson.getString("name");
      int age = jsonPerson.getInt("age");

      Person person = new Person(id, name, age);
      personList.add(person);
    }
    return personList;
  }

  List<Person> usingGson(String jsonArray) {
    Gson gson = new Gson();
    Type listType = new TypeToken<List<Person>>() {}.getType();

    List<Person> personList = gson.fromJson(jsonArray, listType);
    return personList;
  }

  List<Person> usingJackson(String jsonArray) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<List<Person>> jacksonTypeReference = new TypeReference<>() {};

    List<Person> personList = objectMapper.readValue(jsonArray, jacksonTypeReference);
    return personList;
  }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person {
  long id;
  String name;
  int age;
}
