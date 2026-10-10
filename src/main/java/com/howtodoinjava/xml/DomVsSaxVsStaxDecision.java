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
import org.w3c.dom.*;
import javax.xml.xpath.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stax.*;

/**
 * Examples for the tutorial "DOM vs SAX vs StAX Parser in Java: Differences and Examples".
 * https://howtodoinjava.com/java/xml/dom-vs-sax-parser-in-java/
 */
public class DomVsSaxVsStaxDecision {
    static class RecentTitles extends DefaultHandler {
        final List<String> titles = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();
        private boolean recent;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            text.setLength(0);
            if (qName.equals("book")) {
                recent = Integer.parseInt(attributes.getValue("year")) > 2005;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("title") && recent) {
                titles.add(text.toString());
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document doc = domFactory.newDocumentBuilder().parse(Path.of("books.xml").toFile());
            int domCount = doc.getElementsByTagName("book").getLength();     // 3, the whole tree is in memory
            show("domCount", domCount);

            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            saxFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            AtomicInteger books = new AtomicInteger();
            saxFactory.newSAXParser().parse(Path.of("books.xml").toFile(), new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equals("book")) {
                        books.incrementAndGet();
                    }
                }
            });
            int saxCount = books.get();                                      // 3, counted one event at a time
            show("saxCount", saxCount);
        }
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document doc = factory.newDocumentBuilder().parse(Path.of("books.xml").toFile());

            NodeList list = doc.getElementsByTagName("book");
            List<String> titles = new ArrayList<>();
            for (int i = 0; i < list.getLength(); i++) {
                Element book = (Element) list.item(i);
                titles.add(book.getElementsByTagName("title").item(0).getTextContent());
                if (Integer.parseInt(book.getAttribute("year")) < 2010) {
                    book.setAttribute("status", "classic");
                }
            }
            String allTitles = String.join(", ", titles);    // "Java Basics, Spring in Steps, XML for Teams"
            show("allTitles", allTitles);

            XPath xpath = XPathFactory.newInstance().newXPath();
            String classics = xpath.evaluate("count(//book[@status='classic'])", doc);   // "2"
            show("classics", classics);
        }
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            RecentTitles handler = new RecentTitles();
            factory.newSAXParser().parse(Path.of("books.xml").toFile(), handler);
            List<String> recent = handler.titles;         // [Java Basics, Spring in Steps]
            show("recent", recent);
        }
        {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            String firstByAsha = null;
            try (InputStream in = Files.newInputStream(Path.of("books.xml"))) {
                XMLStreamReader reader = factory.createXMLStreamReader(in);
                String title = null;
                while (reader.hasNext() && firstByAsha == null) {
                    if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                        if (reader.getLocalName().equals("title")) {
                            title = reader.getElementText();
                        } else if (reader.getLocalName().equals("author") && reader.getElementText().equals("Asha")) {
                            firstByAsha = title;
                        }
                    }
                }
                reader.close();
            }
            String found = firstByAsha;                   // "Java Basics"
            show("found", found);
        }
        {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer copier = tf.newTransformer();

            List<String> ids = new ArrayList<>();
            try (InputStream in = Files.newInputStream(Path.of("books.xml"))) {
                XMLStreamReader reader = factory.createXMLStreamReader(in);
                while (reader.hasNext()) {
                    if (reader.getEventType() == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals("book")) {
                        DOMResult result = new DOMResult();
                        copier.transform(new StAXSource(reader), result);     // reads one <book> subtree
                        Element book = ((Document) result.getNode()).getDocumentElement();
                        ids.add(book.getAttribute("id"));
                    } else {
                        reader.next();
                    }
                }
                reader.close();
            }
            String bookIds = String.join(",", ids);         // "1,2,3"
            show("bookIds", bookIds);
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
