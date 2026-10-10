package com.howtodoinjava.core.serialization.xmlencoder;

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
import java.beans.*;
import java.lang.reflect.RecordComponent;

/**
 * Examples for the tutorial "XMLEncoder and XMLDecoder: Serialize Java Objects to XML".
 * https://howtodoinjava.com/java/serialization/xmlencoder-and-xmldecoder-example/
 */
public class XmlEncoderDecoder {
    static public class EditorSettings {

        private String theme = "light";
        private int fontSize = 14;
        private boolean autoSave;
        private List<String> recentFiles = new ArrayList<>();
        private WindowSize windowSize;

        public EditorSettings() {
        }

        public String getTheme() { return theme; }
        public void setTheme(String theme) { this.theme = theme; }
        public int getFontSize() { return fontSize; }
        public void setFontSize(int fontSize) { this.fontSize = fontSize; }
        public boolean isAutoSave() { return autoSave; }
        public void setAutoSave(boolean autoSave) { this.autoSave = autoSave; }
        public List<String> getRecentFiles() { return recentFiles; }
        public void setRecentFiles(List<String> recentFiles) { this.recentFiles = recentFiles; }
        public WindowSize getWindowSize() { return windowSize; }
        public void setWindowSize(WindowSize windowSize) { this.windowSize = windowSize; }

        @Override
        public String toString() {
            return "EditorSettings[theme=" + theme + ", fontSize=" + fontSize + ", autoSave=" + autoSave
            + ", recentFiles=" + recentFiles + ", windowSize=" + windowSize + "]";
        }
    }
    static public record WindowSize(int width, int height) { }
    public static void save(Object settings, Path file) throws IOException {
        List<Exception> errors = new ArrayList<>();
        ByteArrayOutputStream xml = new ByteArrayOutputStream();
        try (XMLEncoder encoder = new XMLEncoder(xml)) {
            encoder.setExceptionListener(errors::add);
            encoder.setPersistenceDelegate(WindowSize.class, new RecordPersistenceDelegate());
            encoder.writeObject(settings);
        }
        if (!errors.isEmpty()) {
            IOException failure = new IOException("XMLEncoder could not write " + settings.getClass().getName());
            errors.forEach(failure::addSuppressed);
            throw failure;
        }
        Files.write(file, xml.toByteArray());
    }
    public static <T> T load(Path file, Class<T> type) throws IOException {
        List<Exception> errors = new ArrayList<>();
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file));
        XMLDecoder decoder = new XMLDecoder(in, null, errors::add)) {
            Object value = decoder.readObject();
            if (!errors.isEmpty()) {
                IOException failure = new IOException("XMLDecoder could not read " + file);
                errors.forEach(failure::addSuppressed);
                throw failure;
            }
            return type.cast(value);
        }
    }
    static public class RecordPersistenceDelegate extends PersistenceDelegate {

        @Override
        protected Expression instantiate(Object oldInstance, Encoder out) {
            RecordComponent[] components = oldInstance.getClass().getRecordComponents();
            Object[] args = new Object[components.length];
            try {
                for (int i = 0; i < components.length; i++) {
                    args[i] = components[i].getAccessor().invoke(oldInstance);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Cannot read record " + oldInstance.getClass().getName(), e);
            }
            return new Expression(oldInstance, oldInstance.getClass(), "new", args);
        }

        @Override
        protected boolean mutatesTo(Object oldInstance, Object newInstance) {
            return oldInstance.equals(newInstance);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            EditorSettings settings = new EditorSettings();
            settings.setTheme("dark");
            settings.setFontSize(16);
            Path file = Path.of("settings.xml");

            try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(Files.newOutputStream(file)))) {
                encoder.writeObject(settings);                       // settings.xml with theme and fontSize
            }

            try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(Files.newInputStream(file)))) {
                EditorSettings loaded = (EditorSettings) decoder.readObject();
                String theme = loaded.getTheme();                    // "dark"
                int fontSize = loaded.getFontSize();                 // 16
                boolean autoSave = loaded.isAutoSave();              // false (default, not in the file)
            }
        }
        {
            EditorSettings settings = new EditorSettings();
            settings.setTheme("dark");
            settings.setFontSize(16);
            settings.setRecentFiles(new ArrayList<>(List.of("todo.md", "notes.txt")));
            settings.setWindowSize(new WindowSize(1280, 800));

            save(settings, Path.of("settings.xml"));
            EditorSettings loaded = load(Path.of("settings.xml"), EditorSettings.class);
            String text = loaded.toString();   // EditorSettings[theme=dark, fontSize=16, autoSave=false, recentFiles=[todo.md, notes.txt], windowSize=WindowSize[width=1280, height=800]]
            show("text", text);
        }
        {
            EditorSettings settings = new EditorSettings();
            settings.setRecentFiles(List.of("todo.md"));
            try { save(settings, Path.of("broken.xml"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
            //   Suppressed: InstantiationException: java.util.ImmutableCollections$List12
            boolean written = Files.exists(Path.of("broken.xml"));   // false
            show("written", written);
        }
        {
            EditorSettings settings = new EditorSettings();
            settings.setTheme("dark");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (XMLEncoder encoder = new XMLEncoder(bytes)) {
                encoder.writeObject(settings);
            }
            String xml = bytes.toString(StandardCharsets.UTF_8);
            boolean hasTheme = xml.contains("<string>dark</string>");   // true
            show("hasTheme", hasTheme);
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
