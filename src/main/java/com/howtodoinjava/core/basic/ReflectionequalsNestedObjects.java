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
import java.math.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Examples for the tutorial "Compare Java Objects Without Getters: reflectionEquals()".
 * https://howtodoinjava.com/java/basics/compare-objects-without-getter/
 */
public class ReflectionequalsNestedObjects {
    static class Shipment {
        private Long id;
        private String receiver;
        private String city;

        Shipment(Long id, String receiver, String city) {
            this.id = id;
            this.receiver = receiver;
            this.city = city;
        }
    }
    static class BaseVo {
        private Long version;

        BaseVo(Long version) {
            this.version = version;
        }
    }
    static class ParcelVo extends BaseVo {
        private String receiver;
        private String city;

        ParcelVo(Long version, String receiver, String city) {
            super(version);
            this.receiver = receiver;
            this.city = city;
        }
    }
    static class Size {
        private int widthCm;
        private int heightCm;

        Size(int widthCm, int heightCm) {
            this.widthCm = widthCm;
            this.heightCm = heightCm;
        }
    }
    static class Box {
        private String label;
        private Size size;

        Box(String label, Size size) {
            this.label = label;
            this.size = size;
        }
    }
    static boolean fieldsEqual(Object a, Object b) throws IllegalAccessException {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || a.getClass() != b.getClass()) {
            return false;
        }
        for (Field field : a.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            if (!Objects.equals(field.get(a), field.get(b))) {
                return false;
            }
        }
        return true;
    }
    static record ShipmentRecord(Long id, String receiver, String city) {}
    public static void main(String[] args) throws Exception {
        {
            Shipment first = new Shipment(1L, "Anna", "Pune");
            Shipment second = new Shipment(1L, "Anna", "Pune");
            Shipment other = new Shipment(2L, "Anna", "Pune");
            boolean byEquals = first.equals(second);                               // false, compares references
            show("byEquals", byEquals);
            boolean byFields = EqualsBuilder.reflectionEquals(first, second);      // true
            show("byFields", byFields);
            boolean differentId = EqualsBuilder.reflectionEquals(first, other);    // false
            show("differentId", differentId);
            boolean ignoringId = EqualsBuilder.reflectionEquals(first, other, "id");   // true
            show("ignoringId", ignoringId);
        }
        {
            Shipment fromWebhook = new Shipment(7L, "Ravi", "Delhi");
            Shipment fromFile = new Shipment(7L, "Ravi", "Delhi");
            boolean duplicate = EqualsBuilder.reflectionEquals(fromWebhook, fromFile);   // true
            show("duplicate", duplicate);
            boolean nullSide = EqualsBuilder.reflectionEquals(fromWebhook, null);        // false
            show("nullSide", nullSide);
        }
        {
            Shipment saved = new Shipment(10L, "Meera", "Goa");
            Shipment incoming = new Shipment(null, "Meera", "Goa");
            boolean allFields = EqualsBuilder.reflectionEquals(saved, incoming);                      // false
            show("allFields", allFields);
            boolean withoutId = EqualsBuilder.reflectionEquals(saved, incoming, "id");                // true
            show("withoutId", withoutId);
            boolean fromList = EqualsBuilder.reflectionEquals(saved, incoming, List.of("id"));        // true
            show("fromList", fromList);
        }
        {
            ParcelVo v1 = new ParcelVo(1L, "Ravi", "Delhi");
            ParcelVo v2 = new ParcelVo(2L, "Ravi", "Delhi");
            boolean withParent = EqualsBuilder.reflectionEquals(v1, v2);           // false
            show("withParent", withParent);
            EqualsBuilder upToChild = new EqualsBuilder().setReflectUpToClass(ParcelVo.class);
            boolean childOnly = upToChild.reflectionAppend(v1, v2).isEquals();     // true
            show("childOnly", childOnly);
        }
        {
            Box boxA = new Box("books", new Size(40, 30));
            Box boxB = new Box("books", new Size(40, 30));
            boolean shallow = EqualsBuilder.reflectionEquals(boxA, boxB);          // false, Size compared by reference
            show("shallow", shallow);
            EqualsBuilder recursive = new EqualsBuilder().setTestRecursive(true);
            boolean deep = recursive.reflectionAppend(boxA, boxB).isEquals();      // true
            show("deep", deep);
        }
        {
            try { boolean jdkObject = EqualsBuilder.reflectionEquals(new StringBuilder("a"), new StringBuilder("a")); show("jdkObject", jdkObject); } catch (Throwable _t) { System.out.println("jdkObject -> " + _t); }
        }
        {
            boolean manual = fieldsEqual(new Shipment(3L, "Ali", "Agra"), new Shipment(3L, "Ali", "Agra"));   // true
            show("manual", manual);
        }
        {
            Shipment expected = new Shipment(null, "Anna", "Pune");
            Shipment actual = new Shipment(42L, "Anna", "Pune");
            assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
            assertThat(new Box("toys", new Size(20, 20))).usingRecursiveComparison().isEqualTo(new Box("toys", new Size(20, 20)));
        }
        {
            boolean recordEquals = new ShipmentRecord(1L, "Anna", "Pune").equals(new ShipmentRecord(1L, "Anna", "Pune"));   // true
            show("recordEquals", recordEquals);
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
