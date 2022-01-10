package com.howtodoinjava.core.streams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Person implements Comparable<Person> {

    private Integer id;
    private String fname;
    private String lname;

    @Override
    public int compareTo(final Person p) {
        return this.getId().compareTo(p.getId());
    }
}