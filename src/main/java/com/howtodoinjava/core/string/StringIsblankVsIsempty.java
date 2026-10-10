package com.howtodoinjava.core.string;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Examples for the tutorial "Java String isBlank(): Check Blank or Empty String".
 * https://howtodoinjava.com/java11/check-blank-string/
 */
public class StringIsblankVsIsempty {
    static boolean isNullOrBlank(String text) {
        return text == null || text.isBlank();
    }
    static record SignUp(String username, String email) {
        SignUp {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("username is required");
            }
            username = username.strip();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            boolean emptyText = "".isBlank();                   // true
            show("emptyText", emptyText);
            boolean spaces = "   ".isBlank();                   // true
            show("spaces", spaces);
            boolean mixed = " \t\n ".isBlank();                 // true
            show("mixed", mixed);
            boolean word = " hi ".isBlank();                    // false
            show("word", word);
            boolean notEmpty = "   ".isEmpty();                 // false
            show("notEmpty", notEmpty);
            String name = null;
            boolean missing = name == null || name.isBlank();   // true
            show("missing", missing);
        }
        {
            boolean tab = "\u0009".isBlank();                 // true
            show("tab", tab);
            boolean emSpace = "\u2003".isBlank();             // true
            show("emSpace", emSpace);
            boolean nbsp = "\u00A0".isBlank();                // false
            show("nbsp", nbsp);
            boolean zeroWidth = "\u200B".isBlank();           // false
            show("zeroWidth", zeroWidth);
            boolean bell = "\u0007".isBlank();                // false
            show("bell", bell);
        }
        {
            String bellOnly = "\u0007";
            boolean trimSaysBlank = bellOnly.trim().isEmpty();        // true
            show("trimSaysBlank", trimSaysBlank);
            boolean blankSaysBlank = bellOnly.isBlank();              // false
            show("blankSaysBlank", blankSaysBlank);
            String emOnly = "\u2003";
            boolean trimEm = emOnly.trim().isEmpty();                 // false
            show("trimEm", trimEm);
            boolean stripEm = emOnly.strip().isEmpty();               // true
            show("stripEm", stripEm);
        }
        {
            String nothing = null;
            try { boolean crash = nothing.isBlank(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            boolean helper = isNullOrBlank(nothing);                              // true
            show("helper", helper);
            boolean viaObjects = Objects.requireNonNullElse(nothing, "").isBlank();    // true
            show("viaObjects", viaObjects);
        }
        {
            String user = new SignUp("  lokesh ", "lokesh@mail.com").username();     // "lokesh"
            show("user", user);
            try { SignUp rejected = new SignUp("   ", "amit@mail.com"); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            String csv = "id,fruit\n\n1,apple\n   \n2,banana\n";
            List<String> rows = csv.lines().filter(Predicate.not(String::isBlank)).toList();   // [id,fruit, 1,apple, 2,banana]
            show("rows", rows);
        }
        {
            String pasted = "\u00A0\u200B ";
            boolean looksBlank = pasted.isBlank();                                           // false
            show("looksBlank", looksBlank);
            String cleaned = pasted.replaceAll("(?U)\\s", "").replace("\u200B", "");         // ""
            show("cleaned", cleaned);
            boolean nowBlank = cleaned.isBlank();                                            // true
            show("nowBlank", nowBlank);
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
