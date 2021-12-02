package com.howtodoinjava.core.streams;

import java.util.Objects;

public class Person implements Comparable<Person> {

    public Person(Integer id,
                  String fname,
                  String lname) {
        super();
        this.id = id;
        this.fname = fname;
        this.lname = lname;
    }

    private Integer id;
    private String fname;
    private String lname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", fname=" + fname + ", lname=" + lname
               + "]";
    }

    @Override
    public int compareTo(Person p) {
        return this.getId().compareTo(p.getId());
    }
}