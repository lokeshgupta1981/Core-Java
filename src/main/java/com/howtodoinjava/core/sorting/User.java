package com.howtodoinjava.core.sorting;

import java.io.Serializable;
import java.util.Objects;

public record User(Long id, String firstName, String lastName, Integer age) 
        implements Serializable, Comparable<User> {

    public User {
        if (age < 18) {
            throw new IllegalArgumentException("You cannot hire a minor person");
        }
    }
    
    @Override
    public int compareTo(User o) {
        return this.id.intValue() - o.id.intValue();
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
        User other = (User) obj;
        return Objects.equals(id, other.id);
    }
}
