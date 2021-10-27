package com.howtodoinjava.core.basic;

import java.util.regex.Pattern;

public class RegexExamples {
    public static void main(String[] args)
    {
        System.out.println(Pattern.compile(".*").matcher("abcd").matches());            //true
        System.out.println(Pattern.compile("[a-zA-Z]*").matcher("abcd").matches());       //true
        System.out.println(Pattern.compile("[0-9]*").matcher("01234").matches());       //true
        System.out.println(Pattern.compile("[a-zA-Z0-9]*").matcher("a1b2c3").matches());       //true
    }
}
