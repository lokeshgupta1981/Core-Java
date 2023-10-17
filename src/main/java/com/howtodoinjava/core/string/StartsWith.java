package com.howtodoinjava.core.string;

import java.util.regex.Pattern;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;

public class StartsWith {

  public static void main(String[] args) {
    String blogName = "howtodoinjava.com";

    Assertions.assertTrue(blogName.startsWith("how"));
    Assertions.assertTrue(blogName.startsWith("howto"));
    Assertions.assertFalse(blogName.startsWith("hello"));

    Assertions.assertFalse(blogName.startsWith("^h"));

    Assertions.assertThrows(NullPointerException.class, () -> {
      blogName.startsWith(null);
    });

    Assertions.assertTrue(blogName.startsWith("h", 0));
    Assertions.assertFalse(blogName.startsWith("do", 0));
    Assertions.assertTrue(blogName.startsWith("do", 5));

    String text = "The quick brown fox jumps over the lazy dog";

    boolean result = StringUtils.startsWithAny(text,"The", "A", "An");
    System.out.println(result);

    //3
    String[] prefixes = { "The", "A", "An" };
    String regex = "^(?:" + String.join("|", prefixes) + ").*";

    if (Pattern.compile(regex).matcher(text).matches()) {
      System.out.println("The string starts with one of the specified values.");
    } else {
      System.out.println("The string does not start with any of the specified values.");
    }

    //4
    for (String prefix : prefixes) {
      if (text.startsWith(prefix)) {
        System.out.println("The string starts with '" + prefix + "'.");
        break; // Exit the loop when a match is found
      }
    }

    result = StringUtils.startsWithIgnoreCase(text, "the");

    System.out.println(result);   // true

  }
}
