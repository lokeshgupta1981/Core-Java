package com.howtodoinjava.misc;

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
import java.util.Properties;

/**
 * Examples for the tutorial "Create and Extract a JAR File on Linux, macOS and Windows".
 * https://howtodoinjava.com/java/create-extract-jar-files/
 */
public class CreateExtractJarFileLayout {
    static public class RecipeApp {
        public static void main(String[] args) throws IOException {
            Properties props = new Properties();
            try (InputStream in = RecipeApp.class.getResourceAsStream("/app.properties")) {
                if (in == null) {
                    throw new IllegalStateException("app.properties is missing from the JAR");
                }
                props.load(in);
            }
            System.out.println("Recipes app " + props.getProperty("version"));
        }
    }
    public static void main(String[] args) throws Exception {

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
