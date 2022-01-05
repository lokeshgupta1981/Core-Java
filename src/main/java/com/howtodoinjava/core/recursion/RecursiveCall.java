package com.howtodoinjava.core.recursion;

import java.util.stream.Stream;

@FunctionalInterface
public interface RecursiveCall<T> {
    RecursiveCall<T> apply();

    default boolean isComplete() {
        return false;
    }

    default T result() {
        throw new Error("not implemented");
    }

    default T run() {
        return Stream.iterate(this, RecursiveCall::apply)
                .filter(RecursiveCall::isComplete).findFirst().get().result();
    }

    static <T> RecursiveCall<T> done(T value) {

        return new RecursiveCall<T>() {

            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public T result() {
                return value;
            }

            @Override
            public RecursiveCall<T> apply() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
