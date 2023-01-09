package com.howtodoinjava.core.string;

import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Assertions;

public class SplitCSV {

  public static void main(String[] args) {

    //Split
    String blogName = "how, to, do, in, java";
    String[] tokenArray = blogName.split("\\s*,\\s*");

    Assertions.assertArrayEquals(new String[]{"how", "to", "do", "in", "java"}, tokenArray);
    Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, "a, b,c".split("\\s*,\\s*"));
    Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, "a,   b,    c".split("\\s*,\\s*"));

    List<String> tokenList = Arrays.asList(tokenArray);
    ArrayList<String> tokenArrayList = new ArrayList(Arrays.asList(tokenArray));

    //Join
    List<String> list = Arrays.asList("how", "to", "do", "in", "java");
    String result = String.join(",", list);    //delimited by comma
    Assertions.assertEquals("how,to,do,in,java", result);

    //Guava
    Splitter niceCommaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();

    //["how", "to", "do", "in", "java"]
    Iterable<String> tokensList = niceCommaSplitter.split("how,to,do,in, ,java");
    tokensList.forEach(System.out::println);

    //Apache Commons
    String[] tokens = StringUtils.split("how to do in java");
    Assertions.assertArrayEquals(new String[]{"how", "to", "do", "in", "java"}, tokens);
  }
}
