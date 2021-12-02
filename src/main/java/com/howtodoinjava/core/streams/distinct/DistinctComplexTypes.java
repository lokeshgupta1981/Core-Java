package com.howtodoinjava.core.streams.distinct;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DistinctComplexTypes 
{
    public static void main(String[] args) 
    {
        Person lokeshOne = new Person(1, "Lokesh", "Gupta");
        Person lokeshTwo = new Person(2, "Lokesh", "Gupta");
        Person lokeshThree = new Person(3, "Lokesh", "Gupta");
        Person brianOne = new Person(4, "Brian", "Clooney");
        Person brianTwo = new Person(5, "Brian", "Clooney");
        Person alex = new Person(6, "Alex", "Kolen");
         
        //Add some random persons
        Collection<Person> list = Arrays.asList(alex, 
                                                brianOne, 
                                                brianTwo, 
                                                lokeshOne, 
                                                lokeshTwo, 
                                                lokeshThree);

        //1 - Using default equals() methods
        
        // Get distinct people by id
        List<Person> distinctElements = list.stream()
                    .distinct()
                    .collect( Collectors.toList() );

        // Let's verify distinct people
        System.out.println( distinctElements );
        
        //2 - Using custom equality
        
        // Get distinct objects by key
        distinctElements = list.stream()
                    .filter( distinctByKey(p -> p.getFname() + " " + p.getLname()) )
                    .collect( Collectors.toList() );
        
        // Again verify distinct people
        System.out.println( distinctElements );
    }
    
    //Utility function
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

class Person 
{
    public Person(Integer id, String fname, String lname) {
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
        return "Person [id=" + id + ", fname=" + fname + ", lname=" + lname + "]";
    }
}