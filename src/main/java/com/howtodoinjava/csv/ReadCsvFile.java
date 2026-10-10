package com.howtodoinjava.csv;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Read a CSV File in Java: split(), Commons CSV, OpenCSV, Jackson".
 * https://howtodoinjava.com/java/io/parse-csv-files-in-java/
 */
public class ReadCsvFile {
    static public record Product(String name, int qty, double price) {}
    static record Transaction(LocalDate date, String description, BigDecimal amount) {}
    public static void main(String[] args) throws Exception {
        {
            Path rates = Files.writeString(Files.createTempFile("rates", ".csv"), "code,rate\nUSD,1.0\nEUR,0.92\nGBP,\n");
            List<String[]> rows = new ArrayList<>();
            try (Stream<String> lines = Files.lines(rates)) {
                lines.skip(1).forEach(line -> rows.add(line.split(",", -1)));
            }
            String[] last = rows.get(2);                         // [GBP, ]
            show("last", last);
            int columns = last.length;                           // 2
            show("columns", columns);
        }
        {
            String[] quoted = "\"Apples, Red\",5,1.20".split(",");    // 4 fields instead of 3, the quoted name is cut in two
            show("quoted", quoted);
            String[] trailing = "Pears,,".split(",");                 // [Pears], the two empty fields are gone
            show("trailing", trailing);
            String[] kept = "Pears,,".split(",", -1);                 // [Pears, , ]
            show("kept", kept);
            String[] escaped = "\"6\"\" ruler\",2".split(",");      // ["6"" ruler", 2], quotes not removed
            show("escaped", escaped);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
