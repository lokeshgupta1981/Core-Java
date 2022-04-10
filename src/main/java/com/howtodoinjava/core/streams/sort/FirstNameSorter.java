package com.howtodoinjava.core.streams.sort;

import java.util.Comparator;

import com.howtodoinjava.core.streams.Person;

public class FirstNameSorter implements Comparator<Person> {

    @Override
    public int compare(final Person p1, final Person p2) {
        if (p1.fname() == null || p2.fname() == null) {
            throw new IllegalArgumentException(
                    "Unnamed Person found in the system");
        }
        return p1.fname().compareToIgnoreCase(p2.fname());
    }
}
