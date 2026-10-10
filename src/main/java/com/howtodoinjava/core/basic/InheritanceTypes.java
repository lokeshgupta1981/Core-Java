package com.howtodoinjava.core.basic;

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

/**
 * Examples for the tutorial "Java Inheritance: extends, super, Types and Examples".
 * https://howtodoinjava.com/java/oops/java-inheritance/
 */
public class InheritanceTypes {
    static class Course {
        private final String title;

        Course(String title) {
            this.title = title;
        }

        String title() {
            return title;
        }

        String describe() {
            return title + " course";
        }
    }

    static class VideoCourse extends Course {
        private final int minutes;

        VideoCourse(String title, int minutes) {
            super(title);
            this.minutes = minutes;
        }

        @Override
        String describe() {
            return super.describe() + ", " + minutes + " min of video";
        }
    }
    static class Lesson {
        int maxPoints = 10;

        int points() {
            return 10;
        }
    }

    static class QuizLesson extends Lesson {
        int maxPoints = 25;

        @Override
        int points() {
            return 25;
        }
    }
    static sealed abstract class Enrollment permits FreeEnrollment, PaidEnrollment {
    }

    static final class FreeEnrollment extends Enrollment {
    }

    static non-sealed class PaidEnrollment extends Enrollment {
    }
    static String access(Enrollment enrollment) {
        return switch (enrollment) {
            case FreeEnrollment f -> "first 3 lessons";
            case PaidEnrollment p -> "all lessons";
        };
    }
    static class CourseCatalog {
        private final List<Course> courses = new ArrayList<>();

        void add(Course course) {
            courses.add(Objects.requireNonNull(course, "course"));
        }

        List<Course> all() {
            return List.copyOf(courses);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            VideoCourse javaBasics = new VideoCourse("Java", 90);
            String title = javaBasics.title();                // "Java"
            show("title", title);
            String about = javaBasics.describe();             // "Java course, 90 min of video"
            show("about", about);
            boolean isCourse = javaBasics instanceof Course;  // true
            show("isCourse", isCourse);
        }
        {
            Lesson lesson = new QuizLesson();
            int fieldRead = lesson.maxPoints;                 // 10
            show("fieldRead", fieldRead);
            int methodCall = lesson.points();                 // 25
            show("methodCall", methodCall);
            int asQuiz = ((QuizLesson) lesson).maxPoints;     // 25
            show("asQuiz", asQuiz);
        }
        {
            String free = access(new FreeEnrollment());       // "first 3 lessons"
            show("free", free);
            String paid = access(new PaidEnrollment());       // "all lessons"
            show("paid", paid);
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
