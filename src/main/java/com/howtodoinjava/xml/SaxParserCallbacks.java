package com.howtodoinjava.xml;

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
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.XMLConstants;
import javax.xml.validation.*;

/**
 * Examples for the tutorial "Java SAX Parser: Read XML with DefaultHandler (Examples)".
 * https://howtodoinjava.com/java/xml/sax-parser-read-xml-example/
 */
public class SaxParserCallbacks {
    static record Recipe(int id, String name, int minutes) {}
    static class RecipeHandler extends DefaultHandler {
        private final List<Recipe> recipes = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();
        private int id;
        private int minutes;
        private String name;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            text.setLength(0);
            if (qName.equals("recipe")) {
                id = Integer.parseInt(attributes.getValue("id"));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case "name" -> name = text.toString().strip();
                case "minutes" -> minutes = Integer.parseInt(text.toString().strip());
                case "recipe" -> recipes.add(new Recipe(id, name, minutes));
                default -> { }
            }
        }

        List<Recipe> recipes() {
            return recipes;
        }
    }
    static class LastChunkHandler extends DefaultHandler {
        final List<String> names = new ArrayList<>();
        private boolean inName;
        private String last;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            inName = qName.equals("name");
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inName) {
                last = new String(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("name")) {
                names.add(last);
            }
            inName = false;
        }
    }
    static SAXParser secureParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setNamespaceAware(true);
        return factory.newSAXParser();
    }
    static SAXParser validatingParser(Path xsd) throws ParserConfigurationException, SAXException {
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd.toFile());
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setNamespaceAware(true);
        factory.setSchema(schema);
        return factory.newSAXParser();
    }
    static class StrictRecipeHandler extends RecipeHandler {
        @Override
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }
    }
    static class StopParsing extends SAXException {
        StopParsing() {
            super("match found");
        }
    }
    static class FirstSlowRecipe extends RecipeHandler {
        String found;
        int seen;

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("recipe")) {
                seen++;
                Recipe last = recipes().getLast();
                if (last.minutes() > 30) {
                    found = last.name();
                    throw new StopParsing();
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            List<String> names = new ArrayList<>();
            factory.newSAXParser().parse(Path.of("recipes.xml").toFile(), new DefaultHandler() {
                private final StringBuilder text = new StringBuilder();

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    text.setLength(0);
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    text.append(ch, start, length);
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (qName.equals("name")) {
                        names.add(text.toString());
                    }
                }
            });
            int count = names.size();                     // 3
            show("count", count);
            String third = names.get(2);                  // "Mac & Cheese"
            show("third", third);
        }
        {
            RecipeHandler handler = new RecipeHandler();
            secureParser().parse(Path.of("recipes.xml").toFile(), handler);
            List<Recipe> recipes = handler.recipes();
            int count = recipes.size();                                               // 3
            show("count", count);
            Recipe third = recipes.get(2);                                            // Recipe[id=3, name=Mac & Cheese, minutes=25]
            show("third", third);
            long quick = recipes.stream().filter(r -> r.minutes() <= 25).count();     // 2
            show("quick", quick);
        }
        {
            LastChunkHandler wrong = new LastChunkHandler();
            secureParser().parse(Path.of("recipes.xml").toFile(), wrong);
            String first = wrong.names.get(0);            // "Pancakes"
            show("first", first);
            String broken = wrong.names.get(2);           // " Cheese"
            show("broken", broken);
        }
        {
            String untrusted = """
            <?xml version="1.0"?>
            <!DOCTYPE recipe [<!ENTITY x SYSTEM "file:secret.txt">]>
            <recipe>&x;</recipe>""";
            try { secureParser().parse(new InputSource(new StringReader(untrusted)), new DefaultHandler());  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            String notWellFormed = "<recipes><recipe id=\"4\"><name>Toast</recipe></recipes>";
            try { secureParser().parse(new InputSource(new StringReader(notWellFormed)), new RecipeHandler());  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            RecipeHandler lenient = new RecipeHandler();
            validatingParser(Path.of("recipes.xsd")).parse(Path.of("bad-recipes.xml").toFile(), lenient);
            int parsed = lenient.recipes().size();        // 1, the error was ignored
            show("parsed", parsed);

            try { validatingParser(Path.of("recipes.xsd")).parse(Path.of("bad-recipes.xml").toFile(), new StrictRecipeHandler());  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            FirstSlowRecipe finder = new FirstSlowRecipe();
            try {
                secureParser().parse(Path.of("recipes.xml").toFile(), finder);
            } catch (StopParsing done) {
                // expected, the handler found a match
            }
            String found = finder.found;                  // "Tomato Soup"
            show("found", found);
            int seen = finder.seen;                       // 2
            show("seen", seen);
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
