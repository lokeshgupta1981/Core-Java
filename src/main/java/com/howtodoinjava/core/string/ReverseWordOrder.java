package com.howtodoinjava.core.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;

/**
 * Reverses the order of words in a sentence ("Java is fun to learn" becomes "learn to fun is
 * Java") with split() and String.join(), Collections.reverse(), Java 21 List.reversed(), streams,
 * a backward StringBuilder loop, an in-place two-pointer char array and
 * StringUtils.reverseDelimited(). Also compares word order reversal with reversing the letters of
 * each word and reversing the whole string, and shows how extra spaces and punctuation behave.
 *
 * <p>Run with: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.string.ReverseWordOrder
 */
public class ReverseWordOrder {

  public static void main(String[] args) {
    String sentence = "Java is fun to learn";

    // 1. split() + Collections.reverse() + String.join()
    List<String> words = Arrays.asList(sentence.strip().split("\\s+"));
    Collections.reverse(words);
    String reversed = String.join(" ", words);
    print("Collections.reverse", reversed);

    // 2. Java 21 List.reversed()
    String reversed21 = String.join(" ", List.of(sentence.strip().split("\\s+")).reversed());
    print("List.reversed", reversed21);

    // 3. Three kinds of reversal
    print("word order", reverseWordOrder(sentence));
    print("letters of each word", reverseEachWord(sentence));
    print("each word, manual loop", reverseEachWordManually(sentence));
    print("whole string", new StringBuilder(sentence).reverse().toString());

    // 4. Streams
    print("stream", reverseWithStream(sentence));

    // 5. Backward loop with StringBuilder
    print("backward loop", reverseWithLoop(sentence));

    // 6. In-place two-pointer reversal of a char array
    print("two-pointer", reverseInPlace(sentence));
    print("two-pointer messy", reverseInPlace("  Java   is fun  "));

    // 7. Extra spaces
    String messy = "  Java   is fun  ";
    print("split(\" \")", Arrays.toString(messy.split(" ")));
    print("split(\"\\\\s+\")", Arrays.toString(messy.split("\\s+")));
    print("strip().split(\"\\\\s+\")", Arrays.toString(messy.strip().split("\\s+")));
    print("messy word order", reverseWordOrder(messy));
    print("messy no strip", String.join(" ", List.of(messy.split("\\s+")).reversed()));

    // 8. Punctuation
    String greeting = "Hello, world of Java!";
    print("punctuation as is", reverseWordOrder(greeting));
    print("punctuation kept at end", reverseKeepEndPunctuation(greeting));

    // 9. Apache Commons Lang StringUtils
    print("reverseDelimited", StringUtils.reverseDelimited(sentence, ' '));
    print("reverseDelimited messy", StringUtils.reverseDelimited(messy, ' '));
    print("reverseDelimited dots", StringUtils.reverseDelimited("com.howtodoinjava.core", '.'));
    print("each word with StringUtils",
        StringUtils.reverseDelimited(StringUtils.reverse(sentence), ' '));

    // 10. Edge cases
    print("empty", reverseWordOrder(""));
    print("blank", reverseWordOrder("   "));
    print("one word", reverseWordOrder("Java"));
    print("tabs and newlines", reverseWordOrder("Java\tis\nfun"));
  }

  /** Reverses the order of words and collapses any whitespace run into one space. */
  static String reverseWordOrder(String text) {
    if (text.isBlank()) {
      return "";
    }
    return String.join(" ", List.of(text.strip().split("\\s+")).reversed());
  }

  /** Reverses the letters of each word and keeps the word order. */
  static String reverseEachWord(String text) {
    return Arrays.stream(text.split(" "))
        .map(word -> new StringBuilder(word).reverse())
        .collect(Collectors.joining(" "));
  }

  /** Reverses the letters of each word with charAt() and a backward loop, no reverse() call. */
  static String reverseEachWordManually(String text) {
    String[] words = text.strip().split("\\s+");
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < words.length; i++) {
      String word = words[i];
      for (int j = word.length() - 1; j >= 0; j--) {
        result.append(word.charAt(j));
      }
      if (i < words.length - 1) {
        result.append(' ');
      }
    }
    return result.toString();
  }

  /** Reads the words array from the last index to the first. */
  static String reverseWithStream(String text) {
    String[] words = text.strip().split("\\s+");
    return IntStream.range(0, words.length)
        .mapToObj(i -> words[words.length - 1 - i])
        .collect(Collectors.joining(" "));
  }

  /** Appends the words from the last one to the first one. */
  static String reverseWithLoop(String text) {
    String[] words = text.strip().split("\\s+");
    StringBuilder result = new StringBuilder();
    for (int i = words.length - 1; i >= 0; i--) {
      result.append(words[i]);
      if (i > 0) {
        result.append(' ');
      }
    }
    return result.toString();
  }

  /**
   * Interview version without split(): reverses the whole char array, then reverses each word
   * back, then removes extra spaces. O(n) time.
   */
  static String reverseInPlace(String text) {
    char[] chars = text.toCharArray();
    int n = chars.length;

    // 1. Reverse the whole array: "Java is fun" -> "nuf si avaJ"
    reverse(chars, 0, n - 1);

    // 2. Reverse each word back: "nuf si avaJ" -> "fun is Java"
    int start = 0;
    while (start < n) {
      while (start < n && chars[start] == ' ') start++;
      int end = start;
      while (end < n && chars[end] != ' ') end++;
      reverse(chars, start, end - 1);
      start = end;
    }

    // 3. Copy words left, one space between them
    int write = 0, read = 0;
    while (read < n) {
      while (read < n && chars[read] == ' ') read++;
      if (read == n) break;
      if (write > 0) chars[write++] = ' ';
      while (read < n && chars[read] != ' ') chars[write++] = chars[read++];
    }
    return new String(chars, 0, write);
  }

  /** Swaps characters from both ends toward the middle. */
  static void reverse(char[] chars, int left, int right) {
    while (left < right) {
      char tmp = chars[left];
      chars[left++] = chars[right];
      chars[right--] = tmp;
    }
  }

  /** Reverses the word order but leaves a final '.', '!' or '?' at the end of the sentence. */
  static String reverseKeepEndPunctuation(String text) {
    String body = text.strip();
    String end = "";
    if (!body.isEmpty() && ".!?".indexOf(body.charAt(body.length() - 1)) >= 0) {
      end = body.substring(body.length() - 1);
      body = body.substring(0, body.length() - 1);
    }
    List<String> words = new ArrayList<>();
    for (String word : body.split("\\s+")) {
      words.add(word.replaceAll("[,;:]+$", ""));
    }
    return String.join(" ", words.reversed()) + end;
  }

  private static void print(String label, String value) {
    System.out.printf("%-28s [%s]%n", label, value);
  }
}
