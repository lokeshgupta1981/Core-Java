package com.howtodoinjava.core.string;

import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;

public class StringJoinerExamples {

  public static void main(String[] args) {
    String DELIMITER = ",";
    String PREFIX = "[";
    String SUFFIX = "]";

    //Constructors
    StringJoiner stringJoinerWithDelimiter = new StringJoiner(DELIMITER);
    StringJoiner stringJoinerWithPrefixSuffix = new StringJoiner(DELIMITER, PREFIX, SUFFIX);

    Assertions.assertThrows(NullPointerException.class, () -> {
      new StringJoiner(null);
    });

    //Default Values
    Assertions.assertEquals("", stringJoinerWithDelimiter.toString());
    Assertions.assertEquals("[]", stringJoinerWithPrefixSuffix.toString());

    stringJoinerWithDelimiter.setEmptyValue("BLANK");
    Assertions.assertEquals("BLANK", stringJoinerWithDelimiter.toString());

    //Add String
    stringJoinerWithDelimiter.add("alex");
    stringJoinerWithDelimiter.add("brian");
    stringJoinerWithDelimiter.add("charles");

    Assertions.assertEquals("alex,brian,charles", stringJoinerWithDelimiter.toString());

    stringJoinerWithPrefixSuffix.add("alex");
    stringJoinerWithPrefixSuffix.add("brian");
    stringJoinerWithPrefixSuffix.add("charles");

    Assertions.assertEquals("[alex,brian,charles]", stringJoinerWithPrefixSuffix.toString());

    //Merge Joiners
    StringJoiner joiner1 = new StringJoiner(",", "[", "]");
    StringJoiner joiner2 = new StringJoiner("-", "{", "}");

    joiner1.add("a");
    joiner1.add("b");

    joiner2.add("1");
    joiner2.add("2");

    StringJoiner finalJoiner = joiner1.merge(joiner2);

    Assertions.assertEquals("[a,b,1-2]", finalJoiner.toString());

    //Streams
    String result = Stream.of("a", "b", "c").collect(Collectors.joining(",", "[", "]"));
    Assertions.assertEquals("[a,b,c]", result);

    //StringBuilder
    StringBuilder builder = new StringBuilder();
    result = builder.append("a").append(",").append("b").append(",").append("c").toString();

    stringJoinerWithDelimiter.add("a").add("b").add("c");


  }
}
