package com.howtodoinjava.tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class GreeterTest {

    private final Greeter greeter = new Greeter();

    @Test
    void greetsByName() {
        assertEquals("Hello, Lokesh!", greeter.greet("Lokesh"));
    }

    @Test
    void stripsSpaces() {
        assertEquals("Hello, Lokesh!", greeter.greet("  Lokesh "));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void greetsStrangerWhenNameIsMissing(String name) {
        assertEquals("Hello, stranger!", greeter.greet(name));
    }
}
