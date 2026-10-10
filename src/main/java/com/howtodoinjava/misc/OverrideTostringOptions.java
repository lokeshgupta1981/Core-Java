package com.howtodoinjava.misc;

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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import java.time.format.DateTimeFormatter;

/**
 * Examples for the tutorial "Override toString() in Java: ToStringBuilder, Records, IDE".
 * https://howtodoinjava.com/java/library/how-to-override-tostring-effectively-with-tostringbuilder/
 */
public class OverrideTostringOptions {
    static class Book {
        private final String title;
        private final String author;
        private final int pages;

        Book(String title, String author, int pages) {
            this.title = title;
            this.author = author;
            this.pages = pages;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("title", title)
                    .append("author", author)
                    .append("pages", pages)
                    .toString();
        }
    }

    static class PlainBook {
        String title = "Dune";
    }
    static class Magazine {
        private final String title;
        private final String editor;          // may be null
        private final String[] topics;

        Magazine(String title, String editor, String... topics) {
            this.title = title;
            this.editor = editor;
            this.topics = topics;
        }

        @Override
        public String toString() {
            return "Magazine{title='" + title + "', editor="
            + Objects.toString(editor, "unknown")
            + ", topics=" + Arrays.toString(topics) + "}";
        }
    }
    static record Loan(String title, String member, LocalDate due) { }
    static abstract class AbstractMember {
        private final int id;
        private final String firstName;
        private final int age;

        AbstractMember(int id, String firstName, int age) {
            this.id = id;
            this.firstName = firstName;
            this.age = age;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    static class Member extends AbstractMember {
        private final LocalDate lastVisit;

        Member(int id, String firstName, int age, LocalDate lastVisit) {
            super(id, firstName, age);
            this.lastVisit = lastVisit;
        }
    }

    static class Guest extends Member {
        private final String branch;

        Guest(int id, String firstName, int age, LocalDate lastVisit, String branch) {
            super(id, firstName, age, lastVisit);
            this.branch = branch;
        }
    }
    static class Account {
        private final String user = "lokesh";
        @ToStringExclude
        private final String password = "s3cret";

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
    static class DateStyle extends ToStringStyle {
        private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateStyle() {
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value instanceof LocalDate date) {
                value = date.format(FORMAT);
            }
            super.appendDetail(buffer, fieldName, value);
        }
    }
    static class Staff extends AbstractMember {
        private final String role;

        Staff(int id, String firstName, int age, String role) {
            super(id, firstName, age);
            this.role = role;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .appendSuper(super.toString())
                    .append("onDuty", true)
                    .toString();
        }
    }
    static class LibraryCard {
        private final int cardNo;
        private final String name;
        private final String email;
        private final String passwordHash;

        LibraryCard(int cardNo, String name, String email, String passwordHash) {
            this.cardNo = cardNo;
            this.name = name;
            this.email = email;
            this.passwordHash = passwordHash;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("cardNo", cardNo)
                    .append("name", name)
                    .toString();           // email and passwordHash stay out of logs
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String plain = new PlainBook().toString();                    // "PlainBook@7a81197d" (hash varies)
            show("plain", plain);
            String custom = new Book("Dune", "Herbert", 412).toString();   // "Book[title=Dune,author=Herbert,pages=412]"
            show("custom", custom);
        }
        {
            String full = new Magazine("Wired", "Lokesh", "AI", "Java").toString();   // "Magazine{title='Wired', editor=Lokesh, topics=[AI, Java]}"
            show("full", full);
            String noEditor = new Magazine("Byte", null).toString();                   // "Magazine{title='Byte', editor=unknown, topics=[]}"
            show("noEditor", noEditor);
        }
        {
            String formatted = "%s by %s, %d pages".formatted("Emma", "Austen", 474);   // "Emma by Austen, 474 pages"
            show("formatted", formatted);

            StringBuilder sb = new StringBuilder("Shelf[");
            for (String title : List.of("Dune", "Emma")) {
                sb.append(title).append(';');
            }
            String shelf = sb.append(']').toString();                                 // "Shelf[Dune;Emma;]"
            show("shelf", shelf);
        }
        {
            Loan loan = new Loan("Dune", "Lokesh", LocalDate.of(2026, 11, 2));
            String loanText = loan.toString();     // "Loan[title=Dune, member=Lokesh, due=2026-11-02]"
            show("loanText", loanText);
        }
        {
            String[] tags = {"classic", "sci-fi"};
            String tagged = new ToStringBuilder("Dune", ToStringStyle.NO_CLASS_NAME_STYLE)
                    .append("tags", tags)
                    .append("isbn", (Object) null)
                    .toString();                   // "[tags={classic,sci-fi},isbn=<null>]"
        }
        {
            Book dune = new Book("Dune", "Herbert", 412);
            String json = new ToStringBuilder(dune, ToStringStyle.JSON_STYLE)
                    .append("title", "Dune")
                    .append("pages", 412)
                    .toString();                   // "{"title":"Dune","pages":412}"
        }
        {
            Guest guest = new Guest(100, "Lokesh", 37, LocalDate.of(2026, 10, 1), "Delhi");
            String guestText = guest.toString();   // "Guest[branch=Delhi,lastVisit=2026-10-01,age=37,firstName=Lokesh,id=100]"
            show("guestText", guestText);
        }
        {
            String account = new Account().toString();                       // "Account[user=lokesh]"
            show("account", account);
            Guest guest = new Guest(100, "Lokesh", 37, LocalDate.of(2026, 10, 1), "Delhi");
            String byName = ReflectionToStringBuilder.toStringExclude(guest, "age", "id");   // contains branch, lastVisit and firstName only
            show("byName", byName);
        }
        {
            Guest guest = new Guest(100, "Lokesh", 37, LocalDate.of(2026, 10, 1), "Delhi");
            String dated = ToStringBuilder.reflectionToString(guest, new DateStyle());   // "Guest[branch=Delhi,lastVisit=01-10-2026,age=37,firstName=Lokesh,id=100]"
            show("dated", dated);
        }
        {
            String staff = new Staff(7, "Anita", 41, "librarian").toString();   // "Staff[role=librarian,age=41,firstName=Anita,id=7,onDuty=true]"
            show("staff", staff);
        }
        {
            Guest guest = new Guest(100, "Lokesh", 37, LocalDate.of(2026, 10, 1), "Delhi");
            String upTo = ReflectionToStringBuilder.toString(guest, ToStringStyle.SHORT_PREFIX_STYLE,
            false, false, Member.class);   // "Guest[branch=Delhi,lastVisit=2026-10-01]"
        }
        {
            Book dune = new Book("Dune", "Herbert", 412);
            LibraryCard card = new LibraryCard(42, "Lokesh", "lokesh@example.com", "x9f2");
            String logLine = "Checkout failed for " + card + ", book " + dune;   // "Checkout failed for LibraryCard[cardNo=42,name=Lokesh], book Book[title=Dune,author=Herbert,pages=412]"
            show("logLine", logLine);
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
