package com.howtodoinjava.core.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DuplicateWords {

  public static void main(String[] args) {
    String sentence = "alex brian charles alex charles david eric david";

    //1 Streams
    List<String> wordsList = Arrays.stream(sentence.split(" ")).collect(Collectors.toList());

    Set<String> tempSet = new HashSet<>();

    List<String> duplicateWords = wordsList.stream()
        .filter(w -> !tempSet.add(w))
        .collect(Collectors.toList());

    System.out.println(duplicateWords);

    Map<String, Integer> wordsMapWithCount = wordsList.stream()
        .collect(Collectors.toMap(Function.identity(), word -> 1, Math::addExact));

    System.out.println(wordsMapWithCount);

    Map<String, Integer> dupWordsMapWithCount = wordsMapWithCount.entrySet()
        .stream().filter(e -> e.getValue() > 1)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    System.out.println(dupWordsMapWithCount);

    System.out.println("==================================");

    //2 Collections

    wordsList.clear();
    tempSet.clear();
    duplicateWords.clear();
    dupWordsMapWithCount.clear();

    wordsList = Arrays.asList(sentence.split(" "));

    for (String word : wordsList) {
      if (!tempSet.add(word)) {
        duplicateWords.add(word);
      }
    }
    System.out.println(duplicateWords);

    for (String word : duplicateWords) {

      dupWordsMapWithCount.put(word, Collections.frequency(wordsList, word));
    }
    System.out.println(dupWordsMapWithCount);
  }
}
