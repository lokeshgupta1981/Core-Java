package com.howtodoinjava.misc;

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
import java.lang.reflect.Type;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Examples for the tutorial "Convert JSON Array to List in Java: Jackson, Gson, Org.json".
 * https://howtodoinjava.com/java/convert-json-array-to-jalist/
 */
public class JsonArrayToListTypeReference {
    static record Book(long id, String title, int year) {}
    static List<Book> readBooks(String json) {
        JSONArray array = new JSONArray(json);
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            books.add(new Book(item.getLong("id"), item.optString("title", "Untitled"), item.getInt("year")));
        }
        return books;
    }
    public static void main(String[] args) throws Exception {
        {
            List<Book> books = readBooks("[{\"id\":1,\"year\":1965}]");          // [Book[id=1, title=Untitled, year=1965]]
            show("books", books);
            try { List<Book> noYear = readBooks("[{\"id\":1,\"title\":\"Dune\"}]"); show("noYear", noYear); } catch (Throwable _t) { System.out.println("noYear -> " + _t); }
            List<Object> generic = new JSONArray("[\"java\",\"json\"]").toList();  // [java, json]
            show("generic", generic);
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
