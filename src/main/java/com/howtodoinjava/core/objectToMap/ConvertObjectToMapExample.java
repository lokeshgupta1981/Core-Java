package com.howtodoinjava.core.objectToMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConvertObjectToMapExample {

  public static void main(String[] args) throws IOException {

    Employee employee = new Employee(1, "Alex",
        LocalDate.of(1995, 1, 2),
        List.of("Delhi", "Nevada"),
        List.of(new Role(11, "Finance"), new Role(12, "HR")));

    Map<String, String> employeeMap;

    employeeMap = convertObjectToMapUsingObjectMapper(employee);
    System.out.println(employeeMap);

    Properties employeeProperties = convertObjectToMapUsingJavaPropsMapper(employee);
    System.out.println(employeeProperties);

    employeeMap = convertObjectToMapUsingGson(employee);
    System.out.println(employeeMap);

    System.out.println(toKeyValuePairs(employee));
  }

  public static Map<String, Object> toKeyValuePairs(Object instance) {
    return Arrays.stream(Employee.class.getDeclaredFields())
        .collect(Collectors.toMap(
            Field::getName,
            field -> {
              try {
                Object result = null;
                field.setAccessible(true);
                result = field.get(instance);
                return result != null ? result : "";
              } catch (Exception e) {
                return "";
              }
            }));
  }

  static Map<String, String> convertObjectToMapUsingObjectMapper(Employee employee) {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    return objectMapper.convertValue(employee, Map.class);
  }

  static Properties convertObjectToMapUsingJavaPropsMapper(Employee employee)
      throws IOException {

    JavaPropsMapper mapper = new JavaPropsMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    Properties properties = mapper.writeValueAsProperties(employee);
    return properties;
  }

  static Map<String, String> convertObjectToMapUsingGson(Employee employee) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();

    return gson.fromJson(gson.toJson(employee),
        new TypeToken<HashMap<String, Object>>() {
        }.getType()
    );
  }
}

class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  @Override
  public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
  }

  @Override
  public LocalDate deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    String ldString = jsonElement.getAsString();
    return LocalDate.parse(ldString, DateTimeFormatter.ISO_LOCAL_DATE);
  }
}