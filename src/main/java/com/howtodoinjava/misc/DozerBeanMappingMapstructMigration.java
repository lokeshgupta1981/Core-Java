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

/**
 * Examples for the tutorial "Dozer Bean Mapping Example and Migration to MapStruct".
 * https://howtodoinjava.com/java/library/dozer-bean-mapping-examples/
 */
public class DozerBeanMappingMapstructMigration {
    public static class Step {
        private String text;
        public Step() { }
        public Step(String text) { this.text = text; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
    public static class Recipe {
        private String name;
        private String minutes;
        private String chef;
        private List<Step> steps = new ArrayList<>();
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getMinutes() { return minutes; }
        public void setMinutes(String minutes) { this.minutes = minutes; }
        public String getChef() { return chef; }
        public void setChef(String chef) { this.chef = chef; }
        public List<Step> getSteps() { return steps; }
        public void setSteps(List<Step> steps) { this.steps = steps; }
    }
    public static class StepDto {
        private String text;
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
    public static class RecipeDto {
        private String name;
        private int minutes;
        private String author;
        private Duration cookTime;
        private List<StepDto> steps;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getMinutes() { return minutes; }
        public void setMinutes(int minutes) { this.minutes = minutes; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public Duration getCookTime() { return cookTime; }
        public void setCookTime(Duration cookTime) { this.cookTime = cookTime; }
        public List<StepDto> getSteps() { return steps; }
        public void setSteps(List<StepDto> steps) { this.steps = steps; }
    }
    public static class Chef {
        @com.github.dozermapper.core.Mapping("author")
        private String fullName;
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }
    static record RecipeView(String name, int minutes, String author) {}
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
