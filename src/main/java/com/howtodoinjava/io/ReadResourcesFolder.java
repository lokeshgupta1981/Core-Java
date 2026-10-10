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
 * Examples for the tutorial "Read a File from the Resources Folder in Java (IDE and JAR)".
 * https://howtodoinjava.com/java/io/read-file-from-resources-folder/
 */
public class ReadResourcesFolder {
    static class RecipeReader {

        static String read(String name) throws IOException {
            ClassLoader loader = RecipeReader.class.getClassLoader();
            try (InputStream in = loader.getResourceAsStream(name)) {
                if (in == null) {
                    throw new FileNotFoundException("Resource not found: " + name);
                }
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }
    static List<String> listResources(String folder) throws IOException, URISyntaxException {
        URL url = Objects.requireNonNull(RecipeReader.class.getResource("/" + folder), folder);
        URI uri = url.toURI();
        if ("jar".equals(uri.getScheme())) {
            try (FileSystem jarFs = FileSystems.newFileSystem(uri, Map.of());
            Stream<Path> files = Files.list(jarFs.getPath(folder))) {
                return files.map(p -> p.getFileName().toString()).sorted().toList();
            }
        }
        try (Stream<Path> files = Files.list(Path.of(uri))) {
            return files.map(p -> p.getFileName().toString()).sorted().toList();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String recipe;
            try (InputStream in = RecipeReader.class.getResourceAsStream("/recipes/pancakes.txt")) {
                if (in == null) {
                    throw new FileNotFoundException("recipes/pancakes.txt is not on the classpath");
                }
                recipe = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            long steps = recipe.lines().count();                    // 2
            show("steps", steps);
            String firstStep = recipe.lines().findFirst().get();    // "Mix flour, milk and eggs."
            show("firstStep", firstStep);
        }
        {
            String pasta = RecipeReader.read("recipes/pasta.txt").strip();   // "Boil pasta for 9 minutes."
            show("pasta", pasta);
            try { String waffles = RecipeReader.read("recipes/waffles.txt"); show("waffles", waffles); } catch (Throwable _t) { System.out.println("waffles -> " + _t); }
        }
        {
            ClassLoader loader = RecipeReader.class.getClassLoader();
            boolean viaLoader = loader.getResource("recipes/pancakes.txt") != null;           // true
            show("viaLoader", viaLoader);
            boolean loaderSlash = loader.getResource("/recipes/pancakes.txt") != null;        // false
            show("loaderSlash", loaderSlash);
            boolean viaClass = RecipeReader.class.getResource("/recipes/pancakes.txt") != null;   // true
            show("viaClass", viaClass);
        }
        {
            List<String> steps = new ArrayList<>();
            try (InputStream in = Objects.requireNonNull(RecipeReader.class.getResourceAsStream("/recipes/pancakes.txt"), "pancakes.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                reader.lines().forEach(steps::add);
            }
            String lastStep = steps.getLast();      // "Fry for 2 minutes per side."
            show("lastStep", lastStep);
        }
        {
            Properties settings = new Properties();
            try (InputStream in = Objects.requireNonNull(RecipeReader.class.getResourceAsStream("/config/app.properties"), "app.properties");
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                settings.load(reader);
            }
            String appName = settings.getProperty("app.name");                     // "recipe-box"
            show("appName", appName);
            int pageSize = Integer.parseInt(settings.getProperty("app.pageSize"));   // 20
            show("pageSize", pageSize);
        }
        {
            URL inJar = RecipeReader.class.getResource("/menu/today.txt");   // jar:file:/home/lokesh/.m2/.../menu-data.jar!/menu/today.txt
            show("inJar", inJar);
            try { File asFile = new File(inJar.toURI()); show("asFile", asFile); } catch (Throwable _t) { System.out.println("asFile -> " + _t); }
            try { Path asPath = Path.of(inJar.toURI()); show("asPath", asPath); } catch (Throwable _t) { System.out.println("asPath -> " + _t); }
            boolean exists = new File(inJar.getFile()).exists();             // false
            show("exists", exists);
        }
        {
            String today;
            try (InputStream in = RecipeReader.class.getResourceAsStream("/menu/today.txt")) {
                today = new String(in.readAllBytes(), StandardCharsets.UTF_8).strip();
            }
            String special = today;                 // "Soup of the day: tomato"
            show("special", special);
        }
        {
            List<String> recipes = listResources("recipes");   // [pancakes.txt, pasta.txt]
            show("recipes", recipes);
            List<String> menu = listResources("menu");         // [today.txt]
            show("menu", menu);
        }
        {
            Path copy = Files.createTempFile("pancakes", ".txt");
            try (InputStream in = RecipeReader.class.getResourceAsStream("/recipes/pancakes.txt")) {
                Files.copy(in, copy, StandardCopyOption.REPLACE_EXISTING);
            }
            long size = Files.size(copy);             // 54
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
