package com.howtodoinjava.core.keywords;

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
 * Examples for the tutorial "Java yield Keyword".
 * https://howtodoinjava.com/java/keywords/yield-keyword-in-java/
 */
public class YieldKeyword {
    static String shippingLabel(int weightKg) {
        return switch (weightKg / 10) {
            case 0 -> "small";
            case 1, 2 -> {
                String size = "medium";
                yield weightKg > 25 ? size + ", heavy" : size;
            }
            default -> {
                if (weightKg > 100) {
                    throw new IllegalArgumentException("Too heavy: " + weightKg);
                }
                yield "large";
            }
        };
    }
    static int daysIn(Month month, boolean leapYear) {
        return switch (month) {
            case FEBRUARY:
            yield leapYear ? 29 : 28;
            case APRIL, JUNE, SEPTEMBER, NOVEMBER:
            yield 30;
            default:
            yield 31;
        };
    }
    static sealed interface Shape permits Circle, Square {}
    static record Circle(double radius) implements Shape {}
    static record Square(double side) implements Shape {}
    static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> {
                double r = c.radius();
                yield Math.PI * r * r;
            }
            case Square s -> s.side() * s.side();
        };
    }
    public static void main(String[] args) throws Exception {
        {
            DayOfWeek day = DayOfWeek.FRIDAY;
            int hours = switch (day) {
                case SATURDAY, SUNDAY -> 0;
                case FRIDAY -> {
                    int base = 8;
                    yield base - 2;                 // short Friday
                }
                default -> 8;
            };
            int result = hours;                     // 6
            show("result", result);
        }
        {
            String parcel = shippingLabel(4);       // "small"
            show("parcel", parcel);
            String box = shippingLabel(27);         // "medium, heavy"
            show("box", box);
            String crate = shippingLabel(60);       // "large"
            show("crate", crate);
            try { String piano = shippingLabel(300); show("piano", piano); } catch (Throwable _t) { System.out.println("piano -> " + _t); }
        }
        {
            int feb = daysIn(Month.FEBRUARY, true);  // 29
            show("feb", feb);
            int june = daysIn(Month.JUNE, false);    // 30
            show("june", june);
            int may = daysIn(Month.MAY, false);      // 31
            show("may", may);
        }
        {
            List<Integer> readings = List.of(3, 7, -1, 12);
            String status = switch (readings.size()) {
                case 0 -> "empty";
                default -> {
                    int firstNegative = -1;
                    for (int i = 0; i < readings.size(); i++) {
                        if (readings.get(i) < 0) {
                            firstNegative = i;
                            break;                  // leaves the for loop only
                        }
                    }
                    yield firstNegative < 0 ? "ok" : "bad reading at " + firstNegative;
                }
            };
            String report = status;                 // "bad reading at 2"
            show("report", report);
        }
        {
            double square = area(new Square(3));     // 9.0
            show("square", square);
            double circle = area(new Circle(1));     // 3.141592653589793
            show("circle", circle);
        }
        {
            int yield = 5;                          // still a legal variable name
            show("yield", yield);
            int doubled = yield * 2;                // 10
            show("doubled", doubled);
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
