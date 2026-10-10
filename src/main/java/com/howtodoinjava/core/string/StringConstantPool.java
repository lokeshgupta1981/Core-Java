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

/**
 * Examples for the tutorial "Java String Constant Pool and intern() Explained".
 * https://howtodoinjava.com/java/string/string-constant-pool/
 */
public class StringConstantPool {

    public static void main(String[] args) throws Exception {
        {
            String literal = "apple";                       // from the string pool
            show("literal", literal);
            String sameLiteral = "apple";                   // the same pooled object
            show("sameLiteral", sameLiteral);
            String object = new String("apple");            // a new object on the heap
            show("object", object);
            String interned = object.intern();              // the pooled "apple"
            show("interned", interned);

            boolean literals = literal == sameLiteral;      // true
            show("literals", literals);
            boolean newObject = literal == object;          // false
            show("newObject", newObject);
            boolean afterIntern = literal == interned;      // true
            show("afterIntern", afterIntern);
            boolean sameText = literal.equals(object);      // true
            show("sameText", sameText);
        }
        {
            String a = "apple";
            String joined = "app" + "le";                   // constant expression
            show("joined", joined);
            boolean sameJoined = a == joined;               // true
            show("sameJoined", sameJoined);

            final String prefix = "app";
            String constant = prefix + "le";                // prefix is a constant variable
            show("constant", constant);
            boolean sameConstant = a == constant;           // true
            show("sameConstant", sameConstant);

            String part = "app";
            String runtime = part + "le";                   // built at runtime
            show("runtime", runtime);
            boolean sameRuntime = a == runtime;             // false
            show("sameRuntime", sameRuntime);
            boolean equalText = a.equals(runtime);          // true
            show("equalText", equalText);
        }
        {
            String built = new StringBuilder("ban").append("ana42").toString();
            String pooled = built.intern();
            boolean added = built == pooled;                // true (built itself is now pooled)
            show("added", added);

            String copy = new String("cherry");
            String fromPool = copy.intern();
            boolean sameCopy = copy == fromPool;            // false (the pool returns the literal)
            show("sameCopy", sameCopy);
        }
        {
            String[] codes = {"IN", "US", "DE"};
            List<String> countries = new ArrayList<>();
            for (int i = 0; i < 1_000_000; i++) {
                countries.add(new String(codes[i % codes.length]));
            }

            Set<String> before = Collections.newSetFromMap(new IdentityHashMap<>());
            before.addAll(countries);
            int beforeCount = before.size();                // 1000000
            show("beforeCount", beforeCount);

            countries.replaceAll(String::intern);
            Set<String> after = Collections.newSetFromMap(new IdentityHashMap<>());
            after.addAll(countries);
            int afterCount = after.size();                  // 3
            show("afterCount", afterCount);
        }
        {
            Map<String, String> canonical = new ConcurrentHashMap<>();
            String first = canonical.computeIfAbsent(new String("IN"), k -> k);
            String second = canonical.computeIfAbsent(new String("IN"), k -> k);
            boolean same = first == second;                 // true
            show("same", same);
            int size = canonical.size();                    // 1
            show("size", size);
        }
        {
            char[] password = {'s', 'e', 'c', 'r', 'e', 't'};
            // check the password here
            Arrays.fill(password, '\0');
            boolean wiped = password[0] == '\0';            // true
            show("wiped", wiped);
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
