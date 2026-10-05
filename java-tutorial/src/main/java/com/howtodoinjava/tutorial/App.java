package com.howtodoinjava.tutorial;

public class App {

    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : null;
        Greeter greeter = new Greeter();
        System.out.println(greeter.greet(name));
    }
}
