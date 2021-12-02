package com.howtodoinjava.core.streams.sort;

import java.util.Comparator;
import com.howtodoinjava.core.streams.Person;

public class FirstNameSorter implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        if (p1.getFname() == null || p2.getFname() == null) {
            throw new IllegalArgumentException(
                    "Unnamed Person found in the system");
        }
        return p1.getFname().compareToIgnoreCase(p2.getFname());
    }
}
