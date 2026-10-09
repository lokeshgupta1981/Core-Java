package com.howtodoinjava.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Examples for the tutorial "Isomorphic Strings in Java".
 * https://howtodoinjava.com/algorithm/isomorphic-strings-in-java-algorithm-code-and-examples/
 */
public class IsomorphicStrings {

  public static boolean isIsomorphicAscii(String s1, String s2) {
    if (s1 == null || s2 == null
        || s1.length() != s2.length()) {
      return false;
    }
    int[] arr1 = new int[256];
    int[] arr2 = new int[256];
    for (int i = 0; i < s1.length(); i++) {
      char c1 = s1.charAt(i);
      char c2 = s2.charAt(i);
      if (arr1[c1] != arr2[c2]) {
        return false;
      }
      arr1[c1] = i + 1;
      arr2[c2] = i + 1;
    }
    return true;
  }

  public static boolean isIsomorphic(String s1, String s2) {
    if (s1 == null || s2 == null) {
      return false;
    }
    int[] cp1 = s1.codePoints().toArray();
    int[] cp2 = s2.codePoints().toArray();
    if (cp1.length != cp2.length) {
      return false;
    }
    Map<Integer, Integer> forward = new HashMap<>();
    Map<Integer, Integer> backward = new HashMap<>();
    for (int i = 0; i < cp1.length; i++) {
      Integer mappedTo = forward.putIfAbsent(cp1[i], cp2[i]);
      Integer mappedFrom = backward.putIfAbsent(cp2[i], cp1[i]);
      if ((mappedTo != null && mappedTo != cp2[i])
          || (mappedFrom != null && mappedFrom != cp1[i])) {
        return false;
      }
    }
    return true;
  }

  public static boolean oneWayOnly(String s1, String s2) {
    if (s1 == null || s2 == null || s1.length() != s2.length()) {
      return false;
    }
    Map<Character, Character> forward = new HashMap<>();
    for (int i = 0; i < s1.length(); i++) {
      Character mappedTo = forward.putIfAbsent(s1.charAt(i), s2.charAt(i));
      if (mappedTo != null && mappedTo != s2.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  public static List<Integer> pattern(String text) {
    Map<Integer, Integer> firstSeen = new HashMap<>();
    List<Integer> result = new ArrayList<>();
    text.codePoints().forEach(cp -> result.add(firstSeen.computeIfAbsent(cp, k -> firstSeen.size())));
    return result;
  }

  public static Map<List<Integer>, List<String>> groupIsomorphic(List<String> words) {
    Map<List<Integer>, List<String>> groups = new LinkedHashMap<>();
    for (String word : words) {
      groups.computeIfAbsent(pattern(word), k -> new ArrayList<>()).add(word);
    }
    return groups;
  }

  public static <T> List<Integer> patternOf(List<T> items) {
    Map<T, Integer> firstSeen = new HashMap<>();
    List<Integer> result = new ArrayList<>();
    for (T item : items) {
      result.add(firstSeen.computeIfAbsent(item, k -> firstSeen.size()));
    }
    return result;
  }

  public static boolean wordPattern(String letters, String sentence) {
    List<String> words = List.of(sentence.trim().split("\\s+"));
    List<String> chars = letters.codePoints().mapToObj(Character::toString).toList();
    return chars.size() == words.size() && patternOf(chars).equals(patternOf(words));
  }

  public static void main(String[] args) {
    System.out.println(isIsomorphicAscii("abbcdd", "qwwcrr"));            // true
    System.out.println(isIsomorphicAscii("aab", "que"));                  // false
    System.out.println(isIsomorphic("a€a", "xyx"));                  // true
    System.out.println(isIsomorphic("😀😀b", "ccd")); // true
    System.out.println(isIsomorphic("Aa", "bb"));                         // false
    System.out.println(oneWayOnly("abc", "xxy"));                         // true (wrong)
    System.out.println(isIsomorphic("abc", "xxy"));                       // false
    System.out.println(pattern("abbcdd"));                                // [0, 1, 1, 2, 3, 3]
    System.out.println(groupIsomorphic(List.of("moon", "deer", "book", "noon", "boob", "sees")));
    System.out.println(wordPattern("abba", "dog cat cat dog"));           // true
    System.out.println(wordPattern("abba", "dog cat cat fish"));          // false
  }
}
