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
import org.apache.commons.lang3.Strings;

/**
 * Examples for the tutorial "Java String startsWith(): Ignore Case, Offset, Multiple Prefixes".
 * https://howtodoinjava.com/java/string/java-string-startswith-example/
 */
public class StartsWithExamples {
    static boolean startsWithIgnoreCase(String text, String prefix) {
        return text.regionMatches(true, 0, prefix, 0, prefix.length());
    }
    static String route(String path, Map<String, String> routes) {
        return routes.keySet().stream()
                .filter(prefix -> path.equals(prefix) || path.startsWith(prefix + "/"))
                .max(Comparator.comparingInt(String::length))
                .map(routes::get)
                .orElse("not-found");
    }
    public static void main(String[] args) throws Exception {
        {
            String path = "/api/v1/users";
            boolean isApi = path.startsWith("/api");                                  // true
            show("isApi", isApi);
            boolean upper = path.startsWith("/API");                                  // false
            show("upper", upper);
            boolean versionAt5 = path.startsWith("v1", 5);                            // true
            show("versionAt5", versionAt5);
            boolean ignoreCase = path.regionMatches(true, 0, "/API", 0, 4);           // true
            show("ignoreCase", ignoreCase);
            boolean anyPrefix = Stream.of("/static", "/api").anyMatch(path::startsWith);   // true
            show("anyPrefix", anyPrefix);
            boolean emptyPrefix = path.startsWith("");                                // true
            show("emptyPrefix", emptyPrefix);
        }
        {
            boolean longer = "abc".startsWith("abcd");          // false
            show("longer", longer);
            boolean atEnd = "abc".startsWith("", 3);             // true
            show("atEnd", atEnd);
            boolean negative = "abc".startsWith("a", -1);        // false
            show("negative", negative);
            boolean beyond = "abc".startsWith("", 4);            // false
            show("beyond", beyond);
            try { boolean nullPrefix = "abc".startsWith(null); show("nullPrefix", nullPrefix); } catch (Throwable _t) { System.out.println("nullPrefix -> " + _t); }
        }
        {
            String line = "2026-06-01 ERROR db timeout";
            boolean isError = line.startsWith("ERROR", 11);      // true
            show("isError", isError);
            boolean isWarn = line.startsWith("WARN", 11);        // false
            show("isWarn", isWarn);
        }
        {
            String path = "/api/v2/orders";
            boolean caret = path.startsWith("^/api");                                          // false
            show("caret", caret);
            boolean anyVersion = Pattern.compile("/api/v\\d+/").matcher(path).lookingAt();     // true
            show("anyVersion", anyVersion);
            boolean wholeMatch = path.matches("/api/v\\d+/.*");                                // true
            show("wholeMatch", wholeMatch);
        }
        {
            boolean pdf = startsWithIgnoreCase("INVOICE-2026.pdf", "invoice");                // true
            show("pdf", pdf);
            boolean tooLong = startsWithIgnoreCase("inv", "invoice");                            // false
            show("tooLong", tooLong);
        }
        {
            boolean turkish = "TITLE".toLowerCase(Locale.forLanguageTag("tr")).startsWith("ti");   // false
            show("turkish", turkish);
            boolean root = "TITLE".toLowerCase(Locale.ROOT).startsWith("ti");                         // true
            show("root", root);
        }
        {
            boolean viaCommons = Strings.CI.startsWith("INVOICE-2026.pdf", "invoice");   // true
            show("viaCommons", viaCommons);
            boolean nullText = Strings.CI.startsWith(null, "invoice");                   // false
            show("nullText", nullText);
        }
        {
            String path = "/api/v1/users";
            List<String> prefixes = List.of("/static", "/api", "/admin");
            boolean allowed = prefixes.stream().anyMatch(path::startsWith);                       // true
            show("allowed", allowed);
            Optional<String> matched = prefixes.stream().filter(path::startsWith).findFirst();    // Optional[/api]
            show("matched", matched);
        }
        {
            String path = "/api/v1/users";
            List<String> nested = List.of("/api", "/api/v1");
            Optional<String> first = nested.stream().filter(path::startsWith).findFirst();                             // Optional[/api]
            show("first", first);
            Optional<String> longest = nested.stream().filter(path::startsWith).max(Comparator.comparingInt(String::length));   // Optional[/api/v1]
            show("longest", longest);
        }
        {
            String phone = "+44 20 7946 0000";
            List<String> codes = List.of("+1", "+44");
            try { Pattern broken = Pattern.compile(String.join("|", codes)); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            Pattern quoted = Pattern.compile(codes.stream().map(Pattern::quote).collect(Collectors.joining("|")));
            boolean known = quoted.matcher(phone).lookingAt();                                             // true
            show("known", known);
        }
        {
            boolean anyCommons = Strings.CS.startsWithAny("/api/v1/users", "/static", "/api");   // true
            show("anyCommons", anyCommons);
            boolean anyIgnoreCase = Strings.CI.startsWithAny("/API/v1/users", "/static", "/api");  // true
            show("anyIgnoreCase", anyIgnoreCase);
        }
        {
            String empty = "";
            boolean viaIndex = "/api/v1".indexOf("/api") == 0;                 // true
            show("viaIndex", viaIndex);
            try { boolean firstChar = empty.charAt(0) == '/'; show("firstChar", firstChar); } catch (Throwable _t) { System.out.println("firstChar -> " + _t); }
            boolean safeFirstChar = !empty.isEmpty() && empty.charAt(0) == '/';   // false
            show("safeFirstChar", safeFirstChar);
            boolean safePrefix = empty.startsWith("/");                         // false
            show("safePrefix", safePrefix);
        }
        {
            String header = null;
            try { boolean crash = header.startsWith("Bearer "); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            boolean safe = header != null && header.startsWith("Bearer ");         // false
            show("safe", safe);
            String present = "Bearer abc123";
            String token = present.startsWith("Bearer ") ? present.substring("Bearer ".length()) : "";   // "abc123"
            show("token", token);
        }
        {
            Map<String, String> routes = Map.of("/api", "api-service", "/api/v2", "api-v2-service", "/static", "cdn");
            String v2 = route("/api/v2/orders", routes);         // "api-v2-service"
            show("v2", v2);
            String v1 = route("/api/v1/users", routes);          // "api-service"
            show("v1", v1);
            String exact = route("/static", routes);             // "cdn"
            show("exact", exact);
            String lookalike = route("/apix", routes);           // "not-found"
            show("lookalike", lookalike);
        }
        {
            String code = "7F-ALPHA";
            boolean digitFirst = !code.isEmpty() && Character.isDigit(code.charAt(0));        // true
            show("digitFirst", digitFirst);
            boolean letterFirst = !code.isEmpty() && Character.isLetter(code.charAt(0));      // false
            show("letterFirst", letterFirst);
        }
        {
            boolean slash = "/home".startsWith("/");                  // true
            show("slash", slash);
            boolean viaChar = !"/home".isEmpty() && "/home".charAt(0) == '/';   // true
            show("viaChar", viaChar);
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
