package com.howtodoinjava.core.string;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

public class StringAlignment extends Format {

  public enum Alignment {
    LEFT, CENTER, RIGHT,
  }

  private final Alignment currentAlignment;
  private final int maxChars;

  public StringAlignment(int maxChars, Alignment align) {
    if (!isValidAlignment(align)) {
      throw new IllegalArgumentException("Invalid justification argument.");
    }
    if (maxChars < 0) {
      throw new IllegalArgumentException("maxChars must be positive.");
    }
    this.currentAlignment = align;
    this.maxChars = maxChars;
  }

  @Override
  public StringBuffer format(Object input, StringBuffer where, FieldPosition ignore) {
    String s = input.toString();
    List<String> strings = splitInputString(s);

    strings.forEach(wanted -> {
      switch (currentAlignment) {
        case RIGHT:
          pad(where, maxChars - wanted.length());
          where.append(wanted);
          break;

        case CENTER:
          int toAdd = maxChars - wanted.length();
          pad(where, toAdd / 2);
          where.append(wanted);
          pad(where, toAdd - toAdd / 2);
          break;

        case LEFT:
          where.append(wanted);
          pad(where, maxChars - wanted.length());
          break;
      }
      where.append("\n");
    });

    return where;
  }

  @Override
  public Object parseObject(String source, ParsePosition pos) {
    return source;
  }

  public String format(String s) {
    return format(s, new StringBuffer(), null).toString();
  }

  private void pad(StringBuffer to, int howMany) {
    to.append(" ".repeat(Math.max(0, howMany)));
  }

  private boolean isValidAlignment(Alignment align) {
    return align == Alignment.LEFT || align == Alignment.CENTER || align == Alignment.RIGHT;
  }

  private List<String> splitInputString(String str) {
    List<String> list = new ArrayList<>();
    if (str == null) return list;
    for (int i = 0; i < str.length(); i += maxChars) {
      int endIndex = Math.min(i + maxChars, str.length());
      list.add(str.substring(i, endIndex));
    }
    return list;
  }
}

