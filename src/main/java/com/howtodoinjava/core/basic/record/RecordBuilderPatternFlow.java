package com.howtodoinjava.core.basic.record;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import lombok.Builder;
import lombok.Singular;

/**
 * Examples for the tutorial "Builder Pattern for Java Records: Nested, Lombok, RecordBuilder".
 * https://howtodoinjava.com/java/basics/builder-pattern-for-java-records/
 */
public class RecordBuilderPatternFlow {
    static public record User(long id, String name, String email, boolean active) {

        public static final class Builder {

            private final long id;
            private final String name;
            private String email = "";
            private boolean active = true;

            public Builder(long id, String name) {
                this.id = id;
                this.name = name;
            }

            public Builder email(String email) {
                this.email = email;
                return this;
            }

            public Builder active(boolean active) {
                this.active = active;
                return this;
            }

            public User build() {
                return new User(id, name, email, active);
            }
        }
    }
    static public record Ticket(String title, int priority, String assignee) {

        public Ticket {
            Objects.requireNonNull(title, "title is required");
            if (priority < 1 || priority > 5) {
                throw new IllegalArgumentException("priority must be 1-5: " + priority);
            }
        }

        public static Builder builder(String title) {
            return new Builder(title);
        }

        public static final class Builder {
            private final String title;
            private int priority = 3;
            private String assignee = "unassigned";

            private Builder(String title) {
                this.title = title;
            }

            public Builder priority(int priority) {
                this.priority = priority;
                return this;
            }

            public Builder assignee(String assignee) {
                this.assignee = assignee;
                return this;
            }

            public Ticket build() {
                return new Ticket(title, priority, assignee);
            }
        }
    }
    static public record Member(long id, String name, boolean active) {

        public Builder toBuilder() {
            return new Builder(id, name).active(active);
        }

        public static final class Builder {
            private final long id;
            private final String name;
            private boolean active = true;

            public Builder(long id, String name) {
                this.id = id;
                this.name = name;
            }

            public Builder active(boolean active) {
                this.active = active;
                return this;
            }

            public Member build() {
                return new Member(id, name, active);
            }
        }
    }
    @Builder(toBuilder = true)
    static public record UserProfile(String username, String theme, @Singular List<String> roles) {

        public UserProfile {
            Objects.requireNonNull(username, "username is required");
            if (theme == null) {
                theme = "light";
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            User user = new User.Builder(1L, "Lokesh")
                    .email("lokesh@example.com")
                    .build();
            String email = user.email();            // "lokesh@example.com"
            show("email", email);
            boolean active = user.active();         // true (builder default)
            show("active", active);
        }
        {
            User admin = new User.Builder(2L, "Anna")
                    .email("anna@example.com")
                    .active(false)
                    .build();
            String adminText = admin.toString();    // "User[id=2, name=Anna, email=anna@example.com, active=false]"
            show("adminText", adminText);
            User guest = new User.Builder(3L, "Guest").build();
            String guestEmail = guest.email();      // ""
            show("guestEmail", guestEmail);
        }
        {
            Ticket bug = Ticket.builder("Login fails").priority(1).build();
            int level = bug.priority();             // 1
            show("level", level);
            String owner = bug.assignee();          // "unassigned"
            show("owner", owner);
            try { Ticket bad = Ticket.builder("Typo").priority(9).build(); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
            try { Ticket noTitle = Ticket.builder(null).build(); show("noTitle", noTitle); } catch (Throwable _t) { System.out.println("noTitle -> " + _t); }
        }
        {
            Member member = new Member.Builder(7L, "Lokesh").build();
            Member disabled = member.toBuilder().active(false).build();
            boolean before = member.active();       // true
            show("before", before);
            boolean after = disabled.active();      // false
            show("after", after);
            boolean sameName = disabled.name().equals(member.name());   // true
            show("sameName", sameName);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
