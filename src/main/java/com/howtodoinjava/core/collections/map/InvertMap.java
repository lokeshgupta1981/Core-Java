package com.howtodoinjava.core.collections.map;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Invert a Map in Java: Swap Keys and Values, Even With Duplicates".
 * https://howtodoinjava.com/java/collections/invert-java-map/
 */
public class InvertMap {

    public static void main(String[] args) throws Exception {
        {
            Map<String, String> languages = Map.of("en", "English", "fr", "French", "de", "German");
            Map<String, String> codesByName = languages.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            String french = codesByName.get("French");          // "fr"
            show("french", french);
            String german = codesByName.get("German");          // "de"
            show("german", german);
        }
        {
            Map<String, String> languages = new TreeMap<>(Map.of("en", "English", "fr", "French"));
            Map<String, String> inverted = new HashMap<>();
            for (Map.Entry<String, String> entry : languages.entrySet()) {
                inverted.put(entry.getValue(), entry.getKey());
            }
            String code = inverted.get("English");               // "en"
            show("code", code);
            int size = inverted.size();                          // 2
            show("size", size);
        }
        {
            Map<String, String> userRoles = new TreeMap<>(Map.of("alice", "admin", "bob", "editor", "carol", "admin"));
            try { Map<String, String> usersByRole = userRoles.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)); show("usersByRole", usersByRole); } catch (Throwable _t) { System.out.println("usersByRole -> " + _t); }
        }
        {
            Map<String, String> withNullKey = new HashMap<>();
            withNullKey.put(null, "unknown");
            try { Map<String, String> flipped = withNullKey.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)); show("flipped", flipped); } catch (Throwable _t) { System.out.println("flipped -> " + _t); }
        }
        {
            Map<String, String> languages = new TreeMap<>(Map.of("en", "English", "fr", "French", "de", "German"));
            Map<String, String> sameOrder = languages.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a, LinkedHashMap::new));   // {German=de, English=en, French=fr}
            show("sameOrder", sameOrder);
            Map<String, String> sortedByName = languages.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a, TreeMap::new));   // {English=en, French=fr, German=de}
            show("sortedByName", sortedByName);
        }
        {
            Map<String, String> userRoles = new TreeMap<>(Map.of("alice", "admin", "bob", "editor", "carol", "admin"));
            Map<String, String> firstUser = userRoles.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (first, second) -> first, LinkedHashMap::new));   // {admin=alice, editor=bob}
            show("firstUser", firstUser);
            Map<String, String> lastUser = userRoles.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (first, second) -> second, LinkedHashMap::new));   // {admin=carol, editor=bob}
            show("lastUser", lastUser);
            Map<String, String> joined = userRoles.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a + "," + b, LinkedHashMap::new));   // {admin=alice,carol, editor=bob}
            show("joined", joined);
        }
        {
            Map<String, String> userRoles = new TreeMap<>(Map.of("alice", "admin", "bob", "editor", "carol", "admin"));
            Map<String, List<String>> usersByRole = new TreeMap<>();
            for (Map.Entry<String, String> entry : userRoles.entrySet()) {
                usersByRole.computeIfAbsent(entry.getValue(), role -> new ArrayList<>()).add(entry.getKey());
            }
            Map<String, List<String>> result = usersByRole;      // {admin=[alice, carol], editor=[bob]}
            show("result", result);
        }
        {
            Map<String, String> userRoles = new TreeMap<>(Map.of("alice", "admin", "bob", "editor", "carol", "admin"));
            Map<String, List<String>> usersByRole = userRoles.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, TreeMap::new, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));   // {admin=[alice, carol], editor=[bob]}
            show("usersByRole", usersByRole);
            Map<String, Long> usersPerRole = userRoles.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, TreeMap::new, Collectors.counting()));   // {admin=2, editor=1}
            show("usersPerRole", usersPerRole);
        }
        {
            Map<String, String> languages = Map.of("en", "English", "fr", "French");
            Optional<String> code = languages.entrySet().stream().filter(e -> e.getValue().equals("French")).map(Map.Entry::getKey).findFirst();   // Optional[fr]
            show("code", code);
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
