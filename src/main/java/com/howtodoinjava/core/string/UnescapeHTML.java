package com.howtodoinjava.core.string;

import java.util.HashMap;
import org.apache.commons.text.StringEscapeUtils;

public class UnescapeHTML {

  public static void main(String[] args) {
    String escapedString = "&lt;java&gt;public static void main(String[] args) { ... }&lt;/java&gt;";

    //1 Apache Commons Text
    String unEscapedHTML = StringEscapeUtils.unescapeHtml4(escapedString);
    System.out.println(unEscapedHTML);

    //2 Custom Method
    unEscapedHTML = unescapeHTML(escapedString);
    System.out.println(unEscapedHTML);
  }

  private static HashMap<String, String> htmlEntities;

  static {
    htmlEntities = new HashMap<String, String>();
    htmlEntities.put("&lt;", "<");
    htmlEntities.put("&gt;", ">");
    htmlEntities.put("&amp;", "&");
    htmlEntities.put("&quot;", "\"");
    htmlEntities.put("&nbsp;", " ");
    htmlEntities.put("&copy;", "\u00a9");
    htmlEntities.put("&reg;", "\u00ae");
    htmlEntities.put("&euro;", "\u20a0");
  }

  public static final String unescapeHTML(String source) {
    int i, j;

    boolean continueLoop;
    int skip = 0;
    do {
      continueLoop = false;
      i = source.indexOf("&", skip);
      if (i > -1) {
        j = source.indexOf(";", i);
        if (j > i) {
          String entityToLookFor = source.substring(i, j + 1);
          String value = (String) htmlEntities.get(entityToLookFor);
          if (value != null) {
            source = source.substring(0, i)
                + value + source.substring(j + 1);
            continueLoop = true;
          } else if (value == null) {
            skip = i + 1;
            continueLoop = true;
          }
        }
      }
    } while (continueLoop);
    return source;
  }
}


