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
import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * Examples for the tutorial "Java Annotations Tutorial: Built-in, Meta and Custom Annotations".
 * https://howtodoinjava.com/java/complete-java-annotations-tutorial/
 */
public class AnnotationRetentionLifecycle {
    @SuppressWarnings({"unchecked", "rawtypes"})       // array value in braces
    static class LegacyCatalog {
        @Deprecated(since = "2.0", forRemoval = true)  // several elements
        @SuppressWarnings("removal")                   // single element named value
        List load() {
            return new ArrayList();
        }

        @Override                                      // marker, no elements
        public String toString() {
            return "catalog";
        }
    }
    @Target(ElementType.TYPE_USE)
    @interface NonEmpty {}
    static class PriceCalculator {
        /** @deprecated use {@link #total(double, double)} with an explicit tax rate */
        @Deprecated(since = "2.0", forRemoval = true)
        double total(double price) {
            return price * 1.2;
        }

        double total(double price, double taxRate) {
            return price * (1 + taxRate);
        }
    }
    @SuppressWarnings("unchecked") // the legacy API always returns a List<String>
    static List<String> legacyNames(Object raw) {
        return (List<String>) raw;
    }
    @SafeVarargs
    static <T> List<T> listOf(T... items) {
        return List.of(items);
    }
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Cached {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Monitored {}
    @Cached
    @Monitored
    static class CourseService {}

    static class PremiumCourseService extends CourseService {}
    @Repeatable(Schedules.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Schedule {
        String day();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Schedules {
        Schedule[] value();
    }
    static class ReportJob {
        @Schedule(day = "MON")
        @Schedule(day = "FRI")
        void sendReport() {}
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.RECORD_COMPONENT)
    @interface Required {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.RECORD_COMPONENT)
    @interface MaxLength {
        int value();
    }
    static record SignupForm(@Required String name, @MaxLength(5) String coupon) {}
    static List<String> validate(Record form) throws ReflectiveOperationException {
        List<String> errors = new ArrayList<>();
        for (RecordComponent component : form.getClass().getRecordComponents()) {
            Object value = component.getAccessor().invoke(form);
            if (component.isAnnotationPresent(Required.class)
                    && (value == null || value.toString().isBlank())) {
                errors.add(component.getName() + " is required");
            }
            MaxLength max = component.getAnnotation(MaxLength.class);
            if (max != null && value != null && value.toString().length() > max.value()) {
                errors.add(component.getName() + " is longer than " + max.value());
            }
        }
        return errors;
    }
    public static void main(String[] args) throws Exception {
        {
            Method runFinalization = Runtime.class.getMethod("runFinalization");
            Deprecated info = runFinalization.getAnnotation(Deprecated.class);
            boolean forRemoval = info.forRemoval();                     // true
            show("forRemoval", forRemoval);
            String since = info.since();                                // "18"
            show("since", since);
            boolean overrideKept = Thread.class.getMethod("toString").isAnnotationPresent(Override.class); // false
            show("overrideKept", overrideKept);
        }
        {
            List<@NonEmpty String> tags = List.of("java", "spring");   // [java, spring]
            Object raw = "java";
            String tag = (@NonEmpty String) raw;                       // "java"
            show("tag", tag);
        }
        {
            List<String> names = legacyNames(List.of("Lokesh", "Alex"));   // [Lokesh, Alex]
            show("names", names);
        }
        {
            List<String> levels = listOf("beginner", "advanced");    // [beginner, advanced]
            show("levels", levels);
        }
        {
            boolean cached = PremiumCourseService.class.isAnnotationPresent(Cached.class);       // true
            show("cached", cached);
            boolean monitored = PremiumCourseService.class.isAnnotationPresent(Monitored.class); // false
            show("monitored", monitored);
            int ownAnnotations = PremiumCourseService.class.getDeclaredAnnotations().length;      // 0
            show("ownAnnotations", ownAnnotations);
        }
        {
            Method send = ReportJob.class.getDeclaredMethod("sendReport");
            int schedules = send.getAnnotationsByType(Schedule.class).length;   // 2
            show("schedules", schedules);
            Schedule direct = send.getAnnotation(Schedule.class);               // null
            show("direct", direct);
            boolean wrapped = send.isAnnotationPresent(Schedules.class);        // true
            show("wrapped", wrapped);
        }
        {
            List<String> valid = validate(new SignupForm("Lokesh", "JAVA5"));     // []
            show("valid", valid);
            List<String> invalid = validate(new SignupForm(" ", "SPRING25"));    // [name is required, coupon is longer than 5]
            show("invalid", invalid);
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
