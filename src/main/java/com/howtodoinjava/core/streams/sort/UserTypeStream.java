package com.howtodoinjava.core.streams.sort;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import com.howtodoinjava.core.streams.Person;

public class UserTypeStream {

    public static void main(String[] args) {
        Stream<Person> personStream = getPersonStream();

        personStream.sorted() // ascending
                .forEach(System.out::println);

        personStream = getPersonStream();

        personStream.sorted(Comparator.reverseOrder()) // ascending
                .forEach(System.out::println);

        personStream = getPersonStream();

        List<Person> sortedList = personStream.sorted(new FirstNameSorter()).toList();

        sortedList.forEach(System.out::println);

        List<Person> reverseSortedList =
                personStream.sorted(new FirstNameSorter().reversed()).toList();

        reverseSortedList.forEach(System.out::println);

    }

    private static Stream<Person> getPersonStream() {
        Person p1 = new Person(1, "Lokesh", "Gupta");
        Person p2 = new Person(5, "Lokesh", "Gupta");
        Person p3 = new Person(2, "Lokesh", "Gupta");
        Person p4 = new Person(4, "Brian", "Clooney");
        Person p5 = new Person(3, "Brian", "Clooney");
        Person p6 = new Person(6, "Alex", "Kolen");

        // Add some random persons
        Collection<Person> list = Arrays.asList(p1, p2, p3, p4, p5, p6);
        return list.stream();
    }
}
