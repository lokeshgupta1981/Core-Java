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
 * Examples for the tutorial "Multiple Inheritance in Java: Interfaces and the Diamond Problem".
 * https://howtodoinjava.com/java/oops/multiple-inheritance-in-java/
 */
public class MultipleInheritanceDiamondRules {
    static interface Switchable {
        default String powerOn() {
            return "on";
        }
    }

    static interface Dimmable {
        default int level() {
            return 100;
        }
    }

    static class SmartBulb implements Switchable, Dimmable {
    }
    static interface Schedulable {
        String nextRun();

        default String summary() {
            return "next run at " + nextRun();
        }
    }

    static class GardenSprinkler implements Schedulable {
        public String nextRun() {
            return "06:00";
        }
    }
    static interface Device {
        default String describe() {
            return "device";
        }
    }

    static interface Light extends Device {
        @Override
        default String describe() {
            return "light";
        }
    }

    static interface Speaker extends Device {
        @Override
        default String describe() {
            return "speaker";
        }
    }
    static class PoweredDevice {
        public String describe() {
            return "powered device";
        }
    }

    static class WallSocket extends PoweredDevice implements Light {
    }
    static class CeilingLight implements Device, Light {
    }
    static class SmartLamp implements Light, Speaker {
        @Override
        public String describe() {
            return Light.super.describe() + " + " + Speaker.super.describe();
        }
    }
    static interface Firmware {
        static String version() {
            return "2.1";
        }
    }
    static class EnergyMeter {
        int wattHours(int hours) {
            return hours * 60;
        }
    }

    static class Timer {
        String nextSwitch() {
            return "22:00";
        }
    }

    static class SmartPlug {
        private final EnergyMeter meter = new EnergyMeter();
        private final Timer timer = new Timer();

        int usage(int hours) {
            return meter.wattHours(hours);
        }

        String nextSwitch() {
            return timer.nextSwitch();
        }
    }
    static sealed interface Gadget permits Bulb, Plug {
    }

    static record Bulb(String room) implements Gadget, Switchable {
    }

    static record Plug(String room, int watts) implements Gadget, Switchable {
    }
    static String label(Gadget gadget) {
        return switch (gadget) {
            case Bulb b -> b.room() + " bulb is " + b.powerOn();
            case Plug p -> p.room() + " plug draws " + p.watts() + " W";
        };
    }
    public static void main(String[] args) throws Exception {
        {
            SmartBulb bulb = new SmartBulb();
            String power = bulb.powerOn();                     // "on"
            show("power", power);
            int brightness = bulb.level();                     // 100
            show("brightness", brightness);
            boolean twoTypes = bulb instanceof Dimmable;       // true
            show("twoTypes", twoTypes);
        }
        {
            GardenSprinkler sprinkler = new GardenSprinkler();
            String plan = sprinkler.summary();                // "next run at 06:00"
            show("plan", plan);
        }
        {
            String socket = new WallSocket().describe();      // "powered device"
            show("socket", socket);
        }
        {
            String ceiling = new CeilingLight().describe();   // "light"
            show("ceiling", ceiling);
        }
        {
            String lamp = new SmartLamp().describe();         // "light + speaker"
            show("lamp", lamp);
        }
        {
            String firmware = Firmware.version();             // "2.1"
            show("firmware", firmware);
        }
        {
            SmartPlug plug = new SmartPlug();
            int used = plug.usage(5);                         // 300
            show("used", used);
            String next = plug.nextSwitch();                  // "22:00"
            show("next", next);
        }
        {
            String kitchen = label(new Bulb("kitchen"));      // "kitchen bulb is on"
            show("kitchen", kitchen);
            String desk = label(new Plug("desk", 40));        // "desk plug draws 40 W"
            show("desk", desk);
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
