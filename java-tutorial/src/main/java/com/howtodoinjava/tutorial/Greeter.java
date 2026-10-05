package com.howtodoinjava.tutorial;

public class Greeter {

    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, stranger!";
        }
        return "Hello, " + name.strip() + "!";
    }
}
