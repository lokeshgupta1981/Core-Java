package com.howtodoinjava.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializeObjectToString {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    //1 Using Core Java

    Person person = new Person(1, "Lokesh");
    String encodedString;
    Person deserializedPerson;

    /*String encodedString = SerializationUtils.objectToString(person);
    System.out.println(encodedString);

    Person deserializedPerson = (Person) SerializationUtils.stringToObject(encodedString);
    System.out.println(deserializedPerson);*/

    //2 Using Jackson
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(person);
    System.out.println(json);

    deserializedPerson = mapper.readValue(json, Person.class);
    System.out.println(deserializedPerson);

    //3 Gson
    Gson gson = new Gson();
    json = gson.toJson(person);
    System.out.println(json);

    deserializedPerson = gson.fromJson(json, Person.class);
    System.out.println(deserializedPerson);

    //4 Base64 encoded json
    String base64EncodedString = Base64.getEncoder().encodeToString(json.getBytes());
    System.out.println(base64EncodedString);

    String decodedJson = new String(Base64.getDecoder().decode(base64EncodedString));
    deserializedPerson = mapper.readValue(decodedJson, Person.class);
    System.out.println(deserializedPerson);

    //xml

    XmlMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(person);
    System.out.println(xml);

    deserializedPerson = xmlMapper.readValue(xml, Person.class);
    System.out.println(deserializedPerson);
  }

  record Person(long id, String name) {
  }
}

class SerializationUtils {

  public static String objectToString(Serializable obj) throws IOException {

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ois = new ObjectOutputStream(baos)) {

      ois.writeObject(obj);
      return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
  }

  public static Object stringToObject(String obj)
      throws IOException, ClassNotFoundException {

    byte[] data = Base64.getDecoder().decode(obj);
    try (ObjectInputStream ois = new ObjectInputStream(
        new ByteArrayInputStream(data))) {
      return ois.readObject();
    }
  }
}
