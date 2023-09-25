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

    URL fileUrl = JsonArrayToList.class.getClassLoader().getResource("data.json");
    Path filePath = Paths.get(fileUrl.toURI());
    String jsonArray = Files.readString(filePath);

    System.out.println(jsonArray);

    usingJson(jsonArray);
    usingGson(jsonArray);
    usingJackson(jsonArray);
  }

  static void usingJson(String json) {
    JSONArray jsonArray = new JSONArray(json);
    List<Person> arrayList = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {

      JSONObject jsonPerson = jsonArray.getJSONObject(i);

      long id = jsonPerson.getLong("id");
      String name = jsonPerson.getString("name");
      int age = jsonPerson.getInt("age");

      Person person = new Person(id, name, age);
      arrayList.add(person);
    }

    Assertions.assertEquals(3, arrayList.size());
    Assertions.assertEquals(1, arrayList.get(0).getId());
    Assertions.assertEquals("Alex", arrayList.get(0).getName());
    Assertions.assertEquals(41, arrayList.get(0).getAge());
  }

  static void usingGson(String jsonArray) {
    Gson gson = new Gson();
    Type listType = new TypeToken<List<Person>>() {}.getType();

    List<Person> gsonList = gson.fromJson(jsonArray, listType);
    Assertions.assertEquals(3, gsonList.size());
    Assertions.assertEquals(1, gsonList.get(0).getId());
    Assertions.assertEquals("Alex", gsonList.get(0).getName());
    Assertions.assertEquals(41, gsonList.get(0).getAge());
  }

  static void usingJackson(String jsonArray) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<List<Person>> jacksonTypeReference = new TypeReference<>() {};

    List<Person> jacksonList = objectMapper.readValue(jsonArray, jacksonTypeReference);
    Assertions.assertEquals(3, jacksonList.size());
    Assertions.assertEquals(1, jacksonList.get(0).getId());
    Assertions.assertEquals("Alex", jacksonList.get(0).getName());
    Assertions.assertEquals(41, jacksonList.get(0).getAge());
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
