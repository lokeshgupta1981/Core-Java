package com.howtodoinjava.io;

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
 * Examples for the tutorial "How to Read a Properties File in Java (and Write It Back)".
 * https://howtodoinjava.com/java/io/read-write-properties-file/
 */
public class PropertiesFileExamples {
    static Properties loadSettings(Path file) throws IOException {
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            props.load(reader);
        }
        return props;
    }
    static final class AppSettings {

        private static final Properties PROPS = load();

        private AppSettings() {
        }

        private static Properties load() {
            Properties props = new Properties();
            try (InputStream in = AppSettings.class.getResourceAsStream("/config/app.properties")) {
                if (in == null) {
                    throw new IllegalStateException("config/app.properties is not on the classpath");
                }
                props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return props;
        }

        static String get(String key, String defaultValue) {
            return PROPS.getProperty(key, defaultValue);
        }

        static int getInt(String key, int defaultValue) {
            String value = PROPS.getProperty(key);
            if (value == null) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(value.strip());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }
    static void saveSettings(Properties props, Path file) throws IOException {
        Path tmp = Files.createTempFile(file.toAbsolutePath().getParent(), "app", ".tmp");
        try (Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
            props.store(writer, "Recipe box settings");
        }
        Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
    public static void main(String[] args) throws Exception {
        {
            Path file = Files.createTempFile("app", ".properties");
            Files.writeString(file, "app.name=recipe-box\napp.pageSize=20\n");

            Properties props = new Properties();
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                props.load(reader);
            }
            String name = props.getProperty("app.name");               // "recipe-box"
            show("name", name);
            String theme = props.getProperty("app.theme", "light");    // "light"
            show("theme", theme);

            props.setProperty("app.theme", "dark");
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                props.store(writer, "Recipe box settings");
            }
            List<String> saved = Files.readAllLines(file).stream().filter(line -> !line.startsWith("#")).toList();   // [app.name=recipe-box, app.pageSize=20, app.theme=dark]
            show("saved", saved);
        }
        {
            String text = """
            # Recipe box settings
            app.name = recipe-box
            app.pageSize: 20
            app.greeting Bon app\\u00e9tit
            app.categories = breakfast, lunch, \\
            dinner
            """;
            Properties props = new Properties();
            props.load(new StringReader(text));
            String pageSize = props.getProperty("app.pageSize");             // "20"
            show("pageSize", pageSize);
            boolean accented = props.getProperty("app.greeting").equals("Bon app\u00e9tit");   // true
            show("accented", accented);
            String categories = props.getProperty("app.categories");         // "breakfast, lunch, dinner"
            show("categories", categories);
            int count = props.size();                                        // 4
            show("count", count);
        }
        {
            Path config = Files.writeString(Files.createTempFile("app", ".properties"), "app.name=recipe-box\n");
            String appName = loadSettings(config).getProperty("app.name");          // "recipe-box"
            show("appName", appName);
            try { Properties none = loadSettings(Path.of("/etc/recipe-box/missing.properties")); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            Properties defaults = new Properties();
            try (InputStream in = Objects.requireNonNull(AppSettings.class.getResourceAsStream("/config/app.properties"), "config/app.properties");
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                defaults.load(reader);
            }
            String bundledName = defaults.getProperty("app.name");     // "recipe-box"
            show("bundledName", bundledName);
            String bundledSize = defaults.getProperty("app.pageSize"); // "20"
            show("bundledSize", bundledSize);
        }
        {
            Properties bundled = new Properties();
            bundled.setProperty("app.pageSize", "20");
            bundled.setProperty("app.theme", "light");

            Properties settings = new Properties(bundled);
            settings.setProperty("app.theme", "dark");

            String theme = settings.getProperty("app.theme");          // "dark"
            show("theme", theme);
            String size = settings.getProperty("app.pageSize");        // "20"
            show("size", size);
            Set<String> keys = new TreeSet<>(settings.stringPropertyNames());   // [app.pageSize, app.theme]
            show("keys", keys);
            int ownEntries = settings.size();                          // 1
            show("ownEntries", ownEntries);
        }
        {
            int pageSize = AppSettings.getInt("app.pageSize", 10);        // 20
            show("pageSize", pageSize);
            int timeout = AppSettings.getInt("app.timeoutSeconds", 30);   // 30
            show("timeout", timeout);
            String appName = AppSettings.get("app.name", "unknown");      // "recipe-box"
            show("appName", appName);
        }
        {
            Path file = Files.writeString(Files.createTempFile("app", ".properties"), "app.pageSize=20\n");
            Properties props = loadSettings(file);
            props.setProperty("app.pageSize", "25");
            props.setProperty("app.theme", "dark");
            saveSettings(props, file);
            String reloaded = loadSettings(file).getProperty("app.pageSize");    // "25"
            show("reloaded", reloaded);
        }
        {
            Path file = Files.writeString(Files.createTempFile("messages", ".properties"), "greeting=Bon app\u00e9tit\n", StandardCharsets.UTF_8);

            Properties bytes = new Properties();
            try (InputStream in = Files.newInputStream(file)) {
                bytes.load(in);
            }
            boolean brokenText = bytes.getProperty("greeting").equals("Bon app\u00e9tit");   // false (read as ISO-8859-1)
            show("brokenText", brokenText);

            Properties chars = new Properties();
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                chars.load(reader);
            }
            boolean correctText = chars.getProperty("greeting").equals("Bon app\u00e9tit");  // true
            show("correctText", correctText);
        }
        {
            Properties props = new Properties();
            props.put("app.pageSize", 20);
            String size = props.getProperty("app.pageSize");     // null
            show("size", size);
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
