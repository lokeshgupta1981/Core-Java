package com.howtodoinjava.core.string;

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

/**
 * Examples for the tutorial "Java String Interview Questions With Answers for Java 25".
 * https://howtodoinjava.com/interview-questions/string-questions/
 */
public class StringPoolLiteralVsNewString {
    static String reverse(String text) {
        if (text == null) {
            return null;
        }
        char[] chars = text.toCharArray();
        for (int i = 0, j = chars.length - 1; i < j; i++, j--) {
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
    }
    static boolean isPalindrome(String text) {
        String s = text.toLowerCase();
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }
        return true;
    }
    static boolean isAnagram(String first, String second) {
        char[] a = first.toLowerCase().toCharArray();
        char[] b = second.toLowerCase().toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }
    static Optional<Character> firstUnique(String text) {
        Map<Character, Long> counts = text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        return counts.entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();
    }
    static void permute(String prefix, String rest, List<String> result) {
        if (rest.isEmpty()) {
            result.add(prefix);
            return;
        }
        for (int i = 0; i < rest.length(); i++) {
            permute(prefix + rest.charAt(i), rest.substring(0, i) + rest.substring(i + 1), result);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String a = "java";
            String b = "java";
            String c = new String("java");
            boolean samePooled = a == b;                                  // true (one pooled object)
            show("samePooled", samePooled);
            boolean sameObject = a == c;                                  // false (c is a new object)
            show("sameObject", sameObject);
            boolean sameText = a.equals(c);                               // true
            show("sameText", sameText);
            boolean afterIntern = a == c.intern();                        // true
            show("afterIntern", afterIntern);
            String upper = a.toUpperCase();                               // "JAVA" (a stays "java")
            show("upper", upper);
            String reversed = new StringBuilder(a).reverse().toString();  // "avaj"
            show("reversed", reversed);
        }
        {
            int String = 10;                                  // 10 (compiles, but never do this)
            show("String", String);
            boolean isFinal = java.lang.reflect.Modifier.isFinal(java.lang.String.class.getModifiers());   // true
            show("isFinal", isFinal);
        }
        {
            String city = "pune";
            String upperCity = city.toUpperCase();      // "PUNE"
            show("upperCity", upperCity);
            String full = city.concat(", India");       // "pune, India"
            show("full", full);
            boolean unchanged = city.equals("pune");    // true
            show("unchanged", unchanged);
        }
        {
            String s1 = "java";
            String s2 = "java";
            String s3 = new String("java");
            boolean s1s2 = s1 == s2;          // true (both use the pooled object)
            show("s1s2", s1s2);
            boolean s1s3 = s1 == s3;          // false (s3 is a new object)
            show("s1s3", s1s3);
        }
        {
            String fromInput = new String("india");
            String pooled = fromInput.intern();
            boolean sameAsLiteral = pooled == "india";       // true
            show("sameAsLiteral", sameAsLiteral);
            boolean original = fromInput == "india";         // false (fromInput is unchanged)
            show("original", original);
        }
        {
            String full = "howto" + "doinjava";
            boolean compileTime = full == "howtodoinjava";       // true (constant expression)
            show("compileTime", compileTime);

            String part = "howto";
            String joined = part + "doinjava";
            boolean runTime = joined == "howtodoinjava";         // false (built at run time)
            show("runTime", runTime);

            final String constPart = "howto";
            String joinedConst = constPart + "doinjava";
            boolean finalConst = joinedConst == "howtodoinjava";  // true (constPart is a constant)
            show("finalConst", finalConst);
        }
        {
            String fromDb = new String("ADMIN");
            boolean byReference = fromDb == "ADMIN";               // false
            show("byReference", byReference);
            boolean byValue = fromDb.equals("ADMIN");              // true
            show("byValue", byValue);
            try { boolean nullSafe = "ADMIN".equals(null); show("nullSafe", nullSafe); } catch (Throwable _t) { System.out.println("nullSafe -> " + _t); }
            boolean bothNull = Objects.equals(null, null);         // true
            show("bothNull", bothNull);
        }
        {
            int before = "apple".compareTo("banana");               // -1 ('a' - 'b')
            show("before", before);
            int same = "apple".compareTo("apple");                  // 0
            show("same", same);
            int upperFirst = "Apple".compareTo("apple");            // -32 ('A' - 'a')
            show("upperFirst", upperFirst);
            int shorter = "app".compareTo("apple");                 // -2 (length 3 - 5)
            show("shorter", shorter);
            int ignoreCase = "Apple".compareToIgnoreCase("apple");  // 0
            show("ignoreCase", ignoreCase);
            boolean equalText = "Apple".equalsIgnoreCase("APPLE");  // true
            show("equalText", equalText);
        }
        {
            Map<String, Integer> stock = new HashMap<>();
            stock.put("apple", 5);
            int found = stock.get(new String("apple"));    // 5 (equal text, different object)
            show("found", found);
            int hash = "apple".hashCode();                 // 93029210
            show("hash", hash);
        }
        {
            StringBuilder sb = new StringBuilder("java");
            sb.append(" 25").insert(0, "Hello ").replace(0, 5, "Hi");
            String text = sb.toString();                    // "Hi java 25"
            show("text", text);
            int size = sb.length();                         // 10
            show("size", size);
        }
        {
            List<String> names = List.of("Lokesh", "Alex", "Maria");

            StringBuilder line = new StringBuilder();
            for (String name : names) {
                line.append(name).append(';');
            }
            String built = line.toString();                 // "Lokesh;Alex;Maria;"
            show("built", built);
            String joined = String.join(", ", names);       // "Lokesh, Alex, Maria"
            show("joined", joined);
        }
        {
            String version = "25.0.1";
            String plain = version.replace(".", "_");              // "25_0_1"
            show("plain", plain);
            String regex = version.replaceAll(".", "_");           // "______" (dot matches any character)
            show("regex", regex);
            String escaped = version.replaceAll("\\.", "_");       // "25_0_1"
            show("escaped", escaped);
            String first = version.replaceFirst("\\.", "_");       // "25_0.1"
            show("first", first);
            String removed = "java".replace("a", "");              // "jv"
            show("removed", removed);
        }
        {
            String row = "Lokesh,37,,";
            int dropped = row.split(",").length;            // 2 (trailing empty values removed)
            show("dropped", dropped);
            int kept = row.split(",", -1).length;           // 4
            show("kept", kept);
            int wrongDot = "a.b.c".split(".").length;       // 0 (every character is a separator)
            show("wrongDot", wrongDot);
            int rightDot = "a.b.c".split("\\.").length;     // 3
            show("rightDot", rightDot);
        }
        {
            boolean emptyCheck = "  ".isEmpty();           // false
            show("emptyCheck", emptyCheck);
            boolean blankCheck = "  ".isBlank();           // true
            show("blankCheck", blankCheck);
            boolean bothEmpty = "".isEmpty();              // true
            show("bothEmpty", bothEmpty);
            String stripped = "  java  ".strip();          // "java"
            show("stripped", stripped);
        }
        {
            String size = "M";
            String label = switch (size) {
                case "S" -> "Small";
                case "M" -> "Medium";
                case "L" -> "Large";
                default -> "Unknown";
            };
            boolean medium = label.equals("Medium");        // true
            show("medium", medium);
        }
        {
            String byBuilder = new StringBuilder("howtodoinjava").reverse().toString();   // "avajniodotwoh"
            show("byBuilder", byBuilder);
            String byLoop = reverse("howtodoinjava");                                     // "avajniodotwoh"
            show("byLoop", byLoop);
        }
        {
            boolean level = isPalindrome("level");      // true
            show("level", level);
            boolean madam = isPalindrome("Madam");      // true
            show("madam", madam);
            boolean java = isPalindrome("java");        // false
            show("java", java);
        }
        {
            boolean anagram = isAnagram("Listen", "Silent");   // true
            show("anagram", anagram);
            boolean notAnagram = isAnagram("java", "jav");     // false
            show("notAnagram", notAnagram);
        }
        {
            Map<Character, Integer> counts = new TreeMap<>();
            for (char ch : "banana".toCharArray()) {
                counts.merge(ch, 1, Integer::sum);
            }
            String result = counts.toString();             // "{a=3, b=1, n=2}"
            show("result", result);
        }
        {
            Optional<Character> unique = firstUnique("swiss");   // Optional[w]
            show("unique", unique);
            Optional<Character> none = firstUnique("aabb");       // Optional.empty
            show("none", none);
        }
        {
            List<String> perms = new ArrayList<>();
            permute("", "abc", perms);
            String all = perms.toString();                 // "[abc, acb, bac, bca, cab, cba]"
            show("all", all);
        }
        {
            String sentence = "how to do in java";
            String reversedWords = Arrays.stream(sentence.split(" "))
                    .map(word -> new StringBuilder(word).reverse().toString())
                    .collect(Collectors.joining(" "));          // "woh ot od ni avaj"
        }
        {
            char[] pin = {'1', '2', '3', '4'};
            for (char ch : pin) {
                ch = '0';
            }
            String stillThere = new String(pin);                                    // "1234" (the loop changed only copies)
            show("stillThere", stillThere);
            Arrays.fill(pin, '\0');
            long remaining = new String(pin).chars().filter(ch -> ch != 0).count(); // 0
            show("remaining", remaining);
        }
        {
            String word = "caf\u00e9";
            int chars = word.length();                                        // 4
            show("chars", chars);
            int utf8Bytes = word.getBytes(StandardCharsets.UTF_8).length;     // 5 (the accented e needs 2 bytes)
            show("utf8Bytes", utf8Bytes);
            int latin1Bytes = word.getBytes(StandardCharsets.ISO_8859_1).length;   // 4
            show("latin1Bytes", latin1Bytes);
            String back = new String(word.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);   // "caf\u00e9"
            show("back", back);
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
