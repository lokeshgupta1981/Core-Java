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
 * Examples for the tutorial "Difference Between Encapsulation and Abstraction in Java".
 * https://howtodoinjava.com/java/oops/encapsulation-in-java-and-its-relation-with-abstraction/
 */
public class EncapsulationVsAbstractionJava {
    static interface Thermostat {
        void setTarget(int celsius);

        int target();
    }

    static class RoomThermostat implements Thermostat {
        private int targetCelsius = 20;

        public void setTarget(int celsius) {
            if (celsius < 5 || celsius > 30) {
                throw new IllegalArgumentException("Target out of range: " + celsius);
            }
            targetCelsius = celsius;
        }

        public int target() {
            return targetCelsius;
        }
    }
    static class CloudThermostat implements Thermostat {
        private int targetFahrenheit = 68;

        public void setTarget(int celsius) {
            if (celsius < 5 || celsius > 30) {
                throw new IllegalArgumentException("Target out of range: " + celsius);
            }
            targetFahrenheit = celsius * 9 / 5 + 32;
        }

        public int target() {
            return (targetFahrenheit - 32) * 5 / 9;
        }
    }
    static class OpenThermostat {
        public int targetCelsius = 20;
    }
    static class LeakySchedule {
        private final List<Integer> hours = new ArrayList<>(List.of(6, 18));

        List<Integer> hours() {
            return hours;
        }
    }

    static class HeatingSchedule {
        private final List<Integer> hours = new ArrayList<>(List.of(6, 18));

        List<Integer> hours() {
            return List.copyOf(hours);
        }

        void addHour(int hour) {
            if (hour < 0 || hour > 23) {
                throw new IllegalArgumentException("Hour out of range: " + hour);
            }
            hours.add(hour);
        }
    }
    static record Setpoint(String room, int celsius) {
        Setpoint {
            if (celsius < 5 || celsius > 30) {
                throw new IllegalArgumentException("Target out of range: " + celsius);
            }
        }
    }

    static record WeeklyPlan(List<Setpoint> points) {
        WeeklyPlan {
            points = List.copyOf(points);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Thermostat living = new RoomThermostat();
            living.setTarget(22);
            int current = living.target();                    // 22
            show("current", current);
            try { living.setTarget(45);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Thermostat office = new CloudThermostat();
            office.setTarget(25);
            int officeTarget = office.target();               // 25
            show("officeTarget", officeTarget);
        }
        {
            OpenThermostat hallway = new OpenThermostat();
            hallway.targetCelsius = 300;
            int hallwayTarget = hallway.targetCelsius;        // 300
            show("hallwayTarget", hallwayTarget);
        }
        {
            LeakySchedule leaky = new LeakySchedule();
            leaky.hours().add(99);
            List<Integer> leakedHours = leaky.hours();        // [6, 18, 99]
            show("leakedHours", leakedHours);

            HeatingSchedule schedule = new HeatingSchedule();
            try { schedule.hours().add(99);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            schedule.addHour(22);
            List<Integer> safeHours = schedule.hours();       // [6, 18, 22]
            show("safeHours", safeHours);
        }
        {
            Setpoint bedroom = new Setpoint("bedroom", 19);
            int bedroomTarget = bedroom.celsius();            // 19
            show("bedroomTarget", bedroomTarget);
            try { Setpoint sauna = new Setpoint("sauna", 90); show("sauna", sauna); } catch (Throwable _t) { System.out.println("sauna -> " + _t); }

            List<Setpoint> source = new ArrayList<>(List.of(bedroom));
            WeeklyPlan plan = new WeeklyPlan(source);
            source.clear();
            int planSize = plan.points().size();              // 1
            show("planSize", planSize);
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
