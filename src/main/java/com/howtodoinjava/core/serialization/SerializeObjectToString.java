package com.howtodoinjava.core.serialization;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;

/**
 * Examples for the tutorial "Serialize Object to String in Java: Base64 and JSON".
 * https://howtodoinjava.com/java/serialization/object-to-string/
 */
public class SerializeObjectToString {
    static record Person(int id, String name) implements Serializable {}
    static class SerializationUtils {

        static String objectToString(Serializable obj) throws IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
                out.writeObject(obj);
            }
            return Base64.getEncoder().encodeToString(bytes.toByteArray());
        }

        static <T> T stringToObject(String data, Class<T> type)
        throws IOException, ClassNotFoundException {
            byte[] bytes = Base64.getDecoder().decode(data);
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                in.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                "maxdepth=5;maxbytes=10000;" + type.getName() + ";java.lang.*;!*"));
                return type.cast(in.readObject());
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Person person = new Person(1, "Lokesh");
            String encoded = SerializationUtils.objectToString(person);              // "rO0ABXNy..."
            show("encoded", encoded);
            Person restored = SerializationUtils.stringToObject(encoded, Person.class); // Person[id=1, name=Lokesh]
            show("restored", restored);
            boolean equal = restored.equals(person);                                 // true
            show("equal", equal);
        }
        {
            Map<String, Integer> other = new HashMap<>(Map.of("a", 1));
            String foreign = SerializationUtils.objectToString((Serializable) other);
            try { Person rejected = SerializationUtils.stringToObject(foreign, Person.class); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
