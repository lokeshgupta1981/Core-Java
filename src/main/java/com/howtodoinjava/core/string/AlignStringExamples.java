package com.howtodoinjava.core.string;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
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

import java.time.temporal.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Examples for the tutorial "Left, Right or Center Align a String in Java (Examples)".
 * https://howtodoinjava.com/java/string/left-right-or-center-align-string/
 */
public class AlignStringExamples {
    static String center(String text, int width) {
        int gap = Math.max(0, width - text.length());
        int before = gap / 2;
        return " ".repeat(before) + text + " ".repeat(gap - before);
    }
    static enum Align { LEFT, CENTER, RIGHT }
    static String pad(String line, int width, Align align) {
        int gap = Math.max(0, width - line.length());
        return switch (align) {
            case LEFT -> line + " ".repeat(gap);
            case RIGHT -> " ".repeat(gap) + line;
            case CENTER -> " ".repeat(gap / 2) + line + " ".repeat(gap - gap / 2);
        };
    }
    static List<String> wrap(String text, int width) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.strip().split("\\s+")) {
            if (!line.isEmpty() && line.length() + 1 + word.length() > width) {
                lines.add(line.toString());
                line.setLength(0);
            }
            if (!line.isEmpty()) {
                line.append(' ');
            }
            line.append(word);
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        return lines;
    }
    static String align(String text, int width, Align align) {
        return wrap(text, width).stream()
                .map(line -> pad(line, width, align))
                .collect(Collectors.joining("\n"));
    }
    public static void main(String[] args) throws Exception {
        {
            String item = "Tea";
            String left = String.format("%-9s", item);      // "Tea      "
            show("left", left);
            String right = String.format("%9s", item);      // "      Tea"
            show("right", right);
            String middle = center(item, 9);                // "   Tea   "
            show("middle", middle);
        }
        {
            int width = 12;
            String leftAligned = String.format("%-" + width + "s|", "Latte");   // "Latte       |"
            show("leftAligned", leftAligned);
            String rightAligned = String.format("%" + width + "s|", "Latte");   // "       Latte|"
            show("rightAligned", rightAligned);
            String sameResult = "%-12s|".formatted("Latte");                     // "Latte       |"
            show("sameResult", sameResult);
        }
        {
            String full = String.format("%5s|", "Cappuccino");      // "Cappuccino|"
            show("full", full);
            String cut = String.format("%-5.5s|", "Cappuccino");     // "Cappu|"
            show("cut", cut);
            try { String noPad = String.format("%05s", "Tea"); show("noPad", noPad); } catch (Throwable _t) { System.out.println("noPad -> " + _t); }
        }
        {
            String even = center("Tea", 9);         // "   Tea   "
            show("even", even);
            String odd = center("Tea", 8);          // "  Tea   "
            show("odd", odd);
            String tooLong = center("Cappuccino", 6); // "Cappuccino"
            show("tooLong", tooLong);
        }
        {
            String leftSide = StringUtils.rightPad("Tea", 9);         // "Tea      "
            show("leftSide", leftSide);
            String rightSide = StringUtils.leftPad("Tea", 9, '.');    // "......Tea"
            show("rightSide", rightSide);
            String centered = StringUtils.center("Tea", 9, '*');      // "***Tea***"
            show("centered", centered);
            String oddCenter = StringUtils.center("Tea", 8);          // "  Tea   "
            show("oddCenter", oddCenter);
            String nothing = StringUtils.center(null, 9);             // null
            show("nothing", nothing);
        }
        {
            String note = "Free delivery on orders above 30 dollars. Returns accepted within 14 days.";
            String leftNote = align(note, 24, Align.LEFT);       // 4 lines, padded on the right
            show("leftNote", leftNote);
            String rightNote = align(note, 24, Align.RIGHT);     // 4 lines, padded on the left
            show("rightNote", rightNote);
            String centerNote = align(note, 24, Align.CENTER);   // 4 lines, padded on both sides
            show("centerNote", centerNote);
            int lineLength = centerNote.lines().findFirst().orElse("").length();   // 24
            show("lineLength", lineLength);
        }
        {
            List<String> receipt = List.of(
            center("CORNER CAFE", 32),
            "-".repeat(32),
            String.format(Locale.ROOT, "%-22s%10.2f", "Cappuccino", 3.5),
            String.format(Locale.ROOT, "%-22s%10.2f", "Blueberry muffin", 2.75),
            String.format(Locale.ROOT, "%-22s%10.2f", "Tea", 1.8),
            "-".repeat(32),
            String.format(Locale.ROOT, "%-22s%10.2f", "TOTAL", 8.05),
            center("Thank you!", 32));
            int lines = receipt.size();                     // 8
            show("lines", lines);
        }
        {
            String dots = "Tea" + ".".repeat(9 - "Tea".length());   // "Tea......"
            show("dots", dots);
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
