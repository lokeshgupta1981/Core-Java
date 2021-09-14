package com.howtodoinjava.core.streams;

import java.time.LocalDate;

class Employee {
    private long id;
    private String name;
    private LocalDate dateOfBirth;
    private double salary;

    public Employee(final long id, final String name,
        final LocalDate dateOfBirth, final double salary) {
        super();
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(final double salary) {
        this.salary = salary;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", dateOfBirth="
            + dateOfBirth + ", salary=" + salary + "]";
    }
}