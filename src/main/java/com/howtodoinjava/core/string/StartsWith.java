package com.howtodoinjava.core.string;

import org.apache.commons.lang3.ObjectUtils.Null;
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
  }
}
