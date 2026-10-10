package com.howtodoinjava.core.basic;

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
import java.math.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;

/**
 * Examples for the tutorial "Convert an Object to Map in Java: Jackson, Gson and Reflection".
 * https://howtodoinjava.com/java/basics/convert-object-to-map/
 */
public class ObjectToMapConversion {
    static public record Author(String name, String country) {
    }
    static public record Book(String title, int pages, LocalDate published, List<String> tags, Author author) {
    }
    static Map<String, Object> recordToMap(Record source) throws ReflectiveOperationException {
        Map<String, Object> map = new LinkedHashMap<>();
        for (RecordComponent component : source.getClass().getRecordComponents()) {
            map.put(component.getName(), component.getAccessor().invoke(source));
        }
        return map;
    }
    static Map<String, Object> fieldsToMap(Object source) throws IllegalAccessException {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Class<?> type = source.getClass(); type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                map.putIfAbsent(field.getName(), field.get(source));
            }
        }
        return map;
    }
    public static class Shelf {
        private static int count = 0;
        private final String room = "B2";
        private final String label = null;
    }
    public static void main(String[] args) throws Exception {
        {
            Book book = new Book("Dune", 412, LocalDate.of(1965, 8, 1), List.of("sci-fi", "classic"), new Author("Herbert", "US"));
            Map<String, Object> map = recordToMap(book);
            String text = map.toString();             // "{title=Dune, pages=412, published=1965-08-01, tags=[sci-fi, classic], author=Author[name=Herbert, country=US]}"
            show("text", text);
            Object author = map.get("author");        // Author[name=Herbert, country=US]
            show("author", author);
        }
        {
            Map<String, Object> shelf = fieldsToMap(new Shelf());
            String shelfText = shelf.toString();      // "{room=B2, label=null}"
            show("shelfText", shelfText);
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
