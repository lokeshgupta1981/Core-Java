package com.howtodoinjava.core.string;

import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Assertions;

public class TitleCase {

  public static void main(String[] args) {

    //WordUtils
    String string = "how TO Do iN JAVA";
    String capitalizedString = WordUtils.capitalizeFully(string);
    Assertions.assertEquals("How To Do In Java", capitalizedString);

    String string1 = "how-TO Do$iN JAVA";
    String capitalizedString1 = WordUtils.capitalizeFully(string1, new char[]{' ', '-', '$'});
    Assertions.assertEquals("How-To Do$In Java", capitalizedString1);

    //Split the string
    Assertions.assertEquals("Null", titleCase(null));
    Assertions.assertEquals("", titleCase(""));
    Assertions.assertEquals("How To Do In Java", titleCase("HOW to DO IN jAVA"));
    Assertions.assertEquals("How To Do In Java", titleCase("how to do in java"));
  }

  public static String titleCase(String inputString) {

    if (inputString == null) {
      return "Null";
    }

    if (StringUtils.isBlank(inputString)) {
      return "";
    }

    if (StringUtils.length(inputString) == 1) {
      return inputString.toUpperCase();
    }

    StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());

    Stream.of(inputString.split(" ")).forEach(stringPart -> {

      if (stringPart.length() > 1) {
        resultPlaceHolder.append(stringPart.substring(0, 1).toUpperCase())
            .append(stringPart.substring(1).toLowerCase());
      } else {
        resultPlaceHolder.append(stringPart.toUpperCase());
      }

      resultPlaceHolder.append(" ");
    });
    return StringUtils.trim(resultPlaceHolder.toString());
  }
}

