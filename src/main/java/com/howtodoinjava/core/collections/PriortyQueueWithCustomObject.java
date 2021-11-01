package com.howtodoinjava.core.collections;

import java.time.LocalDate;
import java.util.PriorityQueue;

public class PriortyQueueWithCustomObject {
    public static void main(String[] args) {
        PriorityQueue<Employee> priorityQueue = new PriorityQueue<>();
        
        priorityQueue.add(new Employee(1l, "AAA", LocalDate.now()));
        priorityQueue.add(new Employee(4l, "CCC", LocalDate.now()));
        priorityQueue.add(new Employee(5l, "BBB", LocalDate.now()));
        priorityQueue.add(new Employee(2l, "FFF", LocalDate.now()));
        priorityQueue.add(new Employee(3l, "DDD", LocalDate.now()));
        priorityQueue.add(new Employee(6l, "EEE", LocalDate.now()));
         
        while(!priorityQueue.isEmpty()) 
        {
            Employee e = priorityQueue.poll();
            System.out.println(e);
        }
    }
}

class Employee implements Comparable<Employee> {
    private Long id;
    private String name;
    private LocalDate dob;
 
    public Employee(Long id, String name, LocalDate dob) {
        super();
        this.id = id;
        this.name = name;
        this.dob = dob;
    }
     
    @Override
    public int compareTo(Employee emp) {
        return this.getId().compareTo(emp.getId());
    }
 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", dob=" + dob + "]";
    }
}
